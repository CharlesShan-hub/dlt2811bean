# jcms-security — 国密安全模块

## 职责

jcms-security 实现国密（GM）算法支持，涵盖 **SM2 签名/验签**、**SM3 哈希**、**SM4 加密套件**、**X.509 证书管理**以及 **TLS 国密通信**。

这是 CMS 协议"穿上防弹衣"的关键模块——DL/T 2811 原生支持国密加密和签名认证。

## 架构

```
安全模块层次：
┌────────────────────────────────────────────────────────┐
│                    SecurityContext                       │  ← 一站式安全上下文
│  (组合 credentialManager + authenticator + certificate) │
├────────────────────────────────────────────────────────┤
│                                                         │
│  ┌─────────────────┐  ┌──────────────────────────┐     │
│  │ GmCredentialManager│ │ GmAuthenticator           │     │
│  │ (凭证管理)        │  │ (关联认证校验)            │     │
│  └──────┬──────────┘  └────────┬─────────────────┘     │
│         │                      │                        │
│  ┌──────▼──────────┐  ┌───────▼──────────────────┐     │
│  │ GmSignature     │  │ GmCertificateParser       │     │
│  │ (SM2签名/SM3哈希) │  │ (证书解析/格式转换)      │     │
│  └────────┬────────┘  └───────────┬──────────────┘     │
│           │                       │                      │
│  ┌────────▼───────────────────────▼──────────────┐     │
│  │              GmTrustManager                    │     │
│  │         (信任证书管理 / 指纹匹配)               │     │
│  └───────────────────────────────────────────────┘     │
├────────────────────────────────────────────────────────┤
│                    GmSslContext                         │  ← TLS/SSL 层
│              (GM TLS 上下文工厂)                        │
└────────────────────────────────────────────────────────┘
```

## 类详解

### 1. `GmSignature` — SM2 签名/验签 + SM3 哈希

包路径：`com.ysh.jcms.utils.security.GmSignature`

核心密码学工具，基于 BouncyCastle 实现。

#### 签名与验签

```java
// 签名：返回 64 字节 raw 格式 (r || s)
byte[] sig = GmSignature.sign(privateKey, message);

// 验签：返回 true/false
boolean valid = GmSignature.verify(publicKey, message, sig);
```

签名格式符合 DL/T 2811-2024 要求：**OCTET STRING (SIZE(64))**。

内部实现自动处理 DER ↔ raw 格式转换：
- `sign()` 输出 SM3withSM2 DER 签名 → 转换为 64 字节 `r || s`
- `verify()` 将 64 字节 `r || s` → 转换为 DER 格式 → SM3withSM2 验签

#### SM3 哈希

```java
byte[] hash = GmSignature.sm3(data);           // 32 字节
String hex = GmSignature.sm3Hex("hello");       // hex 字符串
```

#### 密钥管理

| 方法 | 说明 |
|------|------|
| `generateKeyPair()` | 生成 SM2 密钥对（sm2p256v1 曲线） |
| `generateSelfSignedCertificate(keyPair)` | 生成自签名 X.509 证书 |
| `decodePublicKey(hex/bytes)` | 从 hex 或字节数组解码 SM2 公钥（支持 X.509 SPKI 和 raw EC point） |
| `decodePrivateKey(hex/bytes)` | 解码 SM2 私钥（支持 PKCS#8 和 raw d 值） |

---

### 2. `GmCredentialManager` — 凭证管理器

包路径：`com.ysh.jcms.utils.security.GmCredentialManager`

负责 SM2 证书和私钥的加载与管理。

#### 创建方式

```java
// 从 PKCS12 密钥库加载（服务端/客户端）
GmCredentialManager cm = GmCredentialManager.forServer("server.pfx", "password", "alias");

// 从证书文件加载（仅验证用途）
GmCredentialManager cm = GmCredentialManager.fromCertificate("ca.cer");

// 从已有密钥对和证书构造
GmCredentialManager cm = GmCredentialManager.fromKeyAndCert(privateKey, certificate);
```

#### 提供的能力

| 方法 | 说明 |
|------|------|
| `getCertificate()` | 获取 X.509 证书 |
| `getPrivateKey()` / `getPublicKey()` | 获取公私钥 |
| `validateCertificate(Date)` | 校验证书有效期 |
| `getCertificateHex()` / `getCertificateBase64()` | 证书编码转换 |
| `getCertificateFingerprint()` | SHA-256 证书指纹 |

资源加载策略：优先文件系统，后 classpath。

---

### 3. `GmCertificateParser` — 证书解析器

包路径：`com.ysh.jcms.utils.security.GmCertificateParser`

X.509 证书解析和格式转换：

```java
// 从 DER 字节解析
X509Certificate cert = GmCertificateParser.parseX509(certBytes);

// 从 Base64 解析
X509Certificate cert = GmCertificateParser.parseFromBase64(base64Str);

// 从 PEM 解析（含 -----BEGIN CERTIFICATE----- 头尾）
X509Certificate cert = GmCertificateParser.parseFromPem(pemStr);

// 格式转换
String hex = GmCertificateParser.toHex(cert);
String b64 = GmCertificateParser.toBase64(cert);
String pem = GmCertificateParser.toPem(cert);

// 提取信息
String subject = GmCertificateParser.getSubject(cert);
String serial = GmCertificateParser.getSerialNumberHex(cert);
String fp = GmCertificateParser.getFingerprintSha256(cert);
```

---

### 4. `GmTrustManager` — 信任管理器

包路径：`com.ysh.jcms.utils.security.GmTrustManager`

管理受信证书集合，支持两种信任模式：

**精确匹配模式**（生产环境）：
```java
GmTrustManager tm = new GmTrustManager()
    .addTrustedCertificate(serverCert)          // 按证书对象
    .addTrustedFingerprint("AB...");            // 按 SHA-256 指纹
```

**信任所有模式**（开发/测试）：
```java
GmTrustManager tm = new GmTrustManager().trustAll();
```

`isTrusted(X509Certificate)` 的校验顺序：trust-all → 指纹匹配 → 证书对象匹配。

---

### 5. `GmAuthenticator` — 关联认证校验器

包路径：`com.ysh.jcms.utils.security.GmAuthenticator`

按照 DL/T 2811-2024 标准校验 Associate 服务中的认证参数。校验流程：

```
validate(authParam, signedData)
    │
    ├─ 1. 检查认证参数是否存在
    ├─ 2. 解析客户端证书
    ├─ 3. 校验证书有效期
    ├─ 4. 信任校验（精确匹配 / trustManager）
    ├─ 5. 签名时间戳校验（防重放攻击，默认 5 分钟窗口）
    └─ 6. SM2 签名验签
         └─ 通过 → Optional.empty() / 失败 → 对应错误码
```

构造方式：
```java
// 单证书模式
GmAuthenticator auth = new GmAuthenticator(trustedCert);

// TrustManager 模式
GmAuthenticator auth = new GmAuthenticator(trustManager);

// 自定义时间容忍窗口（秒）
GmAuthenticator auth = new GmAuthenticator(trustManager, 600);
```

---

### 6. `GmSslContext` — GM TLS 上下文工厂

包路径：`com.ysh.jcms.utils.security.GmSslContext`

使用 Builder 模式创建支持国密套件的 SSLContext。

#### 支持的密码套件

**国密套件**（BouncyCastle JSSE）：
- `TLS_ECDHE_ECDSA_WITH_SM4_SM3`（RFC 风格）
- `TLS_ECDHE_RSA_WITH_SM4_SM3`
- `ECDHE_SM4_SM3`（GmSSL 风格）

**标准 TLS 套件**（备用/测试）：
- `TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256`
- `TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256`

#### 使用方式

```java
// 服务端
GmSslContext ctx = GmSslContext.forServer()
    .keyStore("server.pfx", "password")
    .trustStore("client.cer")
    .build();
SSLContext sslCtx = ctx.getSslContext();

// 客户端
GmSslContext ctx = GmSslContext.forClient()
    .keyStore("client.pfx", "password")
    .trustCertificate("server.cer")
    .build();

// 使用标准 TLS（绕过 GM JSSE 依赖）
GmSslContext ctx = GmSslContext.forServer()
    .keyStore("server.jks", "password")
    .trustStore("ca.jks", "password")
    .useStandardTls()
    .build();
```

自动注册 BouncyCastle 和 BouncyCastleJSSE Provider，支持从文件或 classpath 加载密钥库和证书。

---

### 7. `SecurityContext` — 一站式安全上下文

包路径：`com.ysh.jcms.utils.security.SecurityContext`

将凭证管理器、认证器、服务端证书封装在一起，提供简单的工厂方法：

```java
// 开发环境：自签名
SecurityContext ctx = SecurityContext.generateSelfSigned();
node.setCredentialManager(ctx.credentialManager());

// 生产环境：从配置加载
// (由 application.yaml 的 security.* 配置驱动，
//  通过 CmsConfigInjector 注入到相应组件)
```

提供三个组件：
- `credentialManager()` — 客户端签名用的私钥和证书
- `authenticator()` — 服务端校验客户端认证参数
- `certificate()` — 服务端证书（响应中带回给客户端）

---

## 算法标准依据

| 算法 | 标准 |
|------|------|
| SM2 签名 | GB/T 32918.4-2016 |
| SM2 密钥对 | sm2p256v1 曲线（GMNamedCurves） |
| SM3 哈希 | GB/T 32905-2016 |
| SM4 加密 | GB/T 32907-2016 |
| X.509 证书 | GB/T 20518-2016 |
| GM TLS | GB/T 38636-2020 |

---

## 与配置模块的关系

安全配置通过 `application.yaml` 驱动：

```yaml
security:
  enabled: false                    # 总开关
  keystore:
    path: "certs/server.pfx"        # 服务端密钥库
    password: "changeit"
  truststore:
    path: "certs/ca.cer"            # 信任证书
    password: "changeit"
```

`CmsConfigInjector` 将以上配置注入到 `GmSslContext` 的 builder 中，实现安全配置的集中管理。

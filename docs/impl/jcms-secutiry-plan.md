# jcms-security 真实认证改造计划

## 一、现状诊断

底层密码学（SM2 签名/验签、SM3、证书解析）是**真实实现**（BouncyCastle），但认证流程是"假"的——默认情况下任何客户端都能关联成功，证书/签名形同虚设。

三个硬伤 + 两个隐患：

| # | 位置 | 问题 | 后果 |
| --- | --- | --- | --- |
| ① | `AssociateServer` L38-43 | 校验逻辑被包在 `if (带了认证参数)` 里 | 客户端**不带**认证参数就完全跳过认证，直接放行 |
| ② | `SecurityContext.fromConfig` L58-60 | `security.enabled=false` → `generateSelfSigned()`（trustAll） | 默认模式下任何证书都信任 |
| ③ | `CmsNode.initCredentialManager` L57-63 | 硬编码 `generateSelfSigned()` | 客户端永远用自签名证书，忽略 `security.keystore` 配置 |
| ④ | `AssociateClient.beforeAll` L32-41 | `if(!secure) authParam(null)` 后又被无条件 `buildAuthParam` 覆盖 | `secure` 标志失效，非 secure 也照样带签名 |
| ⑤ | `AssociateClient` L77 vs `signedTime` | 签名时间用 `now/1000`，字段却是另一次 `now()` | 跨秒边界时验签数据对不上，验签可能失败 |

## 二、改造目标

1. 服务端支持**三个档位**（见下），能真正校验或强制校验。
2. 保留开发模式：`enabled=false` 时自认证（trustAll），回环测试不受影响。
3. 客户端从配置加载真实凭证（`enabled=true` 时）。
4. 假设客户端/服务器**在不同机器、甚至不是同一方编写**，安全语义必须按协议独立成立。

## 三、方案设计：两个正交开关

把「是否启用真实认证」和「是否强制客户端认证」拆成两个独立开关，组合出三个有效档位。

```yaml
security:
  enabled: true     # 是否启用真实认证机制（false=自认证/trustAll）
  required: true    # 是否强制客户端认证（仅 enabled=true 时有效）
```

### 档位表

| `enabled` | `required` | 档位 | 语义 |
| --- | --- | --- | --- |
| `false` | `false` | **档位 1：不认证** | 服务器自认证（自签名 + trustAll），客户端爱带不带，带了也放行 |
| `true` | `false` | **档位 2：可选认证** | 服务器真实 CA 校验；客户端带了就走真实流程，不带也放行 |
| `true` | `true` | **档位 3：强制认证** | 服务器真实 CA 校验 + 必须携带，缺或验不过就拒绝 |
| `false` | `true` | ⚠️ 无效 | 未启用却强制，代码里显式拒绝或忽略并告警 |

### 关键语义

- **`enabled` 决定"怎么验"**：`false` → trustAll（自认证）；`true` → CA 签发校验（`cert.verify(caKey)`）。
- **`required` 决定"要不要验"**：`true` → 客户端必须带认证参数；`false` → 客户端可带可不带。
- `required` 只在 `enabled=true` 时有意义；`enabled=false` 时无论 `required` 如何，都按 trustAll 处理（若配了 `required=true` 应告警）。

## 四、分步任务

### 阶段 1：修客户端凭证加载 + 签名一致性

- [ ] **1.1** `CmsNode.initCredentialManager()`：改为 `SecurityContext.fromConfig(CmsConfigLoader.load())`，`enabled=true` 时从 `security.keystore` 加载真实凭证，`false` 时自签名。
- [ ] **1.2** `AssociateClient.beforeAll()`：让 `secure` 标志真正生效——`secure=false` 时不构建 authParam；`secure=true` 时才签名。
- [ ] **1.3** 修签名时间戳一致性：先构造 `CmsUtcTime now = new CmsUtcTime().now()`，用 `now.secondsSinceEpoch.value()` 作为签名数据，再 `signedTime(now)`，消除两次取时的偏差。

### 阶段 2：服务端三档位校验

- [ ] **2.1** `CmsConfig.Security` 新增 `required` 字段（默认 `false`），并纳入 `merge()`。
- [ ] **2.2** `AssociateSecurity.validate()`：`authenticator == null` 时**不能**静默通过（当前直接 `return NO_ERROR`），应返回"认证不可用"错误码或抛错。
- [ ] **2.3** `AssociateServer` 按档位表实现：
  - `enabled=false` → 不校验（现状）。
  - `enabled=true, required=false` → 带了就验，没带放行。
  - `enabled=true, required=true` → 没带直接拒绝；带了走真实校验。
- [ ] **2.4** 处理无效组合 `enabled=false, required=true`：启动时告警，按 `enabled=false`（trustAll）处理。

### 阶段 3：配置与文档

- [ ] **3.1** 确认 `application.yaml` 的 `security.*` 配置项齐全（enabled / required / timeTolerance / keystore / truststore）。
- [ ] **3.2** 更新 [05b-jcms-security.md](05b-jcms-security.md)，说明三档位、两个开关、真实认证的开启方式和校验流程。

## 五、测试验证

| 场景 | enabled | required | 预期 |
| --- | --- | --- | --- |
| 回环测试（现状） | false | false | 关联成功，不强制 |
| 无认证参数 | true | false | 关联成功（可选） |
| 无认证参数 | true | true | 服务端拒绝 |
| 错误/过期/不受信证书 | true | true | 服务端拒绝 |
| 正确证书 + 签名 | true | true | 关联成功 |
| 时间戳跨秒边界 | true | true | 验签稳定（不再因两次取时失败） |
| 无效组合 | false | true | 启动告警，按 trustAll 处理 |

## 六、决策记录

1. **开关方案**：两个正交开关 `enabled` + `required`（非单个 mode 枚举）。
2. **命名**：`enabled` / `required`，准确表达"认证"而非"加密"。
3. **信任方式**：`enabled=true` 时用 CA 签发校验（`cert.verify(caKey)`）。
4. **客户端凭证**：`enabled=true` 时复用 `security.keystore`（与服务端共用配置）。

## 七、非目标（本次不做）

- 不改 GM TLS（`GmSslContext` / `InnerServer` 的 TLS 目前仍是 RSA 自签名，属另一条线）。
- 不引入真实 PKI / 证书吊销（CRL/OCSP）。
- 不改认证之外的安全功能。

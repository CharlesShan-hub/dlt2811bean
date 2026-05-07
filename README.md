# dlt2811bean

**DL/T 2811-2024 CMS 协议 Java 实现** — 新国标变电站二次系统通信

---

## 数据对象映射 - datatypes

| 章节            | 类型      | 类名                  | 标记  | en/de | test |
| :-------------: | :------: | :------------------- | :---: | :---: | :--: |
| 7.1.1 基础类型   | 布尔     | CMSBoolean           | base  |   ✅   |  ✅   |
| 7.1.2 基础类型   | 整数     | CmsInt8              | base  |   ✅   |  ✅   |
| 7.1.2 基础类型   | 整数     | CmsInt16             | base  |   ✅   |  ✅   |
| 7.1.2 基础类型   | 整数     | CmsInt32             | base  |   ✅   |  ✅   |
| 7.1.2 基础类型   | 整数     | CmsInt64             | base  |   ✅   |  ✅   |
| 7.1.3 基础类型   | 整数     | CmsInt8U             | base  |   ✅   |  ✅   |
| 7.1.3 基础类型   | 整数     | CmsInt16U            | base  |   ✅   |  ✅   |
| 7.1.3 基础类型   | 整数     | CmsInt32U            | base  |   ✅   |  ✅   |
| 7.1.3 基础类型   | 整数     | CmsInt64U            | base  |   ✅   |  ✅   |
| 7.1.4 基础类型   | 浮点数   | CmsFloat32           | base  |   ✅   |  ✅   |
| 7.1.4 基础类型   | 浮点数   | CmsFloat64           | base  |   ✅   |  ✅   |
| 7.1.5 基础类型   | 字符串   | CmsOctetString       | base  |   ✅   |  ✅   |
| 7.1.5 基础类型   | 字符串   | CmsVisibleString     | base  |   ✅   |  ✅   |
| 7.1.5 基础类型   | 字符串   | CmsUtf8String        | base  |   ✅   |  ✅   |
| 7.1.5 基础类型   | 字符串   | CmsBitString         | base  |   ✅   |  ✅   |
| 7.1.6 基础类型   | 枚举     | CmsEnumerated        | base  |   ✅   |  ✅   |
| 7.1.7 基础类型   | 编码枚举 | CmsCodedEnum         | base  |   ✅   |  ✅   |
| 7.1.8 基础类型   | 压缩列表 | CmsPackedList        | base  |   ✅   |  ✅   |
| 7.2.1 扩展类型   | 协调世界时 | CmsUtcTime         | bean  |   ✅   |  ✅   |
| 7.2.1 扩展类型   | 时间质量 | CmsTimeQuality       | code  |   ✅   |  ✅   |
| 7.2.2 扩展类型   | 二进制时间 | CmsBinaryTime      | bean  |   ✅   |  ✅   |
| 7.3.1 公共ACSI   | 对象名   | CmsObjectName        | bean  |   ✅   |  ✅   |
| 7.3.2 公共ACSI   | 对象引用 | CmsObjectReference   | bean  |   ✅   |  ✅   |
| 7.3.3 公共ACSI   | 子引用   | CmsSubReference      | bean  |   ✅   |  ✅   |
| 7.3.4 公共ACSI   | 二进制时间 | CmsTimeStamp       | copy  |   -   |  -   |
| 7.3.5 公共ACSI   | 双点位置 | CmsDbpos             | enum  |   ✅   |  ✅   |
| 7.3.6 公共ACSI   | 品质     | CmsQuality           | code  |   ✅   |  ✅   |
| 7.3.7 公共ACSI   | 挡位命令 | CmsTcmd              | enum  |   ✅   |  ✅   |
| 7.3.8 公共ACSI   | 条目标识 | CmsEntryID           | bean  |   ✅   |  ✅   |
| 7.3.9 公共ACSI   | 条目时间 | CmsEntryTime         | copy  |   -   |  -   |
| 7.3.10 公共ACSI  | 文件条目 | CmsFileEntry        | bean  |   ✅   |  ✅   |
| 7.3.11 公共ACSI  | 服务错误 | CmsServiceError     | enum  |   ✅   |  ✅   |
| 7.3.12 公共ACSI  | 物理通信地址 | CmsPhyComAddr     | bean  |   ✅   |  ✅   |
| 7.4 功能约束     | 功能约束 | CmsFC                | bean  |   ✅   |  ✅   |
| 7.5.1 控制对象属性| 控制对象属性 | -               |   -   |   -   |  -   |
| 7.5.2 控制操作   | 发出者   | CmsOriginator         | bean  |   ✅   |  ✅   |
| 7.5.2 控制操作   | 发出者-内部字段 | CmsOrCat         | enum  |   ✅   |  ✅   |
| 7.5.3 控制操作   | 检测     | CmsCheck              | code  |   ✅   |  ✅   |
| 7.5.4 控制操作   | 附加原因 | CmsAddCause           | enum  |   ✅   |  ✅   |
| 7.6.1 控制块属性 | 控制块属性 | -                   |   -   |   -   |  -   |
| 7.6.2 控制块     | 触发条件 | CmsTriggerConditions  | code  |   ✅   |  ✅   |
| 7.6.3 控制块     | 触发原因 | CmsReasonCode         | code  |   ✅   |  ✅   |
| 7.6.4 控制块     | 缓存报告控制块选项域 | CmsRcbOptFlds | code  |   ✅   |  ✅   |
| 7.6.5 控制块     | 日志控制块选项域 | CmsLcbOptFlds       | code  |   ✅   |  ✅   |
| 7.6.6 控制块     | 多播采样值控制块选项域 | CmsMsvcbOptFlds | code  |   ✅   |  ✅   |
| 7.6.7 控制块     | 采样模式 | CmsSmpMod             | enum  |   ✅   |  ✅   |
| 7.7.1 数据       | 数据     | CmsData               | choice|   ✅   |  ✅   |
| 7.7.1 数据       | 内容相同的列表 | CmsArray        | array |   ✅   |  ✅   |
| 7.7.1 数据       | 内容可不同的列表 | CmsStructure  | array |   ✅   |  ✅   |

---

## 服务

```plaintext
service/
├── CmsService.java              ← 已有：APCH 基类
├── CmsServiceError.java         ← 已有：错误码枚举
│
├── association/                 ← 关联服务组 (SC=1,2,3,154)
│   ├── CmsAssociateNegotiate.java   ← SC=154 (新建)
│   ├── CmsAssociate.java            ← SC=1 (已有 Cms01 改名)
│   ├── CmsRelease.java              ← SC=3 (已有 Cms03 改名)
│   └── CmsAbort.java                ← SC=2 (已有 Cms02 改名)
│
├── directory/                   ← 目录服务组 (SC=80~83,155,156)
│   ├── CmsGetServerDirectory.java
│   ├── CmsGetLogicDeviceDirectory.java
│   ├── CmsGetLogicNodeDirectory.java
│   ├── CmsGetAllDataValues.java
│   ├── CmsGetAllDataDefinition.java
│   └── CmsGetAllCBValues.java
│
├── data/                        ← 数据服务组 (SC=48~51)
│   ├── CmsGetDataValues.java
│   ├── CmsSetDataValues.java
│   ├── CmsGetDataDirectory.java
│   └── CmsGetDataDefinition.java
│
├── dataset/                     ← 数据集服务组 (SC=54~59)
│   ├── CmsCreateDataSet.java
│   ├── CmsDeleteDataSet.java
│   ├── CmsGetDataSetDirectory.java
│   ├── CmsGetDataSetValues.java
│   └── CmsSetDataSetValues.java
│
├── control/                     ← 控制服务组 (SC=68~74)
│   ├── CmsSelect.java
│   ├── CmsSelectWithValue.java
│   ├── CmsCancel.java
│   ├── CmsOperate.java
│   ├── CmsCommandTermination.java
│   ├── CmsTimeActivatedOperate.java
│   └── CmsTimeActivatedOperateTermination.java
│
├── report/                      ← 报告服务组 (SC=90~94)
│   ├── CmsReport.java
│   ├── CmsGetBRCBValues.java
│   ├── CmsSetBRCBValues.java
│   ├── CmsGetURCBValues.java
│   └── CmsSetURCBValues.java
│
├── setting/                     ← 定值组+日志 (SC=84~89,95~99)
│   ├── CmsSelectActiveSG.java
│   ├── CmsSelectEditSG.java
│   ├── CmsSetEditSGValue.java
│   ├── CmsConfirmEditSGValues.java
│   ├── CmsGetEditSGValue.java
│   ├── CmsGetSGCBValues.java
│   ├── CmsGetLCBValues.java
│   ├── CmsSetLCBValues.java
│   ├── CmsQueryLogByTime.java
│   ├── CmsQueryLogAfter.java
│   └── CmsGetLogStatusValues.java
│
├── goose/                       ← GOOSE 控制块 (SC=102,103)
│   ├── CmsGetGoCBValues.java
│   └── CmsSetGoCBValues.java
│
├── sv/                          ← 采样值控制块 (SC=105,106)
│   ├── CmsGetMSVCBValues.java
│   └── CmsSetMSVCBValues.java
│
├── file/                        ← 文件服务 (SC=128~132)
│   ├── CmsGetFile.java
│   ├── CmsSetFile.java
│   ├── CmsDeleteFile.java
│   ├── CmsGetFileAttributeValues.java
│   └── CmsGetFileDirectory.java
│
├── rpc/                         ← RPC 服务 (SC=110~114)
│   ├── CmsGetRpcInterfaceDirectory.java
│   ├── CmsGetRpcMethodDirectory.java
│   ├── CmsGetRpcInterfaceDefinition.java
│   ├── CmsGetRpcMethodDefinition.java
│   └── CmsRpcCall.java
│
└── misc/                        ← 杂项 (SC=153)
    └── CmsTest.java
```

---

## 传输层

### 建立连接：io模块

| 类 | 上层使用者 | 使用方式 |
|---|---|---|
| `CmsConnection` | `CmsSession`、`KeepAliveManager`、`CmsServerTransport` | `send()`、`close()`、`isConnected()` |
| `CmsTransportListener` | `CmsServer`、`CmsClient` | 实现接口，接收回调 |
| `CmsServerTransport` | 仅 `CmsServer` | 构造 → 配置 → start/stop |
| `CmsClientTransport` | 仅 `CmsClient` | 构造 → 配置 → connect |

上层完全不需要关心：Socket 创建、TCP 读写、TLS 握手、线程管理、粘包处理。这些全部被 `io` 包封装在内部。

### 会话管理：session模块

| 类 | 职责 |
|---|---|
| `CmsSession` | 基类：持有 `CmsConnection`、`associationId`、`sessionState`，提供 `send()` |
| `CmsClientSession` | 客户端会话：`PendingRequest` 管理、响应分发、`nextReqId()` |
| `CmsServerSession` | 服务端会话：`AccessPoint` 信息、`SessionListener` |
| `SessionState` | 状态枚举：`DISCONNECTED` → `CONNECTED` → `ASSOCIATED` |
| `PendingRequest` | 请求-响应匹配（`CountDownLatch` + 超时） |
| `AssociationIdGenerator` | 64 字节关联 ID 生成器 |
| `KeepAliveManager` | 心跳管理（30s 空闲检测 → 5s 间隔 Test → 最多 4 次重试） |

session 层位于 io 层之上，为上层（`app`、`protocol`）屏蔽了连接状态管理、请求-响应匹配、心跳维护等会话级逻辑。

---

## TLS 安全传输

本项目支持 TLS/国密 SSL 加密传输，基于 BouncyCastle 实现。

### 依赖

```xml
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk18on</artifactId>
    <version>1.78</version>
</dependency>
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcpkix-jdk18on</artifactId>
    <version>1.78</version>
</dependency>
```

### 生成测试证书

运行 PowerShell 脚本自动生成测试证书：

```powershell
.\Generate-Test-Certs.ps1
```

生成的文件位于 `src/main/resources/certs/`：

| 文件 | 用途 |
|------|------|
| `ca.pfx` | CA 根证书（含私钥） |
| `ca.cer` | CA 根证书（仅公钥，用于分发） |
| `server.pfx` | 服务端证书（含私钥+CA链） |
| `client.pfx` | 客户端证书（含私钥+CA链） |

**密码**: `changeit`

### 使用示例

```java
import com.ysh.dlt2811bean.security.GmSslContext;

// 服务端
GmSslContext serverCtx = GmSslContext.forServer()
    .keyStore("certs/server.pfx", "changeit")
    .trustCertificate("certs/ca.cer")
    .build();

// 客户端
GmSslContext clientCtx = GmSslContext.forClient()
    .keyStore("certs/client.pfx", "changeit")
    .trustCertificate("certs/ca.cer")
    .build();

// 建立 TLS 连接
CmsClientTransport client = new CmsClientTransport();
client.sslContext(clientCtx).connectTls("localhost", 8888, new CmsTransportListener() {
    @Override
    public void onConnected(CmsConnection conn) { /* ... */ }
    @Override
    public void onApduReceived(CmsConnection conn, CmsApdu apdu) { /* ... */ }
    @Override
    public void onDisconnected(CmsConnection conn) { /* ... */ }
    @Override
    public void onError(CmsConnection conn, Exception e) { /* ... */ }
});
```

### 生成国密证书（生产环境）

如需生成真正的国密证书，需安装 [GmSSL](https://www.gmssl.org/) 工具，然后修改脚本使用国密算法：

```bash
# 生成 SM2 私钥
gmssl genpkey -algorithm sm2 -out ca.key

# 生成自签名证书
gmssl req -new -x509 -key ca.key -out ca.cer

# 转换为 PKCS12
openssl pkcs12 -export -in ca.cer -inkey ca.key -out ca.pfx
```

### 支持的加密套件

- `ECDHE_SM4_SM3` - 动态加密套件，密钥交换使用 SM2 ECDHE
- `ECC_SM4_SM3` - 静态加密套件，密钥交换使用 SM2 ECC






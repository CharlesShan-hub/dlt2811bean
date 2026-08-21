# jcms-node — §6 传输层节点封装

## 职责

jcms-node 是 DL/T 2811 标准第 6 章（传输层）的 Java 实现，将底层的连接管理、帧收发、会话维护等基础设施，封装为一个**具备客户端/服务器双重能力的节点（CmsNode）**。

每个设备可以同时是服务器（接受其他设备的连接）和客户端（主动连接其他设备），通过 handler 动态加载实现业务逻辑的低耦合。

一句话概括：**CMS 协议传输层的一站式节点封装**。

## 设计理念

```
┌──────────────────────────────────────────────────┐
│                    CmsNode                        │
│  ┌──────────────┐      ┌──────────────────────┐  │
│  │  InnerServer  │      │    InnerClient       │  │
│  │  (被动接受)   │      │    (主动连接)         │  │
│  │               │      │                      │  │
│  │  ServerAcceptor│      │  ClientConnector    │  │
│  │  Dispatcher   │      │  ClientSession      │  │
│  │  ServerSession│      │  sendRequest()       │  │
│  │  KeepAlive    │      │  ReportHandler       │  │
│  └──────┬───────┘      └──────────┬───────────┘  │
│         │                         │               │
│         └──────────┬──────────────┘               │
│                    ▼                              │
│         ┌────────────────────┐                    │
│         │    SclManager      │  ← SCL 文件管理    │
│         │  ContentManager    │  ← 目录/数据缓存   │
│         │  GmCredentialManager│ ← 国密凭证       │
│         └────────────────────┘                    │
└──────────────────────────────────────────────────┘
```

### 关键设计决策

1. **双重角色**：一个 `CmsNode` 同时包含 `InnerServer` 和 `InnerClient`，让设备既可作为服务端接受连接，也可作为客户端发起连接。对应实际变电站场景中 IED 既是数据提供者也是数据消费者。

2. **Handler 动态加载**：业务逻辑以 `ServiceHandler`（服务端）和 `BaseClientHandler`（客户端）的形式动态注册到 Node 上，降低传输层与业务层的耦合。

3. **共享基础设施**：`SclManager`、`ContentManager`、`GmCredentialManager` 由 Node 统一管理，server/client 共享。

---

## 类详解

### 1. `CmsNode` — 节点主类

包路径：`com.ysh.jcms.app.node.CmsNode`

#### 构造

```java
// 创建带服务端的节点（从配置读取端口）
CmsNode node = new CmsNode(true);

// 创建带服务端的节点（指定端口）
CmsNode node = new CmsNode(8102);

// 创建纯客户端节点（无服务端）
CmsNode node = new CmsNode(false);
```

#### 服务端管理

```java
// 注册服务端 Handler
node.registerServer(new GetDataValuesHandler());
node.registerServer(new SetDataValuesHandler());

// 启动服务端（加载 SCL + 启动监听）
node.start(true);   // test 模式：加载 testSclFiles
node.start(false);  // 生产模式：加载 sclFiles
```

服务端启动流程：
```
start(test)
  → CmsConfigLoader → server.{testSclFiles|sclFiles}
  → SclManager.load(file) → SclDocument
  → InnerServer.setScl2Document(doc)
  → ReportEngine.init(doc)
  → InnerServer.start()
      → ServerAcceptor.start()
      → KeepAliveManager.start()
```

#### 客户端管理

```java
// 注册客户端 Handler
node.registerClient(new AssociateClient());

// 执行客户端操作（反射调用 Handler.execute()）
CmsAssociateResponse resp = node.execute(AssociateClient.class, dao);

// 底层连接
node.connect("127.0.0.1", 8102);
node.connectTls("127.0.0.1", 9102, sslContext);

// 直接发帧
Frame resp = node.sendRequest(ServiceName.GET_DATA_VALUES, asduBytes);
```

#### 生命周期

```java
node.start(true);   // 启动
// ... 运行中 ...
node.stop();        // 停止（停止服务端 + 关闭客户端连接）
```

---

### 2. `InnerServer` — 服务端

包路径：`com.ysh.jcms.app.node.InnerServer`

#### 架构

```
InnerServer
├── ServerAcceptor (TCP)       ← 明文端口监听
├── ServerAcceptor (TLS)       ← 加密端口监听（可选，自签名证书）
├── Dispatcher                 ← 服务分发
├── ServerSession 列表         ← 已连接的会话
├── SclDocument                ← SCL 配置（每个会话共享引用）
└── KeepAliveManager           ← 心跳检测
```

#### 初始化

```java
// 从配置读取端口（application.yaml → server.port / server.sslPort）
InnerServer server = new InnerServer();

// 或手动指定
InnerServer server = new InnerServer(8102, 9102);
```

TLS 模式使用**自签名 RSA 证书**（运行时生成），trust-all TrustManager。适合开发和测试环境。

#### 帧处理流程

```
onFrameReceived(conn, frame)
    │
    ├─ findSession(conn) → ServerSession
    ├─ session.touchActivity()  (更新最后活动时间)
    ├─ dispatcher.dispatch(session, frame)
    │    │
    │    ├─ HANDLED → connection.send(response)
    │    ├─ NOT_REGISTERED → 发错误响应帧
    │    └─ ERROR_OCCURRED → 日志
    │
    └─ (ReportEngine 等异步推送由 handler 内部触发)
```

#### `ServerSession` — 服务端会话

内部类，继承 `Session`，扩展：

| 属性 | 说明 |
|------|------|
| `scl2Document` | SCL 文档（各会话可独立持有引用） |
| `sclAccessPoint` | 关联的访问点 |
| `sclDataTypeTemplates` | 数据类型模板快照 |
| `lastActivityTime` | 最后活动时间（保活用） |
| `keepaliveRetries` | 保活重试计数 |

`clear()` 额外清理 `SgSessionState`（定值组会话状态）。

---

### 3. `InnerClient` — 客户端

包路径：`com.ysh.jcms.app.node.InnerClient`

纯发送器——只管发请求、收响应，所有业务逻辑（解码、会话更新）由调用方负责。

#### 连接管理

```java
InnerClient client = new InnerClient();
client.connect("127.0.0.1", 8102);           // TCP
client.connectTls("127.0.0.1", 9102, ctx);   // TLS
```

#### 请求/响应

```java
// 同步发送等待响应
Frame resp = client.sendRequest(ServiceName.GET_DATA_VALUES, asduBytes);
Frame resp = client.sendRequest(ServiceName.GET_DATA_VALUES, asduBytes, 10000); // 自定义超时
```

流程：
```
sendRequest(sc, asdu)
    │
    ├─ extractReqId(asdu) → reqId
    ├─ session.addPendingRequest(reqId, timeout)
    ├─ connection.send(frame)
    └─ session.waitForPendingRequest(reqId, timeout) → Frame
```

#### 推送消息处理

```java
client.setReportHandler(frame -> {
    // 处理服务器推送的 REPORT
    CmsReport report = new CmsReport();
    report.decode(frame.asduBytes());
    // ...
});
```

`onFrameReceived()` 按优先级处理三种帧：
1. **待处理请求的响应** → `session.tryDispatchResponse()`
2. **服务端 TEST 探测** → 自动回复 pong
3. **服务端推送 REPORT** → 调用 `reportHandler`

---

### 4. `KeepAliveManager` — 保活管理器

包路径：`com.ysh.jcms.app.node.KeepAliveManager`

服务端心跳检测，基于配置：

```yaml
server:
  keepalive:
    idleTimeoutMs: 30000    # 空闲超时 (ms)
    retryIntervalMs: 5000   # 重试间隔 (ms)
    maxRetries: 4           # 最大重试次数
```

工作原理：
```
每 1 秒检查所有 ASSOCIATED 会话
    │
    ├─ 空闲 > idleTimeoutMs → 发送 TEST 帧（探测）
    ├─ 空闲 > idleTimeoutMs + retryInterval × retries → 重试计数 +1
    └─ 重试 > maxRetries → 断开连接
```

---

### 5. `SclManager` — SCL 文件管理器

包路径：`com.ysh.jcms.app.node.SclManager`

```java
SclManager mgr = new SclManager();
mgr.load("config/sample-scd-full.scd");    // 文件系统
mgr.load("scd/sample.scd");               // classpath
SclDocument doc = mgr.getDocument();
```

支持从文件系统或 classpath 加载 SCL/SCD 文件。资源搜索策略：绝对路径 → classpath → 相对路径。

---

### 6. `ContentManager` — 目录/数据缓存

包路径：`com.ysh.jcms.app.node.ContentManager`

内存缓存，存储目录服务和数据服务的查询结果，避免重复解析 SCL。

```java
ContentManager cm = new ContentManager();
cm.initServerDir(Arrays.asList("LD0", "PROT", "RCD"));
cm.initAllData(allDataEntries);
cm.initDataDef(dataDefEntries);
cm.initNodeDir(acsiClass, refs);
```

| 方法 | 缓存内容 |
|------|----------|
| `initServerDir()` / `getLdNames()` | 逻辑设备列表 |
| `initLdDir()` / `getLnNames()` | 逻辑节点列表 |
| `initDataRefs()` / `getDataRefs()` | 数据引用列表 |
| `initDataSets()` / `getDataSetRefs()` | 数据集引用列表 |
| `initNodeDir(acsiClass)` / `getNodeRefs()` | ACSI 类下的引用 |
| `initAllData()` / `getAllDataEntries()` | 全部数据值 |
| `initDataDef()` / `getDataDefEntries()` | 全部数据定义 |

---

## 与 handler 模块的关系

CmsNode 不直接包含业务逻辑，而是通过 handler 接口实现职责分离：

```
node.registerServer(handler)        → Dispatcher.register(handler)
node.registerClient(handler)        → clientHandlers[class] = handler

服务端 Handler (ServiceHandler):
  handler.handleRequest(session, request) → 响应帧

客户端 Handler (BaseClientHandler):
  handler.execute(...) → 调用 node.sendRequest() 发起请求 → 解码响应 → 返回结果
```

典型的 handler 模块在 `jcms-app/handler` 下，包括：
- `AssociateClient` / `AssociateServer` — 关联
- `GetDataValuesHandler` — 读数据值
- `SetDataValuesHandler` — 写数据值
- `ReportEngine` — 报告推送
- 等等

---

## 与底层模块的关系

| 模块 | 使用方式 |
|------|---------|
| **jcms-utils/transport** | `InnerServer` → `ServerAcceptor` + `Dispatcher`；`InnerClient` → `ClientConnector` + `ClientSession` |
| **jcms-utils/scl** | `SclManager` 通过 `SclReader` 加载 SCL 文件并运行一致性检查 |
| **jcms-utils/config** | 从 `CmsConfigLoader` 读取端口、保活、SCL 路径等配置 |
| **jcms-utils/security** | 初始化 `GmCredentialManager` 管理国密/TLS 凭证 |
| **jcms-core/data/svc** | Handler 中使用 data/svc PDU 类型编解码 ASDU |

## 快速使用

### 方式一：CmsServer（服务端）
```java
// 创建预置所有服务的服务端节点
CmsServer server = new CmsServer();
server.start(true);  // 启动，加载 testSclFiles
```

### 方式二：CmsClient（客户端）
```java
// 创建预置所有客户端 Handler 的节点
CmsClient client = new CmsClient();
// 或指定 SCL 文件
CmsClient client = new CmsClient("config/sample.scd");

// 连接
client.connect("127.0.0.1", 8102);

// 执行操作（DAO 自动定位 Handler）
AssociateDao dao = new AssociateDao("C_B5041X", "S1");
client.execute(dao);
```

### 方式三：CmsNode（自定义）
```java
// 创建自定义节点
CmsNode node = new CmsNode(true);

// 注册自定义 Handler
node.registerServer(new MyCustomHandler());
node.registerClient(new MyCustomClient());

// 启动
node.start(false);  // 加载生产配置
```

# jcms-transport — 传输层基础构建

## 职责

jcms-transport 提供 CMS 协议传输层的核心抽象，涵盖 **TCP/TLS 连接管理**、**协议帧编解码**、**会话状态管理**和**服务分发**。

它是 jcms-app 中 Server/Client 的基础积木——不包含具体的业务逻辑，只提供传输层骨架。

一句话概括：**CMS 协议的 TCP 传输层基础设施**。

## 架构概览

```
┌─────────────────────────────────────────────────────────┐
│                   应用层 (jcms-app)                       │
├─────────────────────────────────────────────────────────┤
│                   transport 模块                         │
│                                                          │
│  ┌─────────────────────────────────────────────────┐   │
│  │ wire          — 网络连接层                        │   │
│  │  ServerAcceptor   (TCP/TLS 服务端监听)            │   │
│  │  ClientConnector  (TCP/TLS 客户端连接)            │   │
│  │  Connection       (单连接管理 + 读线程)            │   │
│  │  ConnectionListener (连接事件回调接口)             │   │
│  └─────────────────────────────────────────────────┘   │
│                          │                              │
│  ┌─────────────────────────────────────────────────┐   │
│  ▼ frame          — 协议帧层                         │   │
│  │  FrameHeader  (4 字节协议头)                       │   │
│  │  Frame        (Header + ASDU + ReqId)             │   │
│  │  FrameCodec   (encode/split/merge)                │   │
│  │  FrameAssembler (分片帧组装，带内存预算)            │   │
│  └─────────────────────────────────────────────────┘   │
│                          │                              │
│  ┌─────────────────────────────────────────────────┐   │
│  ▼ session       — 会话层                           │   │
│  │  Session         (抽象基类，state() 驱动清理)       │   │
│  │  ClientSession   (客户端：ReqID + 等待响应)        │   │
│  │  SessionState    (生命周期：DISCONNECTED→ASSOCIATED)│   │
│  │  PendingRequest  (待处理请求，支持超时等待)         │   │
│  │  AssociationIdGenerator (关联ID 生成器)            │   │
│  └─────────────────────────────────────────────────┘   │
│                          │                              │
│  ┌─────────────────────────────────────────────────┐   │
│  ▼ service       — 服务分发层                        │   │
│  │  Dispatcher      (CmsServiceInfo→Handler 路由)     │   │
│  │  ServiceHandler  (Handler 接口)                    │   │
│  │  DispatchResult  (分发结果枚举)                    │   │
│  └─────────────────────────────────────────────────┘   │
│                                                          │
│  服务代码定义统一收敛到 jcms-core 的 CmsServiceInfo        │
└─────────────────────────────────────────────────────────┘
```

依赖方向：`wire → frame ← session ← service`，单向无环。

## 子包详解

### 1. `wire` — 网络连接层

#### `Connection`

包路径：`com.ysh.jcms.utils.transport.wire.Connection`

单个 TCP/TLS 连接的抽象。关键设计：

- **拥有 Socket** + `DataInputStream`/`DataOutputStream`
- **后台读线程**：启动一个 daemon 线程 `readLoop()`，持续从网络读帧，通过 `FrameAssembler` 组装完整帧后回调 `ConnectionListener`
- **接收**：`readFrame()` 直接解出 4 字节 `FrameHeader` + ReqID + ASDU，构造 `Frame`（不再走整包重解码）
- **发送**：`send(Frame)` 按 `peerAsduSize` 自动分片；若对端不支持分帧（`fragmentationSupported=false`）且 ASDU 超限，直接抛异常
- **协商参数**（协商服务写入，单一事实来源）：
  - `maxFrameSize` — 接收端 FL 上限（默认 65535）
  - `peerAsduSize` — 对端 ASDU 分片上限（默认 65529）
  - `fragmentationSupported` — 对端是否支持分帧
- **关闭**：`close()` 设置 `running=false` 并关闭 socket，读线程自动退出并回调 `onDisconnected`

```
接收: [FL:2][APCH:4][ReqID:2][Data:FL-6] → FrameAssembler → 完整帧 → listener.onFrameReceived()
发送: Frame → FrameCodec.split() → 分片 → write()
```

#### `ServerAcceptor`

包路径：`com.ysh.jcms.utils.transport.wire.ServerAcceptor`

服务端监听器：

```java
ServerAcceptor acceptor = new ServerAcceptor(port, listener)
    .sslContext(sslContext)      // 可选：启用 TLS
    .needClientAuth(true);       // 可选：要求客户端证书
acceptor.start();               // 启动监听线程
```

- 支持 TCP 和 TLS（通过 `SSLContext`）两种模式
- 每个接受连接自动创建 `Connection` + 启动读线程
- 使用 `CopyOnWriteArrayList` 管理活跃连接，连接断开时自动从清单移除

#### `ClientConnector`

包路径：`com.ysh.jcms.utils.transport.wire.ClientConnector`

客户端连接器：

```java
ClientConnector connector = new ClientConnector().connectTimeout(5000);
Connection conn = connector.connect(host, port, listener);        // TCP
Connection conn = connector.connectTls(host, port, listener, sslCtx);  // TLS
```

#### `ConnectionListener`

连接事件回调接口：

```java
public interface ConnectionListener {
    void onConnected(Connection conn);
    void onDisconnected(Connection conn);
    void onFrameReceived(Connection conn, Frame frame);
    void onError(Connection conn, Exception e);
}
```

---

### 2. `frame` — 协议帧层

#### Wire Format

```
[FL:2][APCH:4][ReqID:2][Data:FL-6]
│       │      │        └── 应用层数据单元（ASDU）
│       │      └─────────── 请求 ID（小端）
│       └────────────────── 协议头（APCH，4 字节）
└────────────────────────── 帧长度（Frame Length，大端，含 APCH+ReqID+Data）
```

- FL 字段为 16 位，最大 65535；单帧 ASDU（不含 ReqID）上限 65529
- APCH 即 `FrameHeader`，4 字节：CC + SC + FL-hi + FL-lo

#### `FrameHeader` — 4 字节协议头

```
CC 字节:
  bit7: Next   (0=最后一帧, 1=还有后续分片)
  bit6: Resp   (0=请求, 1=响应)
  bit5: Err    (0=肯定, 1=否定/错误)
  bit4: 保留
  bit3~0: PI   (协议标识, 固定 0x01)
```

| 字段 | 偏移 | 大小 | 说明 |
|------|------|------|------|
| CC | 0 | 1B | 控制字节（Next + Resp + Err + PI） |
| SC | 1 | 1B | 服务代码，对应 `CmsServiceInfo` 枚举 |
| FL | 2-3 | 2B | 帧长度（APCH + ReqID + Data 总长） |

#### `Frame` — 协议帧

包路径：`com.ysh.jcms.utils.transport.frame.Frame`

```java
public class Frame {
    FrameHeader header;      // 4 字节协议头
    byte[] asduBytes;        // ASDU 负载（不含 ReqID）
    int reqId;               // 请求 ID
}
```

#### `FrameCodec` — 帧编解码工具

无状态静态工具类：

| 方法 | 说明 |
|------|------|
| `encode(Frame)` | 帧 → 完整线网字节（含 FL 前缀） |
| `split(Frame, maxPayload)` | 大帧分片（Next 位标记） |
| `merge(List<Frame>)` | 分片重组为完整帧 |

> 解码不再走整包重解析：`Connection.readFrame()` 直接解 header + ReqID 构造 Frame。

#### `FrameAssembler` — 分片组装器

处理 `Next=1` 的分片帧，按 ReqID 缓存分片直到收到 `Next=0` 的最后一帧，然后合并为完整帧交付。

防滥用预算（恶意对端无法靠挂起分片耗尽内存）：
- 最多 **1024** 个挂起 ReqID
- 累计挂起 **8MB** 上限
- 超限或同一 ReqID 复用 → 抛 `FrameFormatException` 断连

---

### 3. `session` — 会话层

#### `Session` — 抽象会话基类

包路径：`com.ysh.jcms.utils.transport.session.Session`

```java
SessionState: DISCONNECTED → CONNECTED → ASSOCIATED
```

维护会话级别状态：
- `sessionId` / `connection` — 会话标识与底层连接
- `state` — 会话状态（DISCONNECTED / CONNECTED / ASSOCIATED）
- `associationId` — 关联 ID（Associate 服务分配）
- `negotiated` / `negotiatedApduSize` — 协商结果

**状态驱动清理**：`state(SessionState)` 是唯一状态入口，手动（release/abort）与被动（TCP 断开）切换都走这里，按 旧→新 状态对自动派发清理：

- **Hook 1 `clearAssociation()`** — 离开 `ASSOCIATED` 时清理关联级状态；子类可覆写补充业务状态
- **Hook 2 `clearConnection()`** — 进入 `DISCONNECTED` 时做完整拆卸（含关闭连接）；幂等

#### `ClientSession` — 客户端会话

包路径：`com.ysh.jcms.utils.transport.session.ClientSession`

在 `Session` 基础上增加：
- **ReqID 生成**：`nextReqId()` 自动递增（1..65535 循环）
- **待处理请求表**：`ConcurrentHashMap<Integer, PendingRequest>`
- **请求/响应匹配**：
  - `send → addPendingRequest(reqId)` 注册等待
  - `receive → tryDispatchResponse(frame)` 匹配响应
  - `waitForPendingRequest(reqId, timeout)` 阻塞等待
- 覆写 `clearConnection()`：断开时唤醒所有 pending 等待者并清空表，防止 map 无限增长

#### `PendingRequest` — 待处理请求

支持超时等待的请求-响应同步原语，内部使用 `synchronized + wait/notify` 实现阻塞等待，`setResult` 唤醒等待线程。

#### `SessionState` — 会话状态枚举

```java
DISCONNECTED → CONNECTED → ASSOCIATED
```

---

### 4. `service` — 服务分发层

#### `Dispatcher` — 服务分发器

包路径：`com.ysh.jcms.utils.transport.service.Dispatcher`

```java
Dispatcher dispatcher = new Dispatcher();
dispatcher.register(new GetDataValuesHandler());  // 注册 Handler

// 收到请求帧后分发
DispatchOutcome outcome = dispatcher.dispatch(session, request);
// outcome.result → HANDLED / NOT_REGISTERED / ERROR_OCCURRED
// outcome.response → 响应帧
```

通过 `CmsServiceInfo`（服务代码）查找已注册的 `ServiceHandler`，调用 `handleRequest()` 获取响应。未知服务码（含解码出的 `null`）统一返回 `NOT_REGISTERED`。

#### `ServiceHandler` — 处理器接口

```java
public interface ServiceHandler {
    CmsServiceInfo getServiceName();
    Frame handleRequest(Session session, Frame request);
}
```

#### `DispatchResult` — 分发结果枚举

```java
HANDLED           — 已处理，outcome.response 为响应帧
NOT_REGISTERED    — 无对应 Handler
ERROR_OCCURRED    — Handler 抛出异常
```

---

### 5. 服务代码枚举

线网层的服务代码定义已统一收敛到 `com.ysh.jcms.core.info.CmsServiceInfo`（jcms-core 的 info 包），不再在 transport 层维护第二份：

- 常量名即协议服务名（如 `ASSOCIATE`、`GET_DATA_VALUES`）
- `byCode(int)` / `byName(String)` 反向查找；未分配服务码的未确认服务（GOOSE/SV，code=0）不参与 byCode 查找
- 完整服务与码值清单见 `jcms-info.md`

---

## 数据流

### 服务端接收

```
Socket.accept()
    │
    ▼
Connection(读线程) → readFrame()
    │                    │
    │              FrameAssembler.addSegment()
    │                    │
    │             完整帧? ──否→ 等待下一分片
    │                    │
    │                    ▼ 是
    │              listener.onFrameReceived(conn, frame)
    │                    │
    ▼                    ▼
Dispatcher.dispatch(session, request)
    │
    ├─ ServiceHandler.handleRequest() → 响应帧
    └─ Connection.send(response) → 分片→写入 socket
```

### 客户端发送/接收

```
ClientSession.nextReqId() → reqId
    │
    ├─ addPendingRequest(reqId)
    ├─ Connection.send(request) → 写入 socket
    │
    ▼ 等待响应
waitForPendingRequest(reqId, timeout)
    │
    ▼ 收到响应
tryDispatchResponse(frame) → PendingRequest.setResult()
    │
    ▼
调用方拿到结果 → 继续执行
```

---

## 与上层模块的关系

| 模块 | 使用方式 |
|------|---------|
| **jcms-app/server** | `ServerAcceptor` 监听端口，`Dispatcher` 分发请求到各 Handler |
| **jcms-app/client** | `ClientConnector` 建立连接，`ClientSession` 管理请求/响应 |
| **jcms-app/handler** | 各 Handler 实现 `ServiceHandler` 接口，注册到 `Dispatcher` |
| **jcms-core/info** | `CmsServiceInfo` 提供服务代码枚举（transport 引用，无本地重复定义） |
| **jcms-utils/security** | `GmSslContext` 构建的 `SSLContext` 注入 `ServerAcceptor` / `ClientConnector` |

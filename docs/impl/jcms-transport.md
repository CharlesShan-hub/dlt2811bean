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
│  │  FrameCodec   (编解码 + 分段/重组)                 │   │
│  │  FrameAssembler (分片帧组装)                       │   │
│  └─────────────────────────────────────────────────┘   │
│                          │                              │
│  ┌─────────────────────────────────────────────────┐   │
│  ▼ session       — 会话层                           │   │
│  │  Session         (抽象基类)                        │   │
│  │  ClientSession   (客户端：ReqID + 等待响应)        │   │
│  │  SessionState    (生命周期：DISCONNECTED→ASSOCIATED)│   │
│  │  PendingRequest  (待处理请求，支持超时等待)         │   │
│  │  AssociationIdGenerator (关联ID 生成器)            │   │
│  └─────────────────────────────────────────────────┘   │
│                          │                              │
│  ┌─────────────────────────────────────────────────┐   │
│  ▼ service       — 服务分发层                        │   │
│  │  Dispatcher      (服务名→Handler 路由)             │   │
│  │  ServiceHandler  (Handler 接口)                    │   │
│  │  DispatchResult  (分发结果枚举)                    │   │
│  └─────────────────────────────────────────────────┘   │
│                                                          │
│  ServiceName  — 服务代码枚举 (线网协议值)                 │
└─────────────────────────────────────────────────────────┘
```

## 子包详解

### 1. `wire` — 网络连接层

#### `Connection`

包路径：`com.ysh.jcms.utils.transport.wire.Connection`

单个 TCP/TLS 连接的抽象。关键设计：

- **拥有 Socket** + `DataInputStream`/`DataOutputStream`
- **后台读线程**：启动一个 daemon 线程 `readLoop()`，持续从网络读帧，通过 `FrameAssembler` 组装完整帧后回调 `ConnectionListener`
- **发送**：`send(Frame)` 自动调用 `FrameCodec.split()` 分片（当 ASDU 超过 `maxFrameSize` 时）
- **关闭**：`close()` 设置 `running=false` 并关闭 socket，读线程自动退出并回调 `onDisconnected`

```
接收: [FL:2][Header:4][ASDU:FL-4] → FrameAssembler → 完整帧 → listener.onFrameReceived()
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
- 使用 `CopyOnWriteArrayList` 管理活跃连接，线程安全

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
[FL:2][CC:1][SC:1][FL-hi:1][FL-lo:1][ASDU:FL-4]
│      │      │      │         │         └── 应用层数据单元
│      │      │      └─────────┴────────────── 帧长度（大端）
│      │      └──────────────────────────────── 服务代码（Service Code）
│      └────────────────────────────────────── 控制字节（Control Code）
└───────────────────────────────────────────── 帧长度（Frame Length）
```

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
| SC | 1 | 1B | 服务代码，对应 `ServiceName` 枚举 |
| FL | 2-3 | 2B | 帧长度（Header + ASDU 总长） |

#### `Frame` — 协议帧

包路径：`com.ysh.jcms.utils.transport.frame.Frame`

```java
public class Frame {
    FrameHeader header;      // 4 字节协议头
    byte[] asduBytes;        // ASDU 负载
    int reqId;               // 从 ASDU 前 2 字节提取的请求 ID
}
```

#### `FrameCodec` — 帧编解码器

无状态静态工具类：

| 方法 | 说明 |
|------|------|
| `encode(Frame)` | 帧 → 完整线网字节（含 FL 前缀） |
| `decode(byte[], offset)` | 线网字节 → Frame |
| `split(Frame, maxPayload)` | 大帧分片（Next 位标记） |
| `merge(List<Frame>)` | 分片重组为完整帧 |

#### `FrameAssembler` — 分片组装器

处理 `Next=1` 的分片帧，缓存分片直到收到 `Next=0` 的最后一帧，然后合并为完整帧交付。

---

### 3. `session` — 会话层

#### `Session` — 抽象会话基类

包路径：`com.ysh.jcms.utils.transport.session.Session`

```java
SessionState: DISCONNECTED → CONNECTED → ASSOCIATED ⇄ RELEASING
```

维护会话级别状态：
- `sessionId` / `connection` — 会话标识与底层连接
- `state` — 会话状态（DISCONNECTED / CONNECTED / ASSOCIATED / RELEASING）
- `associationId` — 关联 ID（Associate 服务分配）
- 协商参数：`negotiatedApduSize`、`peerAsduSize`、`peerProtocolVersion`
- `clear()` — 清理关联状态（含 `RcbStateManager.clear()`）

#### `ClientSession` — 客户端会话

包路径：`com.ysh.jcms.utils.transport.session.ClientSession`

在 `Session` 基础上增加：
- **ReqID 生成**：`nextReqId()` 自动递增（1..65535 循环）
- **待处理请求表**：`ConcurrentHashMap<Integer, PendingRequest>`
- **请求/响应匹配**：
  - `send → addPendingRequest(reqId)` 注册等待
  - `receive → tryDispatchResponse(frame)` 匹配响应
  - `waitForPendingRequest(reqId, timeout)` 阻塞等待

#### `PendingRequest` — 待处理请求

支持超时等待的请求-响应同步原语，内部使用 `CountDownLatch` 或 `synchronized + wait/notify` 实现阻塞等待。

#### `SessionState` — 会话状态枚举

```java
DISCONNECTED → CONNECTED → ASSOCIATED
                 ↓             ↓
              (断开)        RELEASING → DISCONNECTED
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

通过 `ServiceName`（服务代码）查找已注册的 `ServiceHandler`，调用 `handleRequest()` 获取响应。

#### `ServiceHandler` — 处理器接口

```java
public interface ServiceHandler {
    ServiceName getServiceName();
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

### 5. `ServiceName` — 服务代码枚举

包路径：`com.ysh.jcms.utils.transport.ServiceName`

线网协议层的服务代码定义（区别于 `CmsServiceInfo`——那是信息层的元数据枚举），涵盖所有 CMS 服务：

| 服务 | 代码 | 说明 |
|------|------|------|
| ASSOCIATE | 0x01 | 关联 |
| ABORT | 0x02 | 中止 |
| RELEASE | 0x03 | 释放 |
| GET_SERVER_DIRECTORY ~ GET_ALL_CB_VALUES | 0x50~0x9C | 目录服务 |
| GET_DATA_VALUES ~ GET_DATA_DEFINITION | 0x30~0x33 | 数据访问 |
| GET_DATA_SET_VALUES ~ GET_DATA_SET_DIRECTORY | 0x3A~0x39 | 数据集 |
| SELECT_ACTIVE_SG ~ GET_SGCB_VALUES | 0x54~0x59 | 定值组 |
| REPORT ~ SET_URCB_VALUES | 0x5A~0x5E | 报告 |
| GET_LCB_VALUES ~ GET_LOG_STATUS_VALUES | 0x5F~0x63 | 日志 |
| SELECT ~ COMMAND_TERMINATION | 0x70~0x76 | 控制 |
| GET_FILE ~ GET_FILE_ATTRIBUTE_VALUES | 0x7A~0x7E | 文件 |
| TEST | 0x80 | 测试 |
| ASSOCIATE_NEGOTIATE | 0x90 | 协商 |
| GET_RPC_INTERFACE_DIRECTORY ~ RPC_CALL | 0xA0~0xA4 | RPC |

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
| **jcms-svc** | Handler 中使用 svc PDU 类型编解码请求/响应帧的 ASDU |
| **jcms-utils/security** | `GmSslContext` 构建的 `SSLContext` 注入 `ServerAcceptor` / `ClientConnector` |

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

**标准支撑**：每一层都有 DL/T 2811 条款背书——wire（6.5 分帧 / 6.6 端口 / 6.7 连接控制 / 6.9 超时与检测）、frame（6.1 APDU / 6.3 空帧 / 6.5 分帧）、session（6.2 服务数据单元 / 6.9.1 超时 / 8.2 关联）、service（8.x 各服务分发）。

## 子包详解

### 1. `wire` — 网络连接层

#### `Connection`

包路径：`com.ysh.jcms.utils.transport.wire.Connection`

单个 TCP/TLS 连接的抽象。核心职责：

- **拥有 Socket + 读写流**，启动时配置 `TCP_NODELAY`（低延迟）和 `KEEPALIVE`（心跳保活）
- **后台读线程**：`startReader()` 启动 daemon 线程持续读帧，`FrameAssembler` 组装后回调 `ConnectionListener`
- **接收逻辑**：`readFrame()` 解 4 字节头 + ReqID + Data → 构造 `Frame`；PI 错误帧丢弃计数，连续 5 次触发断连
- **发送逻辑**：`send(Frame)` 自动分片（按 `peerAsduSize`），不支持分帧时 ASDU 超限直接抛异常
- **协商参数**（由协商服务写入，单一事实来源）：`maxFrameSize`（接收端上限）、`peerAsduSize`（对端发送上限）、`fragmentationSupported`（分帧能力）
- **关闭**：`close()` 置 `running=false` 并关闭 socket，读线程退出后自动触发 `onDisconnected` 回调

```
接收: [APCH:4][ReqID:2][Data:FL-2] → FrameAssembler → 完整帧 → listener.onFrameReceived()
发送: Frame → FrameCodec.split() → 分片 → write()
```

#### `ServerAcceptor`

包路径：`com.ysh.jcms.utils.transport.wire.ServerAcceptor`

服务端监听器，提供 TCP/TLS 服务端口接入能力：

- 支持 TCP 和 TLS（通过 `SSLContext`），TLS 模式可配置客户端证书验证
- 独立 acceptor 线程循环接受连接，每个连接封装为 `Connection` 并启动读线程
- **同 IP 单连接策略**（标准 6.7）：新连接到达时自动关闭同一 IP 的旧连接
- `CopyOnWriteArrayList` 管理活跃连接，断连时通过 `onClosed` 回调自动清理

#### `ClientConnector`

包路径：`com.ysh.jcms.utils.transport.wire.ClientConnector`

客户端连接器，支持 TCP 和 TLS 两种出站连接：

- `connect()` — 建立普通 TCP 连接，可配置连接超时
- `connectTls()` — 建立 TLS 连接，握手阶段设置超时避免阻塞

#### `ConnectionListener`

连接事件回调接口，定义四个核心事件：

| 回调 | 触发时机 |
|------|----------|
| `onConnected` | TCP 连接建立完成 |
| `onFrameReceived` | 完整帧接收完成（经 FrameAssembler 组装） |
| `onDisconnected` | 连接关闭（正常或异常） |
| `onError` | I/O 异常发生 |

---

### 2. `frame` — 协议帧层

#### 线网格式

```
[APCH:4][ReqID:2][Data:FL-2]
│         │      └── 应用层数据单元（Data）
│         └───────── 请求 ID（16 位小端）
└─────────────────── 协议头 APCH = CC + SC + FL
```

- FL 为不含 APCH 的 ASDU 长度（ReqID + Data），上限 65531（标准 6.1.2 c）
- Test 帧（仅 APCH）FL=0；空数据帧 FL=2（标准 6.3）

#### `FrameHeader` — 4 字节协议头

| 字节 | 位域 | 含义 |
|------|------|------|
| CC[0] | bit7 | Next 分片标记（0=最后 / 1=继续） |
| CC[0] | bit6 | Resp 响应标记（0=请求 / 1=响应） |
| CC[0] | bit5 | Err 错误标记（0=肯定 / 1=否定） |
| CC[0] | bit3~0 | PI 协议标识（固定 0x01） |
| SC[1] | — | 服务代码，映射 `CmsServiceInfo` |
| FL[2-3] | — | ASDU 长度，16 位小端 |

`encode()` — 编码前校验 `serviceCode != null`，防止静默输出非法 SC=0x00
`decode()` — 从字节数组解析 FrameHeader，SC 为 null 时仍能解码

#### `Frame` — 协议帧载体

包路径：`com.ysh.jcms.utils.transport.frame.Frame`

包含 `FrameHeader` + `asduBytes`（ASDU 负载）+ `reqId`（请求序号），`MAX_PAYLOAD_SIZE = 65529`。

#### `FrameCodec` — 无状态帧编解码

| 功能 | 说明 |
|------|------|
| `encode(Frame)` | 帧 → 线网字节；Test 帧（reqId=0 且无数据）省略 ReqID 输出 |
| `split(Frame, maxPayload)` | 超上限帧分片，同 ReqID，Next 位标记 |
| `merge(List<Frame>)` | 分片重组为完整帧 |

#### `FrameAssembler` — 有状态分片组装器

处理 Next=1 的分片帧，按 ReqID 缓存分片直到 Next=0 的终帧到达后合并交付。

**防滥用机制**：最多 1024 个挂起 ReqID，累计 8MB 上限；超限或 ReqID 复用 → 抛异常断连。

---

### 3. `session` — 会话层

#### `Session` — 抽象会话基类

包路径：`com.ysh.jcms.utils.transport.session.Session`

生命周期：`DISCONNECTED → CONNECTED → ASSOCIATED`

维护会话级状态：会话标识、底层连接、关联 ID、AP 引用、协商结果等。

**状态驱动清理**：`state(newState)` 是唯一状态入口，按旧→新状态对自动派发清理：
- **离开 ASSOCIATED** → `clearAssociation()` 清理关联级状态（可覆写补充业务状态）
- **进入 DISCONNECTED** → `clearConnection()` 完整拆卸（含关闭底层连接，幂等）

#### `ClientSession` — 客户端会话

包路径：`com.ysh.jcms.utils.transport.session.ClientSession`

扩展 `Session`，增加客户端特有能力：
- **ReqID 自动生成**：`AtomicInteger` 递增 1..65535 循环
- **待处理请求管理**：`ConcurrentHashMap<Integer, PendingRequest>` 维护等待表
- **请求-响应匹配**：`tryDispatchResponse()` 按 ReqID 匹配响应帧，`waitForPendingRequest()` 阻塞等待结果
- **断连清理**：覆写 `clearConnection()` 唤醒所有等待者并清空映射

#### `PendingRequest` — 同步等待原语

支持超时的请求-响应对，内部使用 `synchronized + wait/notify` 实现阻塞等待：
- `setResult()` 唤醒等待线程
- `waitForResult()` 阻塞至超时或收到结果

#### `AssociationIdGenerator` — 关联 ID 生成器

生成 64 字节关联 ID，格式：
- Bytes 0-7：Unix 时间戳
- Bytes 8-39：UUID（MSB + LSB）
- Bytes 40-63：SecureRandom 随机数据

#### `SessionState` — 状态枚举

`DISCONNECTED | CONNECTED | ASSOCIATED`

---

### 4. `service` — 服务分发层

#### `Dispatcher` — 服务分发器

包路径：`com.ysh.jcms.utils.transport.service.Dispatcher`

服务路由核心，通过 `CmsServiceInfo` 查找已注册的 `ServiceHandler` 并分发请求。

- `register(handler)` — 注册 Handler，以 `CmsServiceInfo` 为 key
- `dispatch(session, frame)` — 按服务码路由到 Handler，返回 `DispatchOutcome`
- 未知服务码（含 null）→ `NOT_REGISTERED`；Handler 异常 → `ERROR_OCCURRED`

#### `ServiceHandler` — 处理器接口

```
getServiceName()   — 返回该 Handler 负责的服务码
handleRequest()    — 处理请求帧，返回响应帧
```

#### `DispatchResult` — 分发结果

| 结果 | 含义 |
|------|------|
| `HANDLED` | 已处理，携带响应帧 |
| `NOT_REGISTERED` | 无对应 Handler |
| `ERROR_OCCURRED` | Handler 抛出异常 |

---

### 5. 服务代码

服务代码统一定义在 `com.ysh.jcms.core.info.CmsServiceInfo`（jcms-core），transport 层直接引用，无本地重复定义：

- 常量名即协议服务名（如 `ASSOCIATE`、`GET_DATA_VALUES`）
- 支持 `byCode(int)` / `byName(String)` 反向查找
- GOOSE/SV 等未确认服务（code=0）不参与 byCode 查找
- 完整服务清单见 `jcms-info.md`

---

## 调用关系

### 服务端 — 一帧请求的完整旅程

```
┌─────────────────────────────────────────────────────────────────────────┐
│ acceptor 线程                                                            │
│  ServerAcceptor.accept()                                                 │
│    │ 同 IP 去重 (6.7)                                                   │
│    ▼                                                                    │
│  Connection(socket, listener) + startReader()                           │
│    │                                                                    │
│    │         ┌─ 读线程 ──────────────────────────────────────────┐      │
│    │         │ readFrame(): 解 APCH + ReqID + Data               │      │
│    │         │   PI 错误 → 丢弃计数 (6.1.3)                      │      │
│    │         │   FrameAssembler: Next=1 缓存 / Next=0 合并        │      │
│    │         │   → listener.onFrameReceived(conn, frame)        │      │
│    │         └────────────────────────────────────────────────────┘      │
│    ▼                                                                    │
│  Dispatcher.dispatch(session, frame)                                    │
│    ├─ HANDLED       → Handler → 响应帧                                  │
│    ├─ NOT_REGISTERED → 空错误帧 (6.2.2)                                 │
│    └─ ERROR_OCCURRED → 空错误帧                                        │
│    ▼                                                                    │
│  Connection.send(response) → FrameCodec.split → encode → write          │
└─────────────────────────────────────────────────────────────────────────┘
```

### 客户端 — 请求/响应匹配

```
┌─────────────────────────────────────────────────────────────────────────┐
│ 业务线程                                   读线程                         │
│                                                                          │
│  ClientConnector.connect() ────────────► 建立连接 + 读线程              │
│  Negotiate → Associate（协商 + 建立关联）                                  │
│                                                                          │
│  sendRequest(service, asdu, timeout)                                    │
│    ├─ nextReqId() — 1~65535 循环 (6.2.1 a)                              │
│    ├─ addPendingRequest(reqId) — 注册等待                               │
│    ├─ connection.send(request) ──────► write(socket)                     │
│    ▼                                                                    │
│  waitForPendingRequest(reqId)     readFrame() ◄── socket 响应            │
│    │ 阻塞等待                    FrameAssembler → 完整帧                 │
│    │                              tryDispatchResponse(frame)             │
│    │                              (按 ReqID 匹配响应)                    │
│    ◄────────── setResult 唤醒 ─────────┘                                │
│    ▼                                                                    │
│  拿到响应；超时返回 null (6.9.1)                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

### 关闭 — 双保险清理

```
Connection 断开（正常/异常/close()）
    │
    ▼ 读线程 finally
  listener.onDisconnected() → 业务层: Session.state(DISCONNECTED)
    │                         └─ clearConnection() 清理会话
    ▼
  onClosed 回调 → ServerAcceptor 从连接清单移除
```

---

## 性能与健壮性

### handler 线程池

读线程只做帧解码，业务 handler 在线程池执行，不阻塞读取。响应乱序由 ReqID 匹配（标准 6.2.1 b）。

### 输入缓冲与 TCP 选项

- `BufferedInputStream(64KB)` — 减少大帧系统调用次数
- `setTcpNoDelay(true)` — 低延迟小报文交互
- `setKeepAlive(true)` — 连接保活

### 安全机制

- PI 错误帧丢弃计数，连续 5 次触发断连（标准 6.1.3）
- 分片组装器 1024 ReqID / 8MB 内存预算，防 DoS
- FrameHeader 编码前校验 serviceCode 非空，fail-fast
- ClientSession 断连时清空 pending 等待表，防止无限增长

## 与上层模块的关系

| 模块 | 使用方式 |
|------|---------|
| **jcms-app/server** | `ServerAcceptor` 监听端口，`Dispatcher` 分发请求到各 Handler |
| **jcms-app/client** | `ClientConnector` 建立连接，`ClientSession` 管理请求/响应 |
| **jcms-app/handler** | 各 Handler 实现 `ServiceHandler` 接口，注册到 `Dispatcher` |
| **jcms-core/info** | `CmsServiceInfo` 提供服务代码枚举（transport 引用） |
| **jcms-utils/security** | `GmSslContext` 的 `SSLContext` 注入 TLS 连接器 |

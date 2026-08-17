# jcms-app — §10 应用层：客户端与服务端 + CLI

jcms-app 是 DL/T 2811-2024 协议的应用层模块，将底层协议封装为易用的 Java API，同时提供交互式 CLI 和 HTTP 远程控制能力。

---

## 架构总览

```
jcms-app
├── node/          # 协议核心节点
│   ├── CmsNode     # 基类：连接管理、会话、SCL 加载
│   ├── CmsClient   # 客户端 API（纯协议，无 CLI 依赖）
│   └── CmsServer   # 服务端 API（纯协议，无 CLI 依赖）
│
├── console/       # 交互式 CLI
│   ├── CmsConsole         # 接口，包含 JLine 交互循环等全部默认实现
│   ├── CmsClientConsole   # 客户端 CLI，仅注册命令处理器
│   └── CmsServerConsole   # 服务端 CLI，仅注册命令处理器
│
├── handler/       # 协议处理器（请求/响应编解码）
├── support/       # 工具类（CmsContent、分页等）
└── tool/          # 工具和示例
    ├── CmsClientDemo    # API 使用示例
    └── MockLogGenerator # 模拟日志生成器
```

### 分层设计

| 层 | 类 | 用途 | 依赖 |
|---|---|---|---|
| 协议 API | `CmsClient` / `CmsServer` | 纯协议调用，无 CLI、无 JLine | 仅 `CmsNode` |
| CLI 交互 | `CmsClientConsole` / `CmsServerConsole` | 交互式命令行 | `CmsConsole` 接口 |
| 接口 | `CmsConsole` | 包含 JLine 循环、API 服务器等全部默认逻辑 | 无 |

**关键设计**：`CmsConsole` 是接口（非抽象类），所有方法均为 `default` 实现。`CmsClientConsole` 和 `CmsServerConsole` 只需实现 `registerHandlers()` 即可，其余全部由接口继承。这样避免了 Java 单继承的限制——`CmsClientConsole` 继承 `CmsClient`，`CmsServerConsole` 继承 `CmsServer`，同时都获得 `CmsConsole` 的 CLI 能力。

---

## 快速开始

### 作为库使用（纯 API）

外部项目只需 `import com.ysh.jcms.app.node.CmsClient`，无需关心 CLI 实现：

```java
CmsClient client = new CmsClient();
client.connect("127.0.0.1", 8102);
client.execute(new AssociateDao().sapRef("C_B5041X/S1"));
// 执行数据读写...
client.execute(new ReleaseDao());
client.close();
```

完整示例见 `com.ysh.jcms.app.tool.CmsClientDemo`。

### 启动交互式 CLI

```powershell
# 客户端
just run-client

# 服务端
just run-server
```

CLI 启动后可用 `cms` 命令发送指令，详见 `cms.md` 工作区规则。

---

## API 使用指南

### 1. 创建客户端

```java
CmsClient client = new CmsClient();
```

构造时自动注册所有协议处理器。

### 2. 连接与关联

```java
// TCP 连接
client.connect("127.0.0.1", 8102);

// TLS 连接
client.connectTls("127.0.0.1", 8103, sslContext);

// 关联访问点（自动协商参数）
client.execute(new AssociateDao().sapRef("C_B5041X/S1"));
```

### 3. 执行请求

无返回值的操作：

```java
client.execute(new ReleaseDao());
```

需要读取返回值的操作（通过 `CmsContent`）：

```java
CmsContent<SvrDirDao> content = new CmsContent<>(new SvrDirDao());
client.getClient(SvrDirClient.class).executeResult(content);
// content.res() 包含响应数据
System.out.println(content.res());
```

### 4. 生命周期

```
connect → execute(associate) → 多次 execute → execute(release) → close()
```

---

## 处理器的注册机制

`CmsClient` 在构造时自动注册所有客户端处理器，通过 `BaseClientHandler<D>` 的泛型参数自动匹配 DAO 类型。`client.execute(dao)` 根据 DAO 的 class 查找对应的 handler 并执行。

处理器注册链路：

```
CmsClient 构造
  └─ registerClients()
       ├─ registerClient(new NegotiateClient())   → 匹配 NegotiateClientDao
       ├─ registerClient(new AssociateClient())   → 匹配 AssociateDao
       ├─ registerClient(new GetDataValuesClient()) → 匹配 GetDataValuesDao
       └─ ... 共 50+ 个处理器
```

---

## 接口：CmsConsole

`CmsConsole` 接口封装了 CLI 的全部行为，包括：

- **JLine 交互循环**：命令历史记录、Tab 补全、行编辑
- **自动执行**：支持 `autoExec` 配置和 `CMS_AUTO_EXEC` 环境变量
- **批处理**：分号分隔多条命令，遇错停止
- **注释支持**：`#` 和 `//` 开头的行被忽略，行内也支持
- **HTTP API 服务器**：客户端 CLI 启动时自动启动 `CliApiServer`，支持远程执行命令
- **PDU 跟踪**：`trace-pdu on` 查看编解码报文

---

## 相关文档

- [jcms-node.md](jcms-node.md) — CmsNode 基类细节
- [jcms-svc.md](jcms-svc.md) — 服务报文段封装
- [jcms-transport.md](jcms-transport.md) — 传输层实现
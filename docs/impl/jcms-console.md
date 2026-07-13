# jcms-console — 命令行交互界面

## 职责

jcms-console 提供 CMS 协议的**交互式命令行界面（CLI）**，基于 JLine 实现。用户通过终端输入命令与 CMS 服务器交互，同时支持 HTTP API 远程调用，方便脚本集成。

一句话概括：**CMS 协议的人机交互界面——终端 CLI + HTTP API 双通道**。

## 架构

```
┌─────────────────────────────────────────────────────────┐
│                  CmsConsole (CmsNode 子类)               │
│                                                          │
│  ┌──────────────┐    ┌──────────────────────────────┐   │
│  │  CmsNode     │    │  命令行循环 (JLine)            │   │
│  │  InnerClient │    │  readLine() → tokenize()      │   │
│  │  InnerServer │    │  → handler.execute()          │   │
│  └──────────────┘    └──────────────────────────────┘   │
│         │                       │                        │
│         ▼                       ▼                        │
│  ┌──────────────┐    ┌──────────────────────────────┐   │
│  │ ClientHandler │    │   ConsolePrinter             │   │
│  │ (协议交互)    │    │   输出格式化 + ANSI 颜色      │   │
│  └──────────────┘    └──────────┬───────────────────┘   │
│                                  │                        │
│  ┌───────────────────────────────▼────────────────────┐  │
│  │  CliApiServer (嵌入式 HTTP 服务器)                   │  │
│  │  POST /api/execute → executeLine()                  │  │
│  │  GET  /api/status → 连接状态                        │  │
│  │  GET  /ui/* → Vue Web UI 静态文件                    │  │
│  └────────────────────────────────────────────────────┘  │
│                                                          │
│  ┌────────────────────────────────────────────────────┐  │
│  │  CmsRemoteClient (远程 CLI 客户端)                   │  │
│  │  java CmsRemoteClient connect --ap C_B5041X/S1     │  │
│  └────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

## 类详解

### 1. `CmsConsole` — CLI 基类

包路径：`com.ysh.jcms.app.console.CmsConsole`

继承 `CmsNode`，在其基础上添加交互式命令行循环。核心职责：

- **命令注册**：`register(CommandHandler)` / `registerHandlers()`（抽象方法，子类实现）
- **命令解析**：`executeLine()` → tokenize → 参数解析 → handler.execute()
- **主循环**：`run()` → readLine → 解析 → 执行 → 循环

#### 命令解析规则

```
输入格式： 命令名 [--参数名 值]...
布尔 flag： --secure → {secure: "true"}
行内注释： # 或 // 开头的行为注释
批量执行： 用 ; 分隔多个命令
引号支持： "带空格的参数"
```

#### 输出模式

所有输出方法提供 **双模式**——文本模式和 JSON 模式，由 `--json` 参数控制：

```java
// 文本模式（默认）
outputList("Logical Devices", items, ...)
  → Logical Devices:
    [0] LD0
    [1] PROT

// JSON 模式（--json）
outputList("Logical Devices", items, ...)
  → {"success":true,"data":["LD0","PROT"]}
```

JSON 模式供 `CliApiServer` 和远程客户端使用，方便程序解析。

#### 主循环生命周期

```
run()
  ├─ registerHandlers()       ← 注册命令处理器
  ├─ onStart()                ← 子类钩子（启动 API 服务器）
  ├─ autoExec                 ← 自动执行初始化命令
  │   ├─ config.autoExec
  │   └─ 环境变量 CMS_AUTO_EXEC
  │
  ├─ while (running)
  │   ├─ readLine("cms> ")    ← JLine 读取输入
  │   ├─ 解析 (tokenize + 参数)
  │   ├─ handler.execute()
  │   └─ 支持 ; 批量 + #// 注释
  │
  ├─ close()                   ← 断开连接
  ├─ onStop()                  ← 子类钩子（停止 API 服务器）
  └─ System.exit(0)
```

---

### 2. `CmsClientConsole` — 客户端 CLI

包路径：`com.ysh.jcms.app.console.CmsClientConsole`

继承 `CmsConsole`（`createServer=false`），是实际使用的交互式客户端。

#### 注册的 Handler

**客户端 Handler**（约 50 个，处理协议交互），例如：
- 连接：`NegotiateClient`, `AssociateClient`, `ReleaseClient`, `AbortClient`
- 目录：`SvrDirClient`, `LdDirClient`, `LnDirClient`, `AllDataValuesClient` 等
- 数据：`GetDataValuesClient`, `SetDataValuesClient`, `GetDataDirectoryClient` 等
- 数据集：`GetDataSetValuesClient`, `CreateDataSetClient`, `DeleteDataSetClient` 等
- 定值组：`GetSgcbValuesClient`, `SelectActiveSgClient`, `SetEditSgValueClient` 等
- 报告：`GetBrcbValuesClient`, `SetUrcbValuesClient`, `ReportClient`（推送报告）
- 日志：`GetLcbValuesClient`, `QueryLogByTimeClient` 等
- GOOSE：`GetGoCbValuesClient`, `SetGoCbValuesClient` 等
- 控制：`SelectClient`, `OperateClient`, `CancelClient` 等
- 文件：`GetFileClient`, `SetFileClient`, `DeleteFileClient` 等

**控制台 Handler**（约 50 个，处理 CLI 命令格式化和参数校验），例如：
- `ConnectHandler`, `DisconnectHandler`, `HelpHandler`
- `GetDataValuesConsole`, `SetDataValuesConsole` 等
- `TracePduHandler`, `ClearHandler`

**推送报告处理**：注册 `ReportClient` 作为 `InnerClient.reportHandler`，当服务器推送 REPORT 帧时自动解析并打印。

#### 启动方式

```java
// main 入口
java com.ysh.jcms.app.console.CmsClientConsole

// 或通过脚本
cms.ps1 / cms.cmd
```

`onStart()` 自动启动 `CliApiServer`（API 端口默认 7899），使 `cms connect --ap ...` 等命令可通过 HTTP 远程执行。

---

### 3. `CmsServerConsole` — 服务端 CLI

包路径：`com.ysh.jcms.app.console.CmsServerConsole`

服务端控制台，继承 `CmsConsole`（`createServer=true`）。用于启动服务端、加载 SCD 文件、监控运行状态等。与 `CmsClientConsole` 结构类似但面向运维场景。

---

### 4. `CommandHandler` — 命令处理器接口

包路径：`com.ysh.jcms.app.console.CommandHandler`

```java
public interface CommandHandler {
    String name();                              // 命令名
    String description();                       // 帮助描述
    List<Param> params();                       // 参数定义
    void execute(CmsConsole console, Map<String, String> args) throws Exception;
}
```

每个命令对应一个实现类，例如：
- `ConnectHandler` — 处理 `connect --ap ...`
- `GetDataValuesConsole` — 处理 `get-data-values --refs ...`

---

### 5. `Param` — 命令参数描述

包路径：`com.ysh.jcms.app.console.Param`

```java
public class Param {
    String name;           // 参数名（如 "ap"、"refs"）
    String description;    // 帮助文本
    String defaultValue;   // 默认值（可选）
}
```

`HelpHandler` 使用 `Param` 列表生成帮助信息。

---

### 6. `ConsolePrinter` — 输出格式化工具

包路径：`com.ysh.jcms.app.console.ConsolePrinter`

提供 ANSI 彩色输出和 API 捕获流支持：

| 方法 | 颜色 | 前缀 | 用途 |
|------|------|------|------|
| `info()` | 青色 | `"  "` | 普通信息 |
| `success()` | 绿色 | `"  OK  "` | 成功 |
| `error()` | 红色 | `"  ERR "` | 错误 |
| `gray()` | 灰色 | `"  "` | 辅助信息 |
| `raw()` | 无色 | 无 | 原始输出（JSON、文件内容等） |
| `list()` | — | `"[N]"` | 序号列表 |

**捕获流机制**：使用 `ThreadLocal<OutputStream>` 支持 `CliApiServer` 的并发请求。API 请求处理时设置捕获流，输出重定向到 HTTP 响应而不是终端，避免并发输出混乱。

---

### 7. `CliApiServer` — 嵌入式 HTTP API

包路径：`com.ysh.jcms.app.console.api.CliApiServer`

基于 `com.sun.net.httpserver.HttpServer` 的轻量 HTTP 服务，提供三个端点：

| 端点 | 方法 | 功能 |
|------|------|------|
| `/api/execute` | POST | 执行 CLI 命令，返回文本输出 |
| `/api/status` | GET | 查询连接状态（JSON） |
| `/ui/*` | GET | 静态文件服务（Vue Web UI） |
| `/` | GET | 默认 → index.html |

**`/api/execute` 流程**：
```
POST /api/execute (body: cmd=connect --ap C_B5041X/S1)
    │
    ├─ 设置 ThreadLocal captureStream
    ├─ console.executeLine(cmdLine)
    │   ├─ 正常 → 输出到 captureStream
    │   └─ 异常 → 输出错误到 captureStream
    ├─ 恢复 captureStream
    └─ 返回 captureStream 的内容
```

**CORS 支持**：所有端点返回 `Access-Control-Allow-Origin: *`，允许 Web UI 跨域调用。

---

### 8. `CmsRemoteClient` — 远程 CLI 客户端

包路径：`com.ysh.jcms.app.console.api.CmsRemoteClient`

通过 HTTP 向本地 `CliApiServer` 发送命令的 main 类。是 `cms.ps1` / `cms.cmd` 脚本的后端：

```bash
# 内部本质执行：
java com.ysh.jcms.app.console.api.CmsRemoteClient connect --ap C_B5041X/S1

# 等价于通过 HTTP 调用：
# POST /api/execute → "connect --ap C_B5041X/S1"
```

---

## 使用示例

### 交互式 CLI

```
PS> cms run
cms> connect --ap C_B5041X/S1
  Connecting to 127.0.0.1:8102 ...
  OK  Associated: C_B5041X/S1
cms> server-dir
  Logical Devices:
    [0] LD0
    [1] PROT
cms> get-data-values --refs "LD0/LLN0.Mod.stVal"
  OK  ...
cms> exit
Bye.
```

### JSON 模式（脚本调用）

```bash
cms server-dir --json
{"success":true,"data":["LD0","PROT"]}
```

### 远程 API 调用

```bash
curl -X POST http://127.0.0.1:7899/api/execute -d "cmd=server-dir --json"
```

### 批量命令 + 注释

```
# 连接并查询
connect --ap C_B5041X/S1; server-dir; // 查看有哪些 LD
```

---

## 与上层模块的关系

| 模块 | 关系 |
|------|------|
| **CmsNode** | `CmsConsole` 继承 `CmsNode`，复用 `InnerClient` 和 `InnerServer` |
| **handler/console** | 每个服务的 `*Console` 类实现 `CommandHandler` 接口，处理 CLI 参数校验和输出格式化 |
| **handler/client** | 每个服务的 `*Client` 类处理协议交互（编码请求 → 发送 → 解码响应） |
| **jcms-utils/config** | 读取 `client.console.*` 配置（tracePdu, autoExec, apiPort 等） |

完整的命令处理链路：
```
用户输入 → CmsConsole.executeLine()
    → CommandHandler.execute()
        → *Console 校验参数
            → *Client 协议交互（encode → sendRequest → decode）
                → ConsolePrinter 输出
```



## Handler 注册清单

`CmsClientConsole` 自动注册的全部命令（约 60 个）：

| 类别 | 命令 |
|------|------|
| **系统** | help, clear, exit |
| **连接** | connect, disconnect, associate, release, abort, negotiate, test |
| **调试** | trace-pdu |
| **目录** | server-dir, ld-dir, ln-dir, all-data, all-def, all-cb |
| **数据** | get-data-values, set-data-values, data-dir, get-data-def |
| **数据集** | get-dataset-values, set-dataset-values, create-dataset, delete-dataset, get-dataset-dir |
| **定值组** | select-active-sg, select-edit-sg, set-edit-sg, get-edit-sg, confirm-edit-sg, sgcb-vals |
| **报告** | get-brcb-vals, set-brcb-vals, get-urcb-vals, set-urcb-vals |
| **日志** | get-lcb-vals, set-lcb-vals, query-log-time, query-log-after, get-log-status |
| **GOOSE** | get-gocb-values, set-gocb-values, get-go-ref, get-goose-elem |
| **控制** | select, select-with-value, operate, cancel |
| **文件** | get-file, set-file, delete-file, get-file-attrs, get-file-dir |

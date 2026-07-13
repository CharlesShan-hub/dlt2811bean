# jcms-svc — §8 服务报文段 Java 封装

## 职责

jcms-svc 是 DL/T 2811 标准第 8 章定义的\*\*所有服务报文段（PDU）\*\*在 Java 侧的实现。每个服务（如关联、目录、数据读写等）对应一组 Request / Response / Error 报文结构，以及相关的辅助类型。

一句话概括：**§8 服务 PDU 的 Java 镜像，覆盖所有请求/响应/错误报文**。

> 不是AI的总结：总的来讲有了jcms-data的实现，这一模块更简单了一些，他们基本就是jcms-data的组合。另外呢，由于我设计的这个结构支持几乎无限层的嵌套，jcms-svc看起来很简单，就是简单内容的嵌套。为了方便理解，这里可能牺牲了一些间接性，比如一些Response-，就算他们内容是一样的，我也是严格和标准统一，写成了不同的类。

## 与 svc 的关系

| 层面 | ccms-svc（C）      | jcms-svc（Java）                               |
| -- | ---------------- | -------------------------------------------- |
| 角色 | 服务 PDU 的 PER 编解码 | 类型声明 + 编解码编排                                 |
| 范围 | 纯数据结构编解码         | 仅 PDU 结构，不含协议交互逻辑                            |
| 用法 | C 函数直接调用         | 继承 `CmsType`，通过 `NativeBridge.Codec` 调用 C 函数 |

svc（包括 C 和 Java 两边）只负责**对服务报文段的数据结构进行编解码**，不负责协议的交互流程（那由 jcms-app/handler 处理）。

## 包结构

```
svc/
├── connection/     # 8.2  关联服务
├── negotiate/      # 8.15 协商服务
├── directory/      # 8.3  目录服务
├── data/           # 8.4  数据访问服务
├── dataset/        # 8.5  数据集服务
├── sg/             # 8.6  定值组服务
├── report/         # 8.7  报告服务
├── log/            # 8.8  日志服务
├── goose/          # 8.9  GOOSE 服务
├── msv/            # 8.10 采样值服务
├── control/        # 8.11 控制服务
├── file/           # 8.12 文件服务
├── rpc/            # 8.13 RPC 服务
├── test/           # 8.14 测试服务
└── other/          # 辅助类型（AssociationId, ReqId, ReferenceChoice）
```

***

## 子包详解

### 1. `connection` — 关联服务（§8.2）

**4 组** PDU（每组 Request + Response + Error，关联中止无 Response）：

| 类                                                                    | 对应服务            |
| -------------------------------------------------------------------- | --------------- |
| `CmsAssociateRequest` / `CmsAssociateResponse` / `CmsAssociateError` | 8.2.1 建立关联      |
| `CmsReleaseRequest` / `CmsReleaseResponse` / `CmsReleaseError`       | 8.2.2 释放关联      |
| `CmsAbort` / `CmsAbortReason`                                        | 8.2.3 中止关联（无响应） |
| `CmsAuthenticationParameter`                                         | 关联请求中的认证参数      |

典型结构（以 Associate 为例）：

```
Associate-RequestPDU ::= SEQUENCE {
    reqId                     Int16U,
    serverAccessPointReference [0] IMPLICIT VisibleString129 OPTIONAL,
    authenticationParameter   [1] IMPLICIT AuthenticationParameter OPTIONAL
}
```

对应 Java 字段：`reqId` + `sapRefPresent`/`sapRef` + `authParamPresent`/`authParam`。OPTIONAL 字段通过 `CmsBoolean present` 标志 + 实际值字段实现。

***

### 2. `negotiate` — 协商服务（§8.15）

| 类                      | 说明                                          |
| ---------------------- | ------------------------------------------- |
| `CmsNegotiateRequest`  | 协商请求（apduSize + asduSize + protocolVersion） |
| `CmsNegotiateResponse` | 协商响应                                        |
| `CmsNegotiateError`    | 协商错误                                        |

***

### 3. `directory` — 目录服务（§8.3）

**6 组** PDU：

| 服务                        | 章节    | Request/Response/Error |
| ------------------------- | ----- | ---------------------- |
| GetServerDirectory        | 8.3.1 | 获取逻辑设备列表               |
| GetLogicalDeviceDirectory | 8.3.2 | 获取逻辑节点列表               |
| GetLogicalNodeDirectory   | 8.3.3 | 获取数据对象/数据集/控制块目录       |
| GetAllDataValues          | 8.3.4 | 获取全部数据值                |
| GetAllDataDefinition      | 8.3.5 | 获取所有数据定义               |
| GetAllCbValues            | 8.3.6 | 获取所有控制块值               |

辅助类型：

| 类                                                                  | 说明                                            |
| ------------------------------------------------------------------ | --------------------------------------------- |
| `CmsAcsiClass`                                                     | ACSI 类枚举（data-object, data-set, brcb, urcb 等） |
| `CmsObjectClass`                                                   | 对象类描述                                         |
| `CmsDataValueEntry` / `CmsDataDefinitionEntry` / `CmsCbValueEntry` | 响应中的条目类型                                      |
| `CmsCbValueChoice`                                                 | 控制块值的 CHOICE（BRCB/URCB/LCB 等）                 |

***

### 4. `data` — 数据访问服务（§8.4）

**4 组** PDU：

| 服务                | 章节    | 说明     |
| ----------------- | ----- | ------ |
| GetDataValues     | 8.4.1 | 读数据值   |
| SetDataValues     | 8.4.2 | 写数据值   |
| GetDataDirectory  | 8.4.3 | 获取数据目录 |
| GetDataDefinition | 8.4.4 | 获取数据定义 |

辅助类型：

| 类                       | 说明           |
| ----------------------- | ------------ |
| `CmsDataRefEntry`       | 数据引用条目（含 fc） |
| `CmsDataRefValueEntry`  | 数据引用 + 值对    |
| `CmsDataDefResultEntry` | 数据定义结果条目     |
| `CmsSubRefEntry`        | 子引用条目        |

***

### 5. `dataset` — 数据集服务（§8.5）

**5 组** PDU：

| 服务                  | 章节    | 说明     |
| ------------------- | ----- | ------ |
| GetDataSetValues    | 8.5.1 | 读数据集值  |
| SetDataSetValues    | 8.5.2 | 写数据集值  |
| CreateDataSet       | 8.5.3 | 创建数据集  |
| DeleteDataSet       | 8.5.4 | 删除数据集  |
| GetDataSetDirectory | 8.5.5 | 读数据集目录 |

辅助类型：

| 类                   | 说明                  |
| ------------------- | ------------------- |
| `CmsDataRefFcEntry` | 数据引用 + 功能约束对（数据集成员） |

***

### 6. `sg` — 定值组服务（§8.6）

**6 组** PDU：

| 服务                  | 章节    | 说明         |
| ------------------- | ----- | ---------- |
| SelectActiveSG      | 8.6.1 | 选择激活定值组    |
| SelectEditSG        | 8.6.2 | 选择编辑定值组    |
| SetEditSGValue      | 8.6.3 | 设置编辑定值值    |
| ConfirmEditSGValues | 8.6.4 | 确认编辑定值（生效） |
| GetEditSGValue      | 8.6.5 | 读编辑定值值     |
| GetSGCBValues       | 8.6.6 | 读定值组控制块    |

辅助类型：

| 类                    | 说明             |
| -------------------- | -------------- |
| `CmsSgRefFcEntry`    | 定值引用 + fc 对    |
| `CmsSgRefValueEntry` | 定值引用 + 值 + 类型对 |
| `CmsSgcbValueChoice` | SGCB 值 CHOICE  |
| `CmsSgcbValueEntry`  | SGCB 值条目       |

***

### 7. `report` — 报告服务（§8.7）

**5 组** PDU + 报告推送：

| 服务                            | 章节       | 说明             |
| ----------------------------- | -------- | -------------- |
| Report                        | 8.7.1    | 服务器主动推送报告（无确认） |
| GetBRCBValues / SetBRCBValues | 8.7.2/.3 | 缓存报告控制块读写      |
| GetURCBValues / SetURCBValues | 8.7.4/.5 | 非缓存报告控制块读写     |

辅助类型：

| 类                                       | 说明           |
| --------------------------------------- | ------------ |
| `CmsReport`                             | 报告报文本身       |
| `CmsReportEntry` / `CmsReportDataEntry` | 报告中的条目/数据条目  |
| `CmsRcbValueChoice`                     | RCB 值 CHOICE |
| `CmsSetBrcbEntry` / `CmsSetBrcbResult`  | BRCB 设置条目/结果 |
| `CmsSetUrcbEntry` / `CmsSetUrcbResult`  | URCB 设置条目/结果 |

***

### 8. `log` — 日志服务（§8.8）

**5 组** PDU：

| 服务                          | 章节       | 说明      |
| --------------------------- | -------- | ------- |
| GetLCBValues / SetLCBValues | 8.8.2/.3 | 日志控制块读写 |
| QueryLogByTime              | 8.8.4    | 按时间查询日志 |
| QueryLogAfter               | 8.8.5    | 按条目查询日志 |
| GetLogStatusValues          | 8.8.6    | 读日志状态值  |

辅助类型：

| 类                                               | 说明           |
| ----------------------------------------------- | ------------ |
| `CmsLogEntry` / `CmsLogDataEntry`               | 日志条目及其数据     |
| `CmsLogStatusValue` / `CmsLogStatusValueChoice` | 日志状态值        |
| `CmsLcbValueChoice`                             | LCB 值 CHOICE |
| `CmsSetLcbEntry` / `CmsSetLcbResult`            | LCB 设置条目/结果  |

***

### 9. `goose` — GOOSE 服务（§8.9）

**5 组** PDU：

| 服务                            | 章节       | 说明               |
| ----------------------------- | -------- | ---------------- |
| SendGOOSEMessage              | 8.9.1    | 发送 GOOSE 报文（无确认） |
| GetGoReference                | 8.9.2    | 读 GOOSE 引用       |
| GetGOOSEElementNumber         | 8.9.3    | 读 GOOSE 元素序号     |
| GetGoCBValues / SetGoCBValues | 8.9.4/.5 | GOOSE 控制块读写      |

辅助类型：

| 类                                                             | 说明               |
| ------------------------------------------------------------- | ---------------- |
| `CmsGoRefFcEntry`                                             | GOOSE 引用 + fc 条目 |
| `CmsGocbValueChoice` / `CmsSetGoCbEntry` / `CmsSetGoCbResult` | 控制块值操作           |
| `CmsSendGooseMessage`                                         | GOOSE 报文数据结构     |

***

### 10. `msv` — 采样值服务（§8.10）

**3 组** PDU：

| 服务                              | 章节        | 说明           |
| ------------------------------- | --------- | ------------ |
| SendMSVMessage                  | 8.10.1    | 发送采样值报文（无确认） |
| GetMSVCBValues / SetMSVCBValues | 8.10.2/.3 | 采样值控制块读写     |

辅助类型与 goose 结构类似。

***

### 11. `control` — 控制服务（§8.11）

**7 组** PDU：

| 服务                              | 章节     | 说明     |
| ------------------------------- | ------ | ------ |
| Select                          | 8.11.1 | 选择控制对象 |
| SelectWithValue                 | 8.11.2 | 带值选择   |
| Operate                         | 8.11.3 | 执行控制操作 |
| Cancel                          | 8.11.4 | 取消控制操作 |
| CommandTermination              | 8.11.5 | 命令终止通知 |
| TimeActivatedOperate            | 8.11.6 | 定时执行   |
| TimeActivatedOperateTermination | 8.11.7 | 定时执行终止 |

每组合 `CmsXxxRequest` / `CmsXxxResponse` / `CmsXxxError` 三个类。

***

### 12. `file` — 文件服务（§8.12）

**5 组** PDU：

| 服务                     | 章节     | 说明      |
| ---------------------- | ------ | ------- |
| GetFile                | 8.12.1 | 读文件（下载） |
| SetFile                | 8.12.2 | 写文件（上传） |
| DeleteFile             | 8.12.3 | 删除文件    |
| GetFileAttributeValues | 8.12.4 | 读文件属性   |
| GetFileDirectory       | 8.12.5 | 列文件目录   |

***

### 13. `rpc` — RPC 服务（§8.13）

**6 组** PDU：

| 服务                        | 章节     | 说明         |
| ------------------------- | ------ | ---------- |
| GetRPCInterfaceDirectory  | 8.13.2 | 读 RPC 接口目录 |
| GetRPCMethodDirectory     | 8.13.3 | 读 RPC 方法目录 |
| GetRPCInterfaceDefinition | 8.13.4 | 读 RPC 接口定义 |
| GetRPCMethodDefinition    | 8.13.5 | 读 RPC 方法定义 |
| RPCCall                   | 8.13.6 | RPC 调用     |

辅助类型：

| 类                                           | 说明                |
| ------------------------------------------- | ----------------- |
| `CmsRpcMethodDef` / `CmsRpcMethodDefChoice` | 方法定义              |
| `CmsRpcMethodEntry`                         | 方法条目              |
| `CmsRpcCallReqChoice`                       | RPC 调用请求参数 CHOICE |

***

### 14. `test` — 测试服务（§8.14）

| 类         | 说明                  |
| --------- | ------------------- |
| `CmsTest` | 测试连接 PDU（ping/pong） |

***

### 15. `other` — 辅助类型

| 类                    | 说明                    |
| -------------------- | --------------------- |
| `CmsAssociationId`   | 关联 ID，Int16U          |
| `CmsReqId`           | 请求 ID，Int16U，关联请求中使用  |
| `CmsReferenceChoice` | 引用 CHOICE（对象名 / 对象引用） |

***

## 设计模式

所有 svc 类遵循统一的设计模式：

### PDU 三元组

每个服务通常包含三个类：

```
CmsXxxRequest   — 请求 PDU（客户端 → 服务器）
CmsXxxResponse  — 响应 PDU（服务器 → 客户端，成功时）
CmsXxxError     — 错误 PDU（服务器 → 客户端，失败时）
```

例外：

- **无确认服务**（Report、SendGOOSEMessage、SendMSVMessage）：只有报文结构，没有 Request/Response
- **Abort**：只有 Request，无 Response/Error

### 结构体声明

每个 PDU 类继承 `CmsType`，典型结构：

```java
public class CmsXxxRequest extends CmsType {
    // 公开字段 = ASN.1 SEQUENCE 的成员
    public CmsInt32U field1;
    public CmsBoolean field2;
    public CmsUint8Array field3;

    public CmsXxxRequest() {
        super(Codec.XXX_REQUEST);    // 绑定 C 编解码函数
        this.field1 = new CmsInt32U();
        this.field2 = new CmsBoolean();
        this.field3 = new CmsUint8Array();
    }

    // Fluent setter 链式赋值
    public CmsXxxRequest field1(long v) { this.field1.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(field1, field2, field3);
    }
}
```

### OPTIONAL 字段

OPTIONAL 字段使用 `CmsBoolean present` 标志 + 实际值字段的模式：

```java
public CmsBoolean optFieldPresent;    // 标志位
public CmsInt32U optField;            // 实际值（当 present 为 true 时有效）
```

这种模式对应 C 侧 `cms_optional()` 辅助函数，由 C 编解码器自动处理。

***

## 与 jcms-app/handler 的关系

| 层次                   | 职责                            |
| -------------------- | ----------------------------- |
| **jcms-svc**         | 定义 PDU 数据结构 + 编解码能力           |
| **jcms-app/handler** | 协议交互逻辑（构建请求 → 发送 → 接收 → 解析响应） |

svc 只回答"报文长什么样"；handler 回答"报文怎么发、怎么收、怎么处理"。

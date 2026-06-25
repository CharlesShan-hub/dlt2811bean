# svc 服务数据单元 (SDU) 模块参考手册

> 模块路径: [`cms/ccms/include/svc/`](../ccms/include/svc/) + [`cms/ccms/src/svc/`](../ccms/src/svc/)
> 实现语言: **C11**
> 位置: ccms 顶层，组装 data 零件构成完整协议 SDU

---

## 目录

1. [总览](#1-总览)
2. [APDU 与 ASDU](#2-apdu-与-asdu)
3. [统一结构模式](#3-统一结构模式)
4. [OPTIONAL bitmap 规则](#4-optional-bitmap-规则)
5. [目录组织](#5-目录组织)

---

## 1. 总览

svc 层是 DL/T 2811 协议的服务数据单元（SDU）实现层。每个服务对应协议中定义的一组 SEQUENCE（Request/Response/Error），负责把这些 SEQUENCE 的字段组装成完整的 PER 二进制报文。

svc 层不关心每个字段的比特编码细节（那是 per 的职责），也不关心字段的协议语义约束（那是 data 的职责）——它处于"知道每个服务有哪些字段、按什么顺序编解码"的位置。

### 层次定位

```
┌──────────────────────────────────┐
│  svc  — 服务数据单元 (SDU)         │  ← 你在这里
│        组装 data 零件成完整报文     │
├──────────────────────────────────┤
│  data — 协议定义的零件类型          │
├──────────────────────────────────┤
│  per  — PER 编码原语              │
└──────────────────────────────────┘
```

---

## 2. APDU 与 ASDU

DL/T 2811 协议沿用了 IEC 61850 的 APDU/ASDU 分层概念：

- **APDU** (Application Protocol Data Unit) — 应用协议数据单元，即网络上传输的完整报文帧。包含服务标识、长度、会话关联等信息。
- **ASDU** (Application Service Data Unit) — 应用服务数据单元，即服务特定的负载数据。

当前 ccms 实现中，svc 层**仅负责 ASDU 载荷的编解码**。APDU 封装（加服务标签 TLV、分片重组等）由上层 `jcms` 处理。

唯一的例外是 **`negotiate/` 服务**，其 SEQUENCE 中包含 `apduSize` 和 `asduSize` 字段——这两个字段是会话协商时约定 APDU 和 ASDU 的最大容量，而非实际 APDU 封装。

```
┌── 网络报文 ──────────────────────────┐
│  APDU 帧头（jcms）:                   │
│    - 服务标签 (ServiceID)             │
│    - 长度 (FL)                        │
│    - 可选控制字段                     │
├──────────────────────────────────────┤
│  ASDU 载荷（ccms svc）:              │
│    [reqId] [OPTIONAL bitmap] [fields]│
└──────────────────────────────────────┘
```

---

## 3. 统一结构模式

### 3.1 三段式结构

所有服务（除 `test` 外）都采用 **Request / Response / Error** 三段式结构：

```c
// 每个服务三个 PDU 类型
typedef struct { ... } cms_xxx_request_t;    // 请求
typedef struct { ... } cms_xxx_response_t;   // 响应
typedef struct { ... } cms_xxx_error_t;      // 错误（通常 = reqId + ServiceError）
```

每个 PDU 类型暴露 **2 个函数**（buffer 层）：

```c
int cms_xxx_request_encode(const cms_xxx_request_t *pdu, uint8_t *out_buf, int *out_len);
int cms_xxx_request_decode(cms_xxx_request_t *pdu, const uint8_t *in_buf, int in_len);
```

对于包含 `SEQUENCE OF` 子字段的复杂结构（如 `report/cms_report_entry`），额外提供 stream 层函数供父类型嵌套调用：

```c
int cms_xxx_encode_stream(per_stream_t *s, const cms_xxx_t *v);
int cms_xxx_decode_stream(per_stream_t *s, cms_xxx_t *v);
```

### 3.2 reqId — 第一个字段

**除 `test` 外，所有服务的所有 PDU 都以 `reqId`（Int16U）开头**，编号恒为 `/* 0. */`。`reqId` 是请求-响应对应的标识，由调用方分配。

`test` 服务（服务码 0xA1）无服务特定字段，只有 `reqId`，完全由基础框架处理，因此没有自己的 encode/decode 函数。

### 3.3 ErrorPDU 的简化模式

大多数服务的 ErrorPDU 只有两个字段：

```c
/* 0. reqId — Int16U */
/* 1. serviceError — ServiceError */
```

这些文件通常体积很小，放在对应服务目录下，没有单独的子目录。

---

## 4. OPTIONAL bitmap 规则

每个 SEQUENCE 中所有 OPTIONAL/DEFAULT 字段共用一个 bitmap（X.691 §22），编码在 `reqId` 之后（位置 1）：

```
/* 0. reqId — Int16U */
/* 1. OPTIONAL bitmap (N fields: fieldA, fieldB, ...) */
/* 2. fieldA — TypeA OPTIONAL (bitmap[0]) */
/* 3. fieldB — TypeB OPTIONAL (bitmap[1]) */
...
```

对于没有 `reqId` 的内嵌 SEQUENCE（如 `ReportEntry`、`ReportDataEntry`），bitmap 在位置 0：

```
/* 0. OPTIONAL bitmap (N fields) */
/* 1. fieldA OPTIONAL (bitmap[0]) */
...
```

实现使用 `bool[]` 数组 + `per_encode_optional_bitmap`/`per_decode_optional_bitmap`：

```c
bool opt_present[N] = { (pdu->field_present && pdu->field_present->value) && pdu->field, ... };
per_encode_optional_bitmap(s, opt_present, N);
if (opt_present[i]) { ... }
```

---

## 5. 目录组织

### `connection/` — 关联服务

服务码 0x01～0x04：Associate、Release、Abort。

包括 `associate`（关联建立，包含 sapRef/authParam 等 OPTIONAL）、`release`（正常释放）、`abort`（异常中止）。`AuthenticationParameter` 是关联阶段的认证参数类型。

### `negotiate/` — 参数协商

服务码 0x00：Associate Negotiate。

协商 APDU 最大尺寸、ASDU 最大尺寸、协议版本。这是唯一在 SEQUENCE 中直接出现 `apduSize`/`asduSize` 字段的服务。包括 Request、Response、Error。

### `control/` — 控制服务

服务码 0x20～0x26：Select、SelectWithValue、Cancel、Operate、CommandTermination、TimeActivatedOperate、TimeActivatedOperateTermination。

每个服务包含 request（控制命令）和 error（错误响应）。`CommandTermination` 和 `TimeActivatedOperateTermination` 是未确认服务（仅 request，无 response/error）。

### `data/` — 数据服务

服务码 0x30～0x33：GetDataValues、SetDataValues、GetDataDirectory、GetDataDefinition。

包含 `cms_data_ref_entry`（数据引用条目）、`cms_data_ref_value_entry`（带值引用）、`cms_sub_ref_entry`（子引用）、`cms_data_def_result_entry`（数据定义结果）等辅助 SEQUENCE 类型。

### `data/` 子入口 (data/ 目录下的特殊文件)

- `cms_data_ref_entry.c` — DataRefEntry SEQUENCE（reference + OPTIONAL fc）
- `cms_data_ref_value_entry.c` — 带值的 DataRefValueEntry
- `cms_sub_ref_entry.c` — SubReference 条目
- `cms_data_def_result_entry.c` — DataDefinition 结果条目

### `dataset/` — 数据集服务

服务码 0x34～0x37：GetDataSetValues、SetDataSetValues、CreateDataSet、DeleteDataSet、GetDataSetDirectory。

包含 `cms_data_ref_fc_entry`（功能约束引用条目，用于 CreateDataSet 的 SetBRCB/SetURCB 的 DataSet 成员列表）。

### `directory/` — 目录服务

服务码 0x38～0x3F：GetServerDirectory、GetLogicalDeviceDirectory、GetLogicalNodeDirectory、GetAllDataValues、GetAllDataDefinition、GetAllCBValues。

这是 svc 中文件最多的目录。包含：
- 6 个服务主文件（各含 request/response/error）
- 辅助类型：`cms_acsi_class`（ACSI 类）、`cms_object_class`（对象类）
- 子 SEQUENCE 类型：`cms_data_definition_entry`、`cms_data_value_entry`、`cms_cb_value_entry`、`cms_cb_value_choice`

### `report/` — 报告服务

服务码 0x35：Report（未确认服务）。

报告是 DL/T 2811 中最复杂的服务之一，包含：
- `cms_report` — ReportPDU 主类型（reqId + rptID + optFlds + 6 个 OPTIONAL + ReportEntry）
- `cms_report_entry` — ReportEntry（timeOfEntry + entryID + SEQUENCE OF ReportDataEntry）
- `cms_report_data_entry` — ReportDataEntry（ref/fc/id/value/reason）
- `cms_set_brcb_entry/result`、`cms_set_urcb_entry/result` — BRCB/URCB 设置条目的 request 和 error 类型
- `cms_get_brcb_values`、`cms_get_urcb_values` — BRCB/URCB 值的读取
- `cms_set_brcb_values`、`cms_set_urcb_values` — BRCB/URCB 值的写入
- `cms_rcb_value_choice` — RCB 值类型分派

### `log/` — 日志服务

服务码 0x40～0x44：GetLCBValues、SetLCBValues、GetLogStatusValues、QueryLogByTime、QueryLogAfter。

包含：
- `cms_set_lcb_entry/result` — LCB 设置条目
- `cms_query_log_by_time` — 按时间范围查日志（3 个 OPTIONAL 时间/ID 过滤）
- `cms_query_log_after` — 按 EntryID 查日志（1 个 OPTIONAL 起始时间）
- `cms_log_entry` — 日志条目 SEQUENCE
- `cms_log_data_entry` — 日志数据条目
- `cms_log_status_value` / `cms_log_status_value_choice` — 日志状态值
- `cms_lcb_value_choice` — LCB 值类型分派
- `cms_get_lcb_values` — LCB 值读取

### `goose/` — GOOSE 服务

服务码 0x60～0x64：GetGoReference、GetGOOSEElementNumber、GetGoCBValues、SetGoCBValues、SendGOOSEMessage。

包含：
- `cms_send_goose_message` — GOOSE 报文（reqId + 2 个 OPTIONAL + 多个必选字段 + Data 数组）
- `cms_set_go_cb_entry/result` — GoCB 设置条目
- `cms_set_go_cb_values` — GoCB 值写入
- `cms_get_go_cb_values` — GoCB 值读取
- `cms_get_go_reference` — GoReference 查询
- `cms_get_goose_element_number` — GOOSE 元素序号查询
- `cms_go_ref_fc_entry` — GoRef 功能约束条目
- `cms_gocb_value_choice` — GoCB 值类型分派

### `msv/` — 采样值服务

服务码 0x70～0x74：GetMSVCBValues、SetMSVCBValues、SendMSVMessage。

与 GOOSE 结构类似。包含：
- `cms_send_msv_message` — MSV 报文（reqId + 4 个 OPTIONAL + 多个必选字段 + Data 数组）
- `cms_set_msvcb_entry/result` — MSVCB 设置条目
- `cms_set_msvcb_values` — MSVCB 值写入
- `cms_get_msvcb_values` — MSVCB 值读取
- `cms_msvcb_value_choice` — MSVCB 值类型分派

### `sg/` — 定值组服务

服务码 0x50～0x56：SelectActiveSG、SelectEditSG、SetEditSGValue、ConfirmEditSGValues、GetSGValues、GetSGCBValues。

包含：
- `cms_select_active_sg` / `cms_select_edit_sg` — 定值组选择
- `cms_set_edit_sg_value` / `cms_confirm_edit_sg_values` — 定值编辑与确认
- `cms_get_edit_sg_value` — 读取编辑中的定值
- `cms_get_sgcb_values` — SGCB 值读取
- `cms_sg_ref_fc_entry` / `cms_sg_ref_value_entry` — 定值组引用类型
- `cms_sgcb_value_choice` — SGCB 值类型分派

### `rpc/` — RPC 服务

服务码 0x80～0x83：GetRPCInterfaceDirectory、GetRPCMethodDirectory、GetRPCInterfaceDefinition、RPCCall。

包含：
- `cms_rpc_call` — RPC 调用（request 含 method + RpcCallReqChoice，response 含 Data + OPTIONAL nextCallId）
- `cms_get_rpc_interface_directory` / `cms_get_rpc_method_directory` — 接口/方法目录查询
- `cms_get_rpc_interface_definition` — 接口定义查询（含 SEQUENCE OF RpcMethodEntry）
- `cms_get_rpc_method_definition` — 方法定义查询
- `cms_rpc_method_entry` / `cms_rpc_method_def` / `cms_rpc_method_def_choice` — RPC 方法相关类型
- `cms_rpc_call_req_choice` — RPC 调用请求体的 CHOICE 类型

### `file/` — 文件服务

服务码 0x90～0x94：GetFile、SetFile、DeleteFile、GetFileAttributeValues、GetFileDirectory。

- `cms_get_file_directory` — 文件目录查询（reqId + pathName + 3 个 OPTIONAL 时间/文件过滤）
- `cms_get_file` / `cms_set_file` / `cms_delete_file` — 文件读写删
- `cms_get_file_attribute_values` — 文件属性查询
- `cms_visible_string255` — 协议定义的 VisibleString(SIZE(0..255)) 约束类型

### `test/` — 测试服务

服务码 0xA1。无服务特定字段，只有 `reqId`。没有独立的 encode/decode 函数。

### `other/` — 跨服务公用类型

- `cms_req_id` — reqId 的编解码（Int16U 包装）
- `cms_association_id` — AssociationID 类型
- `cms_reference_choice` — Reference 的 CHOICE 分发（用于多种引用类型的统一处理）

---

## 6. 与 data 层的关系

svc 层**不直接调用 per 原语**，所有字段的编解码都通过 data 层完成：

```c
// svc 层代码 —— 组装 data 零件
err = cms_object_reference_encode_stream(s, pdu->reference);   // data/common
err = cms_boolean_encode_stream(s, pdu->rpt_ena);             // data/scalar
err = cms_int32u_encode_stream(s, pdu->conf_rev);             // data/scalar
```

唯一的例外是 OPTIONAL bitmap 和 SEQUENCE OF 长度，它们直接使用 per 层原语：

```c
#include "per/cms_sequence.h"
per_encode_optional_bitmap(s, opt_present, N);   // X.691 §22
per_decode_optional_bitmap(s, opt_present, N);

#include "per/cms_integer.h"
per_encode_length(s, cnt);   // SEQUENCE OF length determinant
per_decode_length(s, &cnt);
```

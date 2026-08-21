# jcms-core.info — 协议元信息枚举

## 1. 模块简介

可执行的协议参考手册：把 DL/T 2811 / IEC 61850 的语义常量集中成 5 个枚举，供上层查询、校验、展示。

1. **定位**：jcms-core 的纯信息子包 `com.ysh.jcms.core.info`，只查不跑（无运行时逻辑）
2. **内容**：5 张参考表 = 服务 / 数据类型 / CDC / LN / FC
3. **与 core.data 的分工**：data 参与编解码，info 只做语义查询
4. **命名约定**：
   - 枚举统一命名 `Cms*Info`
   - 使用 Lombok fluent 访问器（`@Getter @Accessors(fluent = true)`）
   - 内部字段不叫 `name`，统一 `xxxName`（如 `cdcName` / `lnName`）

## 2. 具体内容（5 张参考表）

### CmsFCInfo — 功能约束（FC）· 13 项

| FC | 英文 | 中文 | 可写 |
| -- | ---- | ---- | -- |
| ST | Status information | 状态信息 | 否 |
| MX | Measurands | 测量值 | 否 |
| SP | Setting (outside SG) | 设置值（组外） | 是 |
| SV | Substitution | 替代值 | 是 |
| CF | Configuration | 配置 | 是 |
| DC | Description | 描述 | 是 |
| SG | Setting group | 设置组 | 否 |
| SE | Setting group editable | 设置组可编辑 | 是 |
| SR | Service response | 服务响应 | 否 |
| OR | Operate received | 操作接收 | — |
| BL | Blocking | 阻塞 | 是 |
| EX | Extended definition | 扩展定义 | 否 |
| XX | All FCs (service param) | 全部 FC | — |

### CmsServiceInfo — 服务定义 · 全部已实现服务

| 组 | 章节 | 服务 |
| -- | ---- | ---- |
| Association | 8.2 | associate, release, abort, negotiate |
| Directory | 8.3 | server-dir, ld-dir, ln-dir, all-data, all-def, all-cb |
| Data Access | 8.4 | get/set-data-values, data-dir, get-data-def |
| Data Set | 8.5 | get/set-dataset-values, create/delete-dataset, get-dataset-dir |
| Setting Group | 8.6 | select-active/edit-sg, set/get-edit-sg, confirm-edit-sg, get-sgcb-values |
| Reporting | 8.7 | report, get/set-brcb-values, get/set-urcb-values |
| Logging | 8.8 | get/set-lcb-values, query-log-time/after, get-log-status |
| GOOSE | 8.9 | send-goose, get-go-ref, get-goose-elem, get/set-gocb-values |
| Sampled Value | 8.10 | send-msv, get/set-msvcb-values |
| Control | 8.11 | select, select-with-value, operate, cancel, cmd-term, time-act-ope, time-act-ope-term |
| File | 8.12 | get/set/delete-file, get-file-attrs, get-file-dir |
| RPC | 8.13 | rpc-if-dir/method-dir, rpc-if-def/method-def, rpc-call |
| Test | 8.14 | test |

### CmsCdcInfo — 公用数据类（CDC）· 35 项

| 分组 | CDC | 用途 |
| ---- | --- | ---- |
| 状态量 | SPS, DPS, INS, ACT, ACD, SEC, BCR | 开关位置、整数状态、保护动作 |
| 控制量 | SPC, DPC, INC, BAC, ISC | 遥控分合、可调整数、步进控制 |
| 遥测量 | MV, CMV, WYE, DEL, SEQ, HMV, HWYE, HDEL | 模拟量测量 |
| 采样值 | SAV, ISAV | 采样值数据 |
| 定值 | SPG, ING, ASG, CURVE, VSS, LPL, CSG | 各种类型定值 |
| 控制块 | BRCB, URCB, LCB, SGCB, MSVCB, USVCB, GOOSE, GSSE | 各类控制块 |
| 其他 | LLN0, LOG, LOCAL | LN0、日志、本地操作 |

### CmsLnInfo — 逻辑节点（LN）· 80+ 项

| LnClass | 分组名 | 示例 LN |
| ------- | ------ | ------- |
| L | 系统逻辑节点 | LPHD（物理装置）、LLN0（LN零） |
| P | 保护功能 | PDIS（距离保护）、PIOC（瞬时过流）、PTOC（延时过流） |
| R | 保护相关 | RDRE（扰动记录）、RREC（自动重合闸）、RSYN（同期） |
| C | 控制 | CSWI（开关控制器）、CILO（联锁） |
| G | 通用引用 | GGIO（通用I/O） |
| I | 接口和存档 | IHMI（人机接口）、ITCI（远方控制接口） |
| A | 自动控制 | ATCC（自动调分接开关） |
| M | 计量和测量 | MMXU（测量）、MMTR（计量） |
| S | 传感器监视 | SIMG（绝缘气体监视） |
| X | 开关设备 | XCBR（断路器）、XSWI（隔离开关） |
| T | 仪用互感器 | TCTR（电流互感器）、TVTR（电压互感器） |
| Y | 电力变压器 | YPTR（电力变压器）、YLTC（分接开关） |
| Z | 其他电力设备 | ZBAT（电池）、ZLIN（架空线）、ZMOT（电动机） |

### CmsDataTypeInfo — 数据类型 · 40+ 项

| 分组 | 示例类型 | ASN.1 概要 |
| ---- | ------- | --------- |
| 基础数值（§7.1） | BOOLEAN, INT8~64, INT8U~64U, FLOAT32/64 | 约束整数、定长字节串 |
| 字符串（§7.1.5） | VisibleString, OCTET STRING, UTF8String, BIT STRING | 长度+内容 |
| 对象引用（§7.3） | ObjectName, ObjectReference, SubReference, EntryId | 定长/范围约束 VisibleString |
| 时间（§7.2） | UtcTime, BinaryTime, TimeQuality | 定长字节串 |
| 复合类型（§7.3/7.5） | Originator, Check, Quality, Dbpos, PhyComAddr | SEQUENCE 或 BIT STRING |
| 数据值（§7.7） | Data, DataDefinition | CHOICE of 24 种类型 |
| 控制块（§7.6） | BRCB, URCB, LCB, SGCB, GoCB, MSVCB | SEQUENCE |
| 错误/枚举 | ServiceError, AddCause, TriggerConditions, ReasonCode | 约束整数或 BIT STRING |

## 3. 使用场景

1. **CLI 参数校验**：服务名 → `CmsServiceInfo.byName()`
2. **报文解析提示**：数据类型 → `CmsDataTypeInfo.byTypeName()`
3. **目录展示**：SCD 解析出的 LN/CDC/FC → 中英文名映射
4. **自动补全**：CLI Tab 补全（`--acsi brcb` 等）

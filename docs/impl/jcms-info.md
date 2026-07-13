# jcms-info — 协议元信息枚举

## 职责

jcms-info 是整个项目中**最轻量**的模块——只包含 5 个枚举类，没有运行时逻辑，全是**文档性质的协议元信息**。

它的存在意义是：把 DL/T 2811 / IEC 61850 标准中散落在各章节的**语义常量**（服务定义、数据类型、CDC、逻辑节点、功能约束）集中到一处，供上层模块查阅、校验和展示，避免硬编码和文档与代码脱节。

一句话概括：**可执行的协议参考手册**。

## 与核心层的区别

| 模块                    | 性质    | 用途                        |
| --------------------- | ----- | ------------------------- |
| **jcms-core**（`core`） | 运行时基类 | 定义类型系统、管理 native 内存、编解码编排 |
| **jcms-info**（`info`） | 纯枚举信息 | 协议语义查询、CLI 自动补全/提示、校验     |

core 的类参与编解码流程；info 的枚举只提供信息查询，**不参与**任何运行时逻辑。

## 枚举详解

### 1. `FunctionalConstraint` — 功能约束（FC）

文件：`com.ysh.jcms.info.FunctionalConstraint`

对应 IEC 61850-7-4 §5.4，定义了数据属性的语义类别。共 **11 个** FC 值：

| FC | 英文名                     | 中文名     | 可写 | 说明                        |
| -- | ----------------------- | ------- | -- | ------------------------- |
| ST | Status information      | 状态信息    | 否  | 状态量（开关位置、告警等），可读/替代/报告/日志 |
| MX | Measurands              | 测量值     | 否  | 模拟量遥测（电流、电压等）             |
| SP | Setting (outside SG)    | 设置值（组外） | 是  | 定值参数，立即生效                 |
| SV | Substitution            | 替代值     | 是  | 替代实际过程值                   |
| CF | Configuration           | 配置      | 是  | 配置参数（死区、系数等）              |
| DC | Description             | 描述      | 是  | 描述信息（铭牌、文本等）              |
| SG | Setting group           | 设置组     | 否  | 定值组当前活动值                  |
| SE | Setting group editable  | 设置组可编辑  | 是  | 定值组编辑缓冲区                  |
| SR | Service response        | 服务响应    | 否  | 服务跟踪数据                    |
| OR | Operate received        | 操作接收    | —  | 控制操作结果                    |
| BL | Blocking                | 阻塞      | 是  | 阻塞值更新                     |
| EX | Extended definition     | 扩展定义    | 否  | 应用命名空间                    |
| XX | All FCs (service param) | 全部 FC   | —  | 仅用于 FCD，表示全部 FC           |

每个枚举值包含 **6 个字段**：中英文语义说明、中英文允许操作说明、中英文初始值说明。

***

### 2. `CmsServiceInfo` — 服务定义

文件：`com.ysh.jcms.info.CmsServiceInfo`

涵盖 DL/T 2811 第 8 章 **所有**已实现的服务，按功能分组：

| 组             | 章节   | 数量 | 包含服务                                                                                  |
| ------------- | ---- | -- | ------------------------------------------------------------------------------------- |
| Association   | 8.2  | 4  | associate, release, abort, negotiate                                                  |
| Directory     | 8.3  | 6  | server-dir, ld-dir, ln-dir, all-data, all-def, all-cb                                 |
| Data Access   | 8.4  | 4  | get/set-data-values, data-dir, get-data-def                                           |
| Data Set      | 8.5  | 5  | get/set-dataset-values, create/delete-dataset, get-dataset-dir                        |
| Setting Group | 8.6  | 6  | select-active/edit-sg, set/get-edit-sg, confirm-edit-sg, get-sgcb-values              |
| Reporting     | 8.7  | 5  | report, get/set-brcb-values, get/set-urcb-values                                      |
| Logging       | 8.8  | 5  | get/set-lcb-values, query-log-time/after, get-log-status                              |
| GOOSE         | 8.9  | 5  | send-goose, get-go-ref, get-goose-elem, get/set-gocb-values                           |
| Sampled Value | 8.10 | 3  | send-msv, get/set-msvcb-values                                                        |
| Control       | 8.11 | 7  | select, select-with-value, operate, cancel, cmd-term, time-act-ope, time-act-ope-term |
| File          | 8.12 | 5  | get/set/delete-file, get-file-attrs, get-file-dir                                     |
| RPC           | 8.13 | 5  | rpc-if-dir/method-dir, rpc-if-def/method-def, rpc-call                                |
| Test          | 8.14 | 1  | test                                                                                  |

每个枚举值包含 6 个字段：

- **CLI 名称**：命令行使用的短名（如 `server-dir`、`get-data-values`）
- **标准章节号**：对应 DL/T 2811 的章节（如 `8.3.1`）
- **服务代码**：协议层使用的字节码
- **中英文服务名**
- **中英文功能描述**

提供按 CLI 名称和服务代码两种查询方式：`byName("server-dir")`、`byCode(0x50)`。

***

### 3. `CmsCdcInfo` — 公用数据类（CDC）

文件：`com.ysh.jcms.info.CmsCdcInfo`

对应 IEC 61850-7-3，定义了 **35 个** CDC：

| 分组      | CDC                                              | 用途              |
| ------- | ------------------------------------------------ | --------------- |
| **状态量** | SPS, DPS, INS, ACT, ACD, SEC, BCR                | 开关位置、整数状态、保护动作等 |
| **控制量** | SPC, DPC, INC, BAC, ISC                          | 遥控分合、可调整数、步进控制等 |
| **遥测量** | MV, CMV, WYE, DEL, SEQ, HMV, HWYE, HDEL          | 模拟量测量           |
| **采样值** | SAV, ISAV                                        | 采样值数据           |
| **定值**  | SPG, ING, ASG, CURVE, VSS, LPL, CSG              | 各种类型定值          |
| **控制块** | BRCB, URCB, LCB, SGCB, MSVCB, USVCB, GOOSE, GSSE | 各类控制块           |
| **其他**  | LLN0, LOG, LOCAL                                 | LN0、日志、本地操作     |

每个枚举值包含中英文名和简短描述。

***

### 4. `CmsLnInfo` — 逻辑节点（LN）

文件：`com.ysh.jcms.info.CmsLnInfo`

对应 IEC 61850-7-4，定义了 **80+** 个逻辑节点，按 **LnClass** 分组：

| LnClass | 分组名    | 示例 LN                            |
| ------- | ------ | -------------------------------- |
| **L**   | 系统逻辑节点 | LPHD（物理装置）、LLN0（LN零）             |
| **P**   | 保护功能   | PDIS（距离保护）、PIOC（瞬时过流）、PTOC（延时过流） |
| **R**   | 保护相关   | RDRE（扰动记录）、RREC（自动重合闸）、RSYN（同期）  |
| **C**   | 控制     | CSWI（开关控制器）、CILO（联锁）             |
| **G**   | 通用引用   | GGIO（通用I/O）                      |
| **I**   | 接口和存档  | IHMI（人机接口）、ITCI（远方控制接口）          |
| **A**   | 自动控制   | ATCC（自动调分接开关）                    |
| **M**   | 计量和测量  | MMXU（测量）、MMTR（计量）                |
| **S**   | 传感器监视  | SIMG（绝缘气体监视）                     |
| **X**   | 开关设备   | XCBR（断路器）、XSWI（隔离开关）             |
| **T**   | 仪用互感器  | TCTR（电流互感器）、TVTR（电压互感器）          |
| **Y**   | 电力变压器  | YPTR（电力变压器）、YLTC（分接开关）           |
| **Z**   | 其他电力设备 | ZBAT（电池）、ZLIN（架空线）、ZMOT（电动机）     |

提供按名称和按分组两种查询方式：`byName("PDIS")`、`byClass(LnClass.P)`。

***

### 5. `CmsDataTypeInfo` — 数据类型信息

文件：`com.ysh.jcms.info.CmsDataTypeInfo`

涵盖 DL/T 2811 ASN.1 模块中定义的所有 **数据类型**，约 **40+** 个条目，按用途分组：

| 分组                 | 示例类型                                                  | ASN.1 概要              |
| ------------------ | ----------------------------------------------------- | --------------------- |
| **基础数值**（§7.1）     | BOOLEAN, INT8\~64, INT8U\~64U, FLOAT32/64             | 约束整数、定长字节串            |
| **字符串**（§7.1.5）    | VisibleString, OCTET STRING, UTF8String, BIT STRING   | 长度+内容                 |
| **对象引用**（§7.3）     | ObjectName, ObjectReference, SubReference, EntryId    | 定长/范围约束 VisibleString |
| **时间**（§7.2）       | UtcTime, BinaryTime, TimeQuality                      | 定长字节串                 |
| **复合类型**（§7.3/7.5） | Originator, Check, Quality, Dbpos, PhyComAddr         | SEQUENCE 或 BIT STRING |
| **数据值**（§7.7）      | Data, DataDefinition                                  | CHOICE of 24 种类型      |
| **控制块**（§7.6）      | BRCB, URCB, LCB, SGCB, GoCB, MSVCB                    | SEQUENCE              |
| **错误/枚举**          | ServiceError, AddCause, TriggerConditions, ReasonCode | 约束整数或 BIT STRING      |

每个枚举值包含：类型名、标准章节号、中英文描述、ASN.1 编码概要。

***

## 使用场景

这些枚举在整个项目中被广泛用于：

1. **CLI 参数校验**：用户输入的服务名 → `CmsServiceInfo.byName()` 校验
2. **报文解析提示**：解码后的数据类型 → `CmsDataTypeInfo.byTypeName()` 查说明
3. **目录展示**：从 SCD 文件解析的 LN/CDC/FC → 映射到 `CmsLnInfo`/`CmsCdcInfo`/`FunctionalConstraint` 展示中英文名
4. **自动补全**：CLI 的 Tab 补全（`--acsi brcb` 等）

***

## 与上层模块的关系

| 模块                     | 依赖 info 的原因                 |
| ---------------------- | --------------------------- |
| **jcms-app (console)** | CLI 帮助信息、参数校验、输出格式化         |
| **jcms-app (handler)** | 服务分发时校验服务名和参数合法性            |
| **jcms-svc**           | 引用服务代码和数据类型信息（编译时常量）        |
| **jcms-utils/scl**     | SCD 文件解析后映射 LN/CDC/FC 到中英文名 |


# jcms-scl — SCD/ICD 文件解析模块

## 职责

jcms-scl 负责解析 **SCD（Substation Configuration Description）** 和 **ICD（IED Capability Description）** 文件——这些是基于 IEC 61850 SCL（Substation Configuration Language）标准的 XML 文件，描述了变电站的一次设备拓扑、IED 能力、通信配置、数据类型模板等完整信息。

一句话概括：**SCL XML → Java POJO 模型，让 CMS 服务端能读懂变电站配置**。

## 架构概览

```
              ┌─────────────────────────────────────┐
              │         SCL XML 文件（.scd/.icd）     │
              └──────────────────┬──────────────────┘
                                 │
                          ┌──────▼──────┐
                          │  SclReader   │  ← StAX 流式解析
                          └──────┬──────┘
                                 │
                          ┌──────▼──────┐
                          │ SclDocument  │  ← 顶层 POJO
                          └──────┬──────┘
         ┌───────────────────────┼───────────────────────┐
         ▼                       ▼                       ▼
   ┌──────────┐          ┌──────────────┐       ┌────────────────┐
   │ SclRef   │          │  Navigator   │       │ Convert 系     │
   │ (引用键) │◄─────────│ (引用导航器)  │──────►│ DataValueResolver│
   └──────────┘          └──────────────┘       │ DataConverter   │
         ▲                    │                 │ TypeMapper      │
         │                    ▼                 │ DataWriterResolver│
   ┌──────────┐          ┌──────────────┐       └────────────────┘
   │SclRefParser│         │ TypeChain    │
   └──────────┘          │ CmsDataTypeMap│
                          └──────────────┘
         ┌──────────────────────────────────────────────┐
         │              state 系                         │
         │  CbStateManager (CbStateStore / Association) │
         │  (六种控制块运行时状态，分层存储)              │
         └──────────────────────────────────────────────┘
```

## 子包结构

```
scl/
├── SclDocument.java        — 顶层文档模型
├── SclReader.java          — 顶层入口
├── SclParseException.java  — 解析异常
├── PLAN.md                 — 开发计划
│
├── model/                  — SCL 模型 POJO（≈ 60 个类）
│   ├── header/             — <Header> 及其子元素
│   ├── substation/         — <Substation> 一次设备拓扑
│   ├── communication/      — <Communication> 通信配置
│   ├── ied/                — <IED> 智能电子设备
│   ├── template/           — <DataTypeTemplates> 数据类型模板
│   ├── instance/           — <DOI>/<DAI>/<SDI> 实例化数据
│   ├── control/            — <ReportControl>/<GSEControl>/<LogControl>/<SampledValueControl>
│   ├── input/              — <Inputs>/<ExtRef>/<FCDA>
│   ├── SclPrivate.java     — <Private> 扩展元素
│   ├── SclText.java        — <Text> 文本元素
│   └── SclVal.java         — <Val> 值元素
│
├── reader/                 — StAX 解析器（每节一个）
│   ├── SclReader.java      — 主解析器（含共享工具方法）
│   ├── SclHeaderParser.java
│   ├── SclSubstationParser.java
│   ├── SclCommunicationParser.java
│   ├── SclIedParser.java
│   └── SclTemplateParser.java
│
├── ref/                    — 引用解析
│   ├── SclRef.java         — 引用模型（IED/LD/LN.DO.SDI.DA）
│   └── SclRefParser.java   — 引用字符串解析器
│
├── navigate/               — 模型导航
│   ├── Navigator.java      — 引用 → 模型元素导航器
│   ├── TypeChain.java      — 类型链（LNType → DOType → DAType → bType）
│   └── CmsDataTypeMap.java — bType ↔ CmsDataDefinition selector 映射
│
├── convert/                — SCL → 协议类型转换
│   ├── DataConverter.java           — DataValueEntry → CmsData
│   ├── DataValueResolver.java       — 引用 → DataValueEntry
│   ├── DataValueEntry.java          — (ref, val, bType) 三元组
│   ├── DataDefinitionResolver.java  — 引用 → DataDefinitionEntry
│   ├── DataDefinitionEntry.java     — 类型定义条目
│   ├── DataWriterResolver.java      — 写值解析器
│   ├── ValueMapper.java             — 值格式转换
│   ├── TypeMapper.java              — bType → CmsType 实例
│   ├── DataSetResolver.java         — 数据集解析
│   └── CbConverter.java             — 控制块转换
│
└── state/                  — 运行时状态（六种控制块分层存储）
    ├── CbStateManager.java      — 统一门面（RCB/LCB/GOCB/MSVCB + ASSOCIATION）
    ├── CbStateStore.java        — RUNTIME 层泛型存储（进程内，按 ref）
    └── CbAssociationStore.java  — ASSOCIATION 层泛型存储（按会话隔离）
```

---

## 核心类详解

### 1. `SclDocument` — 顶层文档模型

包路径：`com.ysh.jcms.utils.scl.SclDocument`

```
SclDocument
├── fileType          (SCD / ICD / CID / UNKNOWN)
├── originalFilePath  (源文件路径)
├── header            (SclHeader)             ← 文件头
├── substation        (SclSubstation)          ← 一次设备拓扑
├── communication     (SclCommunication)       ← 通信配置
├── ieds              (List<SclIED>)           ← 智能电子设备
├── dataTypeTemplates (SclDataTypeTemplates)   ← 数据类型模板
└── unsupportedElements (List<String>)         ← 未支持的顶层元素
```

支持的文件类型：
- **SCD** — 全站配置描述，包含 Substation + Communication + IED + DataTypeTemplates
- **ICD** — IED 能力描述，仅包含单个 IED 及其 DataTypeTemplates
- **CID** — 实例化配置描述

---

### 2. `SclReader` — SCL 解析器

包路径：`com.ysh.jcms.utils.scl.reader.SclReader`（`reader` 子包下的已实现版本）

使用 **StAX**（`XMLStreamReader`）流式解析，避免 DOM 方式的大内存占用。解析流程：

```
read(path)
    │
    └─ read(inputStream)
         │
         └─ parseDocument(reader)
              │
              └─ parseSclChildren(reader, document)
                   │
                   ├─ <Header>        → SclHeaderParser.parse()
                   ├─ <Substation>    → SclSubstationParser.parse()
                   ├─ <Communication> → SclCommunicationParser.parse()
                   ├─ <IED>           → SclIedParser.parse()
                   ├─ <DataTypeTemplates> → SclTemplateParser.parse()
                   └─ 未知元素 → document.addUnsupportedElement()
```

共享工具方法（`SclReader` 中作为 `static` 方法提供）：
- `getAttr()` — 获取 XML 属性
- `boolAttr()` / `intAttr()` — 类型化属性获取
- `skipElement()` — 跳过未知元素
- `elementText()` / `parseSimpleElementText()` — 文本内容读取
- `parseTextChild()` / `parseValChild()` — 子元素解析

---

### 3. Model 包 — SCL 模型 POJO

约 **60 个** POJO 类，覆盖 IEC 61850-6 SCL 的全部核心元素。按命名空间组织：

| 子包 | 根元素 | 主要内容 |
|------|--------|----------|
| **header** | `<Header>` | id, version, revision, history |
| **substation** | `<Substation>` | VoltageLevel → Bay → ConductingEquipment, ConnectivityNode, Function, PowerTransformer, TransformerWinding, TapChanger, Line, Process, GeneralEquipment 等 |
| **communication** | `<Communication>` | SubNetwork → ConnectedAP → Address, PhysConn, GSE, SMV |
| **ied** | `<IED>` | AccessPoint → Server → LDevice → LN/LN0 → Services / ReportSettings / GSESettings / AccessControl / Association |
| **template** | `<DataTypeTemplates>` | LNodeType, DOType, DAType, EnumType, SDO, DA, BDA, EnumVal, ProtNs |
| **instance** | `<DOI>` | DOI → SDI → DAI → Val（实例化数据值） |
| **control** | — | ReportControl, GSEControl, LogControl, SampledValueControl |
| **input** | `<Inputs>` | ExtRef, FCDA, DataSet |

---

### 4. `SclRef` / `SclRefParser` — 引用体系

包路径：`com.ysh.jcms.utils.scl.ref`

这是整个 SCL 模块的**索引键**，引用格式：

```
[IEDName/]LDInst/LNName[.DO[.SDI]...[.DA]][FC]
```

示例：
```
E1Q1SB1/C1/MMXU1.Volts.sVC.offset
C_B5041X/LD0/LLN0.Mod.stVal[ST]
LD0/LLN0
```

`SclRef` 不可变对象，提供层级判断方法：
- `isLnLevel()` — LN 级引用
- `isDoLevel()` — DO 级引用
- `isDaLevel()` — DA 级引用（含 SDI 链）
- `fullReference()` — 完整引用字符串

`SclRefParser.parse()` 使用正则表达式解析引用字符串，支持带 IED 前缀和 FC 后缀。

---

### 5. `Navigator` — 引用导航器

包路径：`com.ysh.jcms.utils.scl.navigate.Navigator`

核心功能：**SclRef → 模型元素**。

```java
Navigator nav = Navigator.go(document, "E1Q1SB1/C1/MMXU1.Volts.sVC.offset");
if (nav.isValid()) {
    SclLN ln = nav.ln();          // MMXU1
    SclDOI doi = nav.doi();       // Volts 的 DOI
    SclSDI sdi = nav.sdi();       // sVC 的 SDI
    SclDAI dai = nav.dai();       // offset 的 DAI
    String val = nav.daiValue();  // 值
}
```

导航链路：
```
document → IED → LDevice → LN → DOI → (SDI)* → DAI
```

提供多个 `go()` 重载，支持文档级、IED 级、字符串引用等多种入口。

---

### 6. `TypeChain` + `CmsDataTypeMap` — 类型解析

包路径：`com.ysh.jcms.utils.scl.navigate`

`TypeChain` 通过 `DataTypeTemplates` 追溯类型链：

```
LNType → DOType → DAType → bType
```

```java
TypeChain chain = TypeChain.of(dataTypeTemplates);
String bType = chain.resolveBType(lnTypeId, "Volts.sVC.offset");
// → "FLOAT64"
```

`CmsDataTypeMap` 将 bType 字符串映射为 `CmsDataDefinition` 的 CHOICE selector：
- `"INT32"` → `SEL_INT32`
- `"BOOLEAN"` → `SEL_BOOLEAN`
- `"VISSTRING255"` → `SEL_VISIBLE_STRING`
- 等等

---

### 7. Convert 系 — SCL → 协议类型转换

| 类 | 职责 |
|----|------|
| **`DataValueResolver`** | 按引用从 SCL 模型中解析 `DataValueEntry(ref, val, bType)`，支持 FC 过滤 |
| **`DataConverter`** | `DataValueEntry` → `CmsData` CHOICE 协议类型 |
| **`DataDefinitionResolver`** | 按引用解析 `DataDefinitionEntry`，含 CDC 类型和子结构 |
| **`TypeMapper`** | `(bType, value)` → `CmsType` 实例（如 `"FLOAT32" + "3.14"` → `CmsFloat32`） |
| **`ValueMapper`** | SCL 字符串值 → 协议类型的值格式转换 |
| **`DataWriterResolver`** | 写值时的解析器（SetDataValues 路径） |
| **`DataSetResolver`** | 从 SCL 模型中解析数据集成员定义 |
| **`CbConverter`** | 控制块（BRCB/URCB/LCB/SGCB）的 SCL → 协议类型转换 |

典型流程：
```java
// GetDataValues 服务流程
DataValueEntry dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
// dv.val() → "false", dv.bType() → "BOOLEAN"

CmsData data = DataConverter.toCmsData(dv);
// data.choice → CHOICE_BOOLEAN, data.alt_boolean.value() → false
```

---

### 8. State 系 — 控制块运行时状态

标准 7.6.1 定义六种控制块：BRCB、URCB、LCB、SGCB、GoCB、MSVCB。运行时状态按字段生命周期分三层存储（`@CbField` 标注在 jcms-core 的控制块类上）：

| 层 | 类 | 生命周期 | 说明 |
|----|----|---------|------|
| ENGINEERING | 无存储（读 SCL 模型） | 跨重启 | 只读基底，Set 覆盖值写 RUNTIME 层 |
| RUNTIME | `CbStateStore` | 进程内 | `CbStateManager.RCB/LCB/GOCB/MSVCB`，Set 写入、Get 优先读取 |
| ASSOCIATION | `CbAssociationStore` | 本次连接 | `CbStateManager.ASSOCIATION`，URCB per-association，连接断开清除 |

- `CbStateManager.RCB` 同时承载 BRCB/URCB；URCB 的 `rptEna`/`sqNum`/`gi` 走 ASSOCIATION 层（8.7.4 每个关联一个实例）
- SGCB 是会话级状态，由 jcms-app 的 `SgSessionState` 管理，字段生命周期已标注在 `CmsSgcb`
- 状态覆盖 SCL 静态默认值，运行时修改生效到服务端生命周期结束

---

## 使用流程（完整链路）

```
读取 SCD 文件:
  SclReader.read("config/sample-scd-full.scd") → SclDocument
                                                    │
按引用查值:
  Navigator.go(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal")
  → 定位到 DAI → DataValueResolver.resolve() → DataValueEntry
                                                    │
转协议类型:
  DataConverter.toCmsData(dv) → CmsData（含 CHOICE 选择器 + 值）
                                                    │
编码输出:
  CmsData.encode() → byte[]（PER 编码后的响应报文）
```

## 与上层模块的关系

| 模块 | 使用方式 |
|------|---------|
| **jcms-app/server** | 启动时通过 `SclReader` 加载 SCD 文件，构建 `SclDocument` 供后续服务使用 |
| **jcms-svc** | 不直接依赖，但服务的响应数据来源于 SCL 模型的值 |
| **jcms-core/data** | `DataConverter` / `TypeMapper` 将 SCL 值转换为 `CmsData` 协议类型 |
| **jcms-app/handler** | 各服务处理器通过 `DataValueResolver` / `DataDefinitionResolver` 查询 SCL 模型 |

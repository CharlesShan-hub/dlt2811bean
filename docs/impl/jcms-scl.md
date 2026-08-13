# jcms-scl — SCD/ICD 文件解析模块

## 职责

jcms-scl 负责解析 **SCD（Substation Configuration Description）** 和 **ICD（IED Capability Description）** 文件——这些是基于 IEC 61850 SCL（Substation Configuration Language）标准的 XML 文件，描述了变电站的一次设备拓扑、IED 能力、通信配置、数据类型模板等完整信息。

一句话概括：**SCL XML → Java POJO 模型，让 CMS 服务端能读懂变电站配置**。

位置：`jcms/jcms-utils` 模块，包 `com.ysh.jcms.utils.scl`。依赖 jcms-core 的协议类型（`CmsData` / `CmsBrcb` 等），被 jcms-app 的 handler / SclManager 使用。

## 架构概览

```
              ┌─────────────────────────────────────┐
              │         SCL XML 文件（.scd/.icd）     │
              └──────────────────┬──────────────────┘
                                 │
                          ┌──────▼──────┐
                          │  SclReader   │  ← StAX 流式解析（含 scanAccessPoints 轻量扫描）
                          └──────┬──────┘
                                 │
                          ┌──────▼──────┐
                          │ SclDocument  │  ← 顶层 POJO（惰性索引 ied() / ldNames()）
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
         │              service 层（8.x handler 复用）    │
         │  SclDirectoryService / SclAllValuesService   │
         │  SclDataDirectoryService / SclDatasetService │
         │  SclControlBlockService / SclAccessPointService│
         └──────────────────────────────────────────────┘
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
├── SclParseException.java  — 解析异常
│
├── model/                  — SCL 模型 POJO（≈ 80 个类）
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
├── reader/                 — StAX 解析器（主 + 9 个分节解析器）
│   ├── SclReader.java      — 主解析器（入口 + 共享工具方法 + 轻量扫描）
│   ├── SclHeaderParser.java        — <Header> / <Hitem>
│   ├── SclSubstationParser.java    — <Substation> 容器结构
│   ├── SclEquipmentParser.java     — 一次设备（ConductingEquipment/Transformer 等）
│   ├── SclFunctionParser.java      — 功能节点（Function/SubFunction/EqFunction 等）
│   ├── SclCommunicationParser.java — <Communication>
│   ├── SclIedParser.java           — IED 结构骨架（AccessPoint/Server/LDevice/LN）
│   ├── SclServicesParser.java      — IED 下的 <Services>（ReportSettings/GSESettings）
│   ├── SclInstanceParser.java      — LN 下实例数据（DataSet/FCDA/Inputs/ExtRef/DOI/SDI/DAI）
│   ├── SclControlBlockParser.java  — LN 下控制块（Report/Log/GOOSE/SampledValue）
│   └── SclTemplateParser.java      — <DataTypeTemplates>
│
├── ref/                    — 引用解析
│   ├── SclRef.java         — 引用模型（IED/LD/LN.DO.SDI.DA + FC，Builder 支持）
│   └── SclRefParser.java   — 引用字符串解析器（正则，isValid 校验）
│
├── navigate/               — 模型导航
│   ├── Navigator.java      — 引用 → 模型元素导航器（含 AP 作用域与部分导航）
│   ├── TypeChain.java      — 类型链（LNType → DOType → DAType → bType，含 SDO/SDI 分支）
│   └── CmsDataTypeMap.java — bType → CmsDataDefinition selector / CmsType 映射
│
├── convert/                — SCL → 协议类型转换
│   ├── DataValueResolver.java      — 引用 → DataValueEntry（Navigator + TypeChain 积木）
│   ├── DataConverter.java          — DataValueEntry → CmsData（含 autoDetect 类型自推断）
│   ├── DataValueEntry.java         — (ref, val, bType) 三元组
│   ├── DataDefinitionResolver.java — 引用 → DataDefinitionEntry（含 FC 过滤）
│   ├── DataDefinitionEntry.java    — 类型定义条目（ref + cdcType + 结构）
│   ├── DataWriterResolver.java     — 写值解析器（SetDataValues 路径，可虚拟创建 DAI）
│   ├── ValueMapper.java            — bType ↔ Java 原生类型 / 枚举 ord↔label
│   ├── TypeMapper.java             — bType → CmsType 实例
│   ├── DataSetResolver.java        — FCDA ↔ 引用字符串 互转
│   └── CbConverter.java            — 控制块 → CmsCbValueChoice
│
├── service/                — 8.x 服务逻辑（handler 复用层，本模块新增核心）
│   ├── SclAccessPointService.java  — sapRef → IED + AccessPoint 解析（Associate 用）
│   ├── SclDirectoryService.java    — 服务器/逻辑设备/逻辑节点目录（8.3.1/8.3.2/8.3.3）
│   ├── SclAllValuesService.java    — 全部数据值/定义/控制块值（8.3.4/8.3.5/8.3.6）
│   ├── SclDataDirectoryService.java— 数据目录（8.4.3，LN/DO/SDO 三级，合并实例+模板）
│   ├── SclDatasetService.java      — 数据集引用解析 / FCDA 转换（8.5 系列）
│   └── SclControlBlockService.java — 控制块 ref 解析 + 运行时状态 overlay（8.7/8.8/8.9/8.10）
│
└── state/                  — 运行时状态（六种控制块分层存储）
    ├── CbStateManager.java      — 统一门面（RCB/LCB/GOCB/MSVCB + ASSOCIATION）
    ├── CbStateStore.java        — RUNTIME 层泛型存储（进程内，按 ref，ConcurrentHashMap）
    └── CbAssociationStore.java  — ASSOCIATION 层泛型存储（按会话隔离，连接断开清除）
```

---

## 核心类详解

### 1. `SclDocument` — 顶层文档模型

包路径：`com.ysh.jcms.utils.scl.SclDocument`

```
SclDocument
├── fileType          (SCD / ICD / CID / UNKNOWN)
├── originalFilePath  (源文件路径)
├── xmlns / xsiSchemaLocation（命名空间，默认 IEC 61850-6 SCL）
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

两个惰性索引（首次查询建立，`addIed` 时失效）：
- `ied(name)` — IED 名 → IED 的 O(1) 查找（`iedIndex`）
- `ldNames()` — 全站逻辑设备实例名列表（`ldNamesCache`）

> 注意：`fileType` 目前仅在 `version=2007 && revision=B` 时置为 SCD，其余为 UNKNOWN——这是改进点（见文末）。

---

### 2. `SclReader` — SCL 解析器

包路径：`com.ysh.jcms.utils.scl.reader.SclReader`

使用 **StAX**（`XMLStreamReader`）流式解析，避免 DOM 方式的大内存占用。解析流程：

```
read(path)
    │
    └─ read(inputStream)
         │  （XMLInputFactory 禁用 DTD + 外部实体，防 XXE）
         └─ parseDocument(reader)
              │
              └─ parseSclChildren(reader, document)
                   │
                   ├─ <Header>        → SclHeaderParser.parse()
                   ├─ <Substation>    → SclSubstationParser.parse()
                   ├─ <Communication> → SclCommunicationParser.parse()
                   ├─ <IED>           → SclIedParser.parse()
                   ├─ <DataTypeTemplates> → SclTemplateParser.parse()
                   └─ 未知元素 → document.addUnsupportedElement() + skipElement()
```

**轻量扫描**：`scanAccessPoints(Path/InputStream)` 只读 IED / AccessPoint 的 name 属性，不构建完整模型，几百 IED 几十 MB 的 SCD 也能秒级返回 `IED 名 → AP 名列表`（供 `ap-dir` 控制台命令 / Associate 候选列表）。

共享工具方法（`SclReader` 中作为 `static` 方法提供）：
- `getAttr()` / `boolAttr()` / `intAttr()` — 类型化属性获取
- `skipElement()` — 跳过未知元素
- `elementText()` / `parseSimpleElementText()` — 文本内容读取
- `parseTextChild()` / `parseValChild()` — 子元素解析

---

### 3. Model 包 — SCL 模型 POJO

约 **80 个** POJO 类（Lombok `@Getter` / `@Accessors(fluent)`），覆盖 IEC 61850-6 SCL 的核心元素。按命名空间组织：

| 子包 | 根元素 | 主要内容 |
|------|--------|----------|
| **header** | `<Header>` | id, version, revision, toolID, nameStructure, Hitem |
| **substation** | `<Substation>` | VoltageLevel → Bay → ConductingEquipment, ConnectivityNode, Function/SubFunction, PowerTransformer → TransformerWinding → TapChanger, Line, Process, GeneralEquipment, SubEquipment, Terminal, LNode, Voltage |
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

`SclRef` 不可变对象，提供层级判断方法（`isLnLevel` / `isDoLevel` / `isDaLevel`）与引用组合（`lnReference` / `doReference` / `daReference` / `fullReference`），另有静态 Builder（`SclRef.ld("LD0").lnClass("MMXU")...`）与 `equals`/`hashCode`（不含 FC）。

`SclRefParser.parse()` 用双正则（带 IED / 不带 IED）解析引用字符串，`isValid()` 供各处先校验后解析。

---

### 5. `Navigator` — 引用导航器

包路径：`com.ysh.jcms.utils.scl.navigate.Navigator`

核心功能：**SclRef → 模型元素**，链路 `document → IED → LDevice → LN → DOI → (SDI)* → DAI`。

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

特性：
- **部分导航**：DO 只在模板中定义（实例无 DOI，如 `Beh`）或 SDI/DAI 缺实例时，仍返回"部分 Navigator"（有 LN/DOI，缺低层），让下游走模板查找，而不是直接判无效。
- **AP 作用域**：`go(doc, ap, ref)` 系列限定在指定 AccessPoint 下查找 LD。
- **静态辅助**：`findLd(ap, ldInst)`、`resolveLns(ied, ap, ldName, lnReference)`（LD 名或 LN 引用 → LN 列表）、`findLdInst(ied, ln)`（LN 反查 LD 实例名）。

---

### 6. `TypeChain` + `CmsDataTypeMap` — 类型解析

包路径：`com.ysh.jcms.utils.scl.navigate`

`TypeChain` 通过 `DataTypeTemplates` 追溯类型链，Step Builder 风格：

```
LNodeType → DO → DOType → DA → bType
              └→ SDO → DOType → DA        （SDO 分支）
              └→ DA(Struct) → DAType → BDA （SDI 分支）
```

```java
TypeChain chain = TypeChain.of(dataTypeTemplates);
String bType = chain.resolveBType(lnTypeId, "Volts.sVC.offset");
// → "FLOAT64"
```

`CmsDataTypeMap` 纯查表、零状态，负责两件事：
- bType → `CmsDataDefinition` 的 CHOICE selector（如 `"INT32"` → `SEL_INT32`；无法识别兜底 `SEL_BOOLEAN`）
- bType → `CmsType` 映射 + `visibleStringLength()`（VisString255 → 255 等长度约束）

---

### 7. Convert 系 — SCL → 协议类型转换

| 类 | 职责 |
|----|------|
| **`DataValueResolver`** | 按引用从 SCL 模型中解析 `DataValueEntry(ref, val, bType)`（基于 Navigator + TypeChain 积木） |
| **`DataConverter`** | `DataValueEntry` → `CmsData` CHOICE 协议类型；另有 `autoDetect(val)` 无 bType 时按值自推断类型 |
| **`DataDefinitionResolver`** | 按引用解析 `DataDefinitionEntry`，含 CDC 类型、结构定义、FC 过滤 |
| **`TypeMapper`** | `(bType, value)` → `CmsType` 实例 |
| **`ValueMapper`** | bType ↔ Java 原生类型；枚举值 ord ↔ label 双向查找 |
| **`DataWriterResolver`** | 写值解析器（SetDataValues 路径），DAI 缺失时可虚拟创建，返回服务错误码 |
| **`DataSetResolver`** | FCDA ↔ 完整引用字符串互转（如 `LD/LN.DO.DA`） |
| **`CbConverter`** | 控制块（BRCB/URCB/LCB/GOCB/MSVCB）SCL → `CmsCbValueChoice` |

典型流程：
```java
// GetDataValues 服务流程
DataValueEntry dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
// dv.val() → "false", dv.bType() → "BOOLEAN"

CmsData data = DataConverter.toCmsData(dv);
// data.choice → CHOICE_BOOLEAN, data.alt_boolean.value() → false
```

---

### 8. Service 层 — 8.x 服务逻辑（handler 复用）

各方法返回**完整结果列表**，分页（referenceAfter / pageSize）由 handler 层处理；静态工具类，无状态。

| 类 | 服务 | 说明 |
|----|------|------|
| **`SclAccessPointService`** | Associate | `resolve(scl, sapRef)` 解析 `IED[/AP]`（缺省 AP 为 S1）；`resolveDefault(scl)` 取首个带 AP 的 IED |
| **`SclDirectoryService`** | 8.3.1/8.3.2/8.3.3 | 服务器目录（AP 下 LD 实例名）；逻辑设备目录（ldName 非空 → LN 短名，空 → 全站 `LD/LN`）；逻辑节点目录（按 ACSI 类收集 DO 完整引用/数据集名/控制块名，含 SDO 递归，带环检测） |
| **`SclAllValuesService`** | 8.3.4/8.3.5/8.3.6 | 全部数据值（展开到 DA 级，递归 SDO，按 FC 过滤）；全部数据定义（DO 级含 CDC + structure）；全部控制块值（按 ACSI 类，**overlay 运行时状态**，与 GetXxxCBValues 行为对齐） |
| **`SclDataDirectoryService`** | 8.4.3 | 数据目录三级：LN 级列 DO、DO 级列 DA(含 fc)/SDI、SDO 级列 DA；实例（DOI/DAI）与模板条目**合并去重** |
| **`SclDatasetService`** | 8.5 系列 | `resolveDataSet(ied, [ap,] ref)` → LD/LN/DataSet 三元组；`resolveLn`（创建场景，DataSet 可不存在）；`extractDsName`；`parseRefToFcda`（成员引用 → FCDA，回填 lnClass/prefix/inst） |
| **`SclControlBlockService`** | 8.7/8.8/8.9/8.10 | 按 ref 解析控制块：`resolveBrcb/urcb/lcb/gocb/msvcb`，合并 SCL 默认值 + **运行时状态 overlay**（`applyRuntimeState` / `overlayUrcbRuntime`）；GoCB/MSVCB 优先读 `CbStateManager` 缓存；GOCB/MSVCB 支持 LN 前缀匹配（如 `CTRL` → `CTRL1`） |

---

### 9. State 系 — 控制块运行时状态

标准 7.6.1 定义六种控制块：BRCB、URCB、LCB、SGCB、GoCB、MSVCB。运行时状态按字段生命周期分三层存储（`@CbField` 标注在 jcms-core 的控制块类上）：

| 层 | 类 | 生命周期 | 说明 |
|----|----|---------|------|
| ENGINEERING | 无存储（读 SCL 模型） | 跨重启 | 只读基底，Set 覆盖值写 RUNTIME 层 |
| RUNTIME | `CbStateStore` | 进程内 | `CbStateManager.RCB/LCB/GOCB/MSVCB`，Set 写入、Get 优先读取；客户端断开不丢，服务器重启丢失 |
| ASSOCIATION | `CbAssociationStore` | 本次连接 | `CbStateManager.ASSOCIATION`，按 sessionId 隔离，连接断开 `clearAssociation()` 清除 |

- `CbStateManager.RCB` 同时承载 BRCB/URCB；URCB 的 `rptEna`/`sqNum`/`gi` 走 ASSOCIATION 层（8.7.4 每个关联一个实例）
- SGCB 是会话级状态，由 jcms-app 的 `SgSessionState` 管理，字段生命周期已标注在 `CmsSgcb`
- 存储实现均为 `ConcurrentHashMap`（`CbStateStore` 按 ref；`CbAssociationStore` 双层按 session → ref），线程安全

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
| **jcms-app/node** | `SclManager` 启动时通过 `SclReader` 加载 SCD 文件，构建 `SclDocument` 供后续服务使用 |
| **jcms-app/handler** | 目录/数据/数据集/控制块各 handler 通过 **service 层**（`SclDirectoryService` / `SclAllValuesService` / `SclDataDirectoryService` / `SclDatasetService` / `SclControlBlockService`）查询 SCL 模型；`AssociateServer` 用 `SclAccessPointService` 解析访问点 |
| **jcms-app/console** | `ApDirHandler` 用 `SclReader.scanAccessPoints()` 轻量扫描 IED→AP 目录 |
| **jcms-core** | 反向依赖：jcms-scl 依赖 jcms-core 的协议类型（`CmsData` / `CmsBrcb` / `CmsFC` / `CmsServiceError` 等），用于转换与状态载体 |
| **jcms-svc** | 不直接依赖，服务响应数据来源于 SCL 模型的值 |

## 测试覆盖

`jcms-utils/src/test` 下已有测试（测试类齐全）：

- **reader**：`SclReaderTest`（完整文件解析 + scanLdLns 轻量扫描 + fileType 判定）
- **ref**：`SclRefParserTest`（各种引用格式）
- **navigate**：`NavigateTest`（引用导航）
- **convert**：`DataValueResolverTest`、`DataDefinitionResolverTest`、`DataConverterTest`、`TypeMapperTest`、`ValueMapperTest`、`DataSetResolverTest`、`CbConverterTest`、`DataWriterResolverTest`
- **service**：`SclDirectoryServiceTest`（8.3 目录）、`SclControlBlockServiceTest`（控制块 ref 解析 + 运行时 overlay）
- **state**：`CbStateStoreTest`（RUNTIME / ASSOCIATION 分层存储 + 门面生命周期）

---

## 改进记录（2026-08）

此前识别的改进点均已落地：

1. **fileType 按内容判定**：`SclReader.parseDocument` 统计内容结构，含 `<Substation>` → SCD、单 IED → ICD，不再依赖 2007B 版本号。
2. **调试输出清理**：`DataConverter` 的 `CmsPrinter.consoleOnly` 全部改为 slf4j（入口 debug、未知类型/异常 warn）。
3. **静默异常告警**：`SclControlBlockService` 11 处数字解析异常补 warn 日志，并修复 `buildUrcb` 对空引用的 NPE。
4. **LD 惰性索引**：`SclIED.lDevice()` 首次 O(AP×LD) 建立索引、之后 O(1)；`Navigator.findLd` 复用。
5. **轻量扫描扩展**：`SclReader.scanLdLns()`（IED/AP → LD/LN 秒级目录），与 `scanAccessPoints`/`read` 共用防 XXE 的 `createSafeFactory()`。
6. **清理历史计划文档**：删除源码目录内过期的 `PLAN.md`，后续方向并入本文档。
7. **service / state 补单测**：新增 3 个测试类（见上节），共 26 个用例。

仍可继续推进的方向：SCL 序列化回写、轻量扫描覆盖数据集/控制块候选、CID 与 ICD 细分、`findLdInst` 索引化。

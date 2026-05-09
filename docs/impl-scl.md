# SCL 模块实现

## 概述

SCL 模块实现了 IEC 61850-6（GB/T 42151.6）定义的变电站配置描述语言（SCL，System Configuration description Language）的 XML 文件解析。SCL 是一种基于 XML 的配置描述格式，用于在系统配置工具和 IED 配置工具之间交换 IED 能力描述和系统配置信息。

SCL 模块支持解析 SCD、ICD、CID 三种 SCL 文件类型，覆盖了 SCL 模型中的 5 大顶层元素（Header、Substation、Communication、IED、DataTypeTemplates），并提供了严格的验证模式以检查文件内容的合规性。

### 标准依据

| 标准 | 描述 |
|------|------|
| GB/T 42151.6 / IEC 61850-6:2009 | 电力自动化通信网络和系统 第6部分：配置描述语言 |
| DL/T 860（所有部分） | 电力自动化通信网络和系统 |
| GB/T 45906.3-2025 §5.3 | 引用说明：可直接使用 SCL 配置的 SCD、ICD、CID 等文件 |

### SCL 文件类型

| 类型 | 全称 | 用途 |
|------|------|------|
| **SCD** | Substation Configuration Description | 全站系统配置文件，包含 Substation、Communication、IED 等完整信息 |
| **ICD** | IED Capability Description | 装置能力描述文件（由厂商提供），仅含单 IED，不含 Substation |
| **CID** | Configured IED Description | 已配置的 IED 描述文件，含单 IED 的完整配置 |

### 代码结构

包名：`com.ysh.dlt2811bean.scl`

```
com.ysh.dlt2811bean.scl
├── SclReader.java              ← SCL 文件解析器（主入口）
├── SclDocument.java            ← SCL 文档模型（顶层容器）
└── model/
    ├── SclHeader.java          ← <Header> 元素
    ├── SclHitem.java           ← <History>/<Hitem> 元素
    ├── SclSubstation.java      ← <Substation> 元素
    ├── SclCommunication.java   ← <Communication> 元素
    ├── SclDataTypeTemplates.java ← <DataTypeTemplates> 元素
    └── SclIED.java             ← <IED> 元素
```

## 类型层次

### SCL 顶层文档结构

```
SCL (SclDocument)
├── Header (SclHeader)
│   ├── Text (String)
│   └── History / Hitem (SclHitem[])
├── Substation (SclSubstation)
├── Communication (SclCommunication)
├── IED (SclIED[])
└── DataTypeTemplates (SclDataTypeTemplates)
```

### Substation 层次

```
Substation (SclSubstation)
└── VoltageLevel (SclVoltageLevel[])
    ├── Voltage
    ├── Bay[]
    │   ├── ConductingEquipment[]
    │   │   ├── Terminal[]
    │   │   ├── SubEquipment[]
    │   │   └── LNode[]
    │   ├── ConnectivityNode[]
    │   └── LNode[]
    └── PowerTransformer[]
        ├── TransformerWinding[]
        │   └── Terminal[]
        └── LNode[]
```

### Communication 层次

```
Communication (SclCommunication)
└── SubNetwork (SclSubNetwork[])
    ├── BitRate
    └── ConnectedAP (SclConnectedAP[])
        ├── Address (SclAddress)
        │   └── P[] (key-value Map)
        ├── GSE (SclGSE[])
        ├── SMV (SclSMV[])
        └── PhysConn (SclPhysConn[])
```

### IED 层次

```
IED (SclIED)
├── Services (SclServices)
│   ├── DynAssociation, GetDirectory, ReadWrite, ...
│   ├── ConfDataSet, ConfReportControl, ConfLogControl, ...
│   ├── ReportSettings, GSESettings
│   └── maxNameLength
└── AccessPoint (SclAccessPoint[])
    └── Server (SclServer)
        └── LDevice (SclLDevice[])
            ├── LN0 (SclLN0)
            │   ├── DataSet[]
            │   │   └── FCDA[]
            │   ├── ReportControl[]
            │   │   ├── TrgOps
            │   │   ├── OptFields
            │   │   └── RptEnabled → ClientLN[]
            │   ├── LogControl[]
            │   │   └── TrgOps
            │   ├── GSEControl[]
            │   ├── SampledValueControl[]
            │   │   └── SmvOpts
            │   └── DOI[]
            │       ├── DAI[]
            │       └── SDI → DAI[]
            └── LN (SclLN[])
                ├── DOI[]
                └── Inputs → ExtRef[]
```

### DataTypeTemplates 层次

```
DataTypeTemplates (SclDataTypeTemplates)
├── LNodeType (SclLNodeType[])
│   └── DO (SclDO[])
├── DOType (SclDOType[])
│   ├── DA (SclDA[])
│   │   ├── name, type, bType, fc
│   │   └── dchg, qchg, dupd (TrgOp flags)
│   └── SDO (SclSDO[])
├── DAType (SclDAType[])
│   └── BDA (SclBDA[])
└── EnumType (SclEnumType[])
    └── EnumVal (SclEnumVal[])
        └── ord, value
```

## 使用方式

### 基本用法

```java
// 从文件路径读取
SclReader reader = new SclReader();
SclDocument scl = reader.read("path/to/file.scd");

// 从输入流读取
try (InputStream is = new FileInputStream("path/to/file.icd")) {
    SclDocument scl = reader.read(is, "file.icd");
}

// 启用严格模式
reader.setStrictMode(true);
SclDocument scl = reader.read("path/to/file.cid");
```

### 读取后的访问

```java
// 文件信息
SclDocument.SclFileType fileType = scl.getFileType();
String filePath = scl.getOriginalFilePath();

// Header
SclHeader header = scl.getHeader();
String headerId = header.getId();
List<SclHitem> history = header.getHistory();

// IED 信息
List<SclIED> ieds = scl.getIeds();
for (SclIED ied : ieds) {
    String name = ied.getName();
    List<SclAccessPoint> aps = ied.getAccessPoints();
    for (SclAccessPoint ap : aps) {
        SclServer server = ap.getServer();
        // 遍历逻辑设备、逻辑节点...
    }
}

// Communication
SclCommunication comm = scl.getCommunication();
List<SclSubNetwork> subnets = comm.getSubNetworks();
for (SclSubNetwork sn : subnets) {
    List<SclConnectedAP> caps = sn.getConnectedAPs();
    for (SclConnectedAP cap : caps) {
        SclAddress addr = cap.getAddress();
        String ip = addr.getIp();
        String mac = addr.getMacAddress();
        String appid = addr.getAppid();
    }
}

// DataTypeTemplates
SclDataTypeTemplates templates = scl.getDataTypeTemplates();
SclLNodeType lnt = templates.findLNodeTypeById("LNTYPE_ID");
```

### 严格模式验证

启用严格模式时，`SclReader` 会根据文件类型进行合规性检查：

```java
SclReader reader = new SclReader();
reader.setStrictMode(true);

try {
    SclDocument scl = reader.read("invalid.icd");
} catch (IllegalArgumentException e) {
    // "ICD file must not contain a Substation element"
    System.err.println(e.getMessage());
}
```

**ICD 验证：** 禁止包含 `<Substation>` 元素，必须包含恰好 1 个 IED

**CID 验证：** 必须包含恰好 1 个 IED

**SCD 验证：** 必须包含至少 1 个 IED

### 未支持的 SCL 元素

根据 IEC 61850-6 XSD 定义，`<Line>` 和 `<Process>` 是 SCL 根元素下的可选顶层元素。当前实现会检测并记录这些元素，不会解析其内部结构。

```java
SclDocument scl = reader.read("file_with_line.scd");
if (scl.hasUnsupportedElements()) {
    System.out.println("发现未支持的元素: " + scl.getUnsupportedElements());
    // 输出: [Line, Process]
}
```

## 解析流程

```
SCL XML 文件 / InputStream
    │
    ▼
SclReader.read()
    │
    ├── DOM 解析 (namespace-aware)
    │
    ▼
parseDocument()
    │
    ├── 1. parseHeader()             → SclHeader (+ Hitem)
    ├── 2. parseSubstation()         → SclSubstation
    │      ├── VoltageLevel
    │      │    ├── Bay → ConductingEquipment / ConnectivityNode / LNode
    │      │    └── PowerTransformer → TransformerWinding → Terminal
    │      └── ...
    ├── 3. parseCommunication()      → SclCommunication
    │      └── SubNetwork → ConnectedAP
    │           ├── Address (Map)
    │           ├── GSE / SMV
    │           └── PhysConn
    ├── 4. parseIED()                → SclIED
    │      ├── Services
    │      │    └── ReportSettings, GSESettings
    │      ├── AccessPoint → Server → LDevice
    │      │    ├── LN0 → DataSet / ReportControl / LogControl
    │      │    │           / GSEControl / SampledValueControl / DOI
    │      │    └── LN  → DOI / Inputs
    │      └── ...
    └── 5. parseDataTypeTemplates()  → SclDataTypeTemplates
           ├── LNodeType → DO
           ├── DOType   → DA / SDO
           ├── DAType   → BDA
           └── EnumType → EnumVal
    │
    ▼
    文件类型检测 (ICD / CID / SCD)
    │
    ▼
    严格模式验证（可选）
    │
    ▼
SclDocument 返回
```

## SclAddress 地址模型

`SclAddress` 基于 `Map<String, String>` 存储所有 P 类型的通信地址参数，支持 IEC 61850-6 定义的所有 20+ 种 P 类型。

```java
SclAddress addr = cap.getAddress();

// 便捷方法
String ip = addr.getIp();
String mac = addr.getMacAddress();
String appid = addr.getAppid();
String vlanId = addr.getVlanId();
String vlanPriority = addr.getVlanPriority();

// 通用方法（适用于任何 P 类型）
String osiApTitle = addr.getParam("OSI-AP-Title");
```

支持的 P 类型：

| 类型常量 | 便捷方法 | 说明 |
|---------|---------|------|
| `IP` | `getIp()` | IPv4 地址 |
| `IP-SUBNET` | `getSubnet()` | IPv4 子网掩码 |
| `IP-GATEWAY` | `getGateway()` | IPv4 网关 |
| `OSI-TSEL` | `getOsiTsel()` | OSI 传输选择器 |
| `OSI-PSEL` | `getOsiPsel()` | OSI 表示选择器 |
| `OSI-SSEL` | `getOsiSsel()` | OSI 会话选择器 |
| `MAC-Address` | `getMacAddress()` | MAC 地址 |
| `APPID` | `getAppid()` | 应用标识 |
| `VLAN-ID` | `getVlanId()` | VLAN 标识 |
| `VLAN-PRIORITY` | `getVlanPriority()` | VLAN 优先级 |

## 与官方 XSD 的一致性

| SCL 顶层元素 | 实现 | 状态 |
|-------------|------|------|
| `<Header> + <Text> + <History>/<Hitem>` | `SclHeader` + `SclHitem` | ✅ |
| `<Substation>` 全套层次 | `SclSubstation` (15个内部类) | ✅ |
| `<Communication>` + GSE/SMV/PhysConn | `SclCommunication` (7个内部类) | ✅ |
| `<IED>` + Services + LN/LN0 全套 | `SclIED` (25个内部类) | ✅ |
| `<DataTypeTemplates>` 全套模板 | `SclDataTypeTemplates` (9个内部类) | ✅ |
| `<Line>` / `<Process>` | 检测并记录到 unsupportedElements | ⚠️ 桩 |

## 与 iec61850bean 的对比

| 对比项 | iec61850bean SclParser | **本模块 SclReader** |
|--------|----------------------|-------------------|
| `<Header>` | ❌ 不解析 | ✅ 完整解析 + History |
| `<Substation>` | ❌ 不解析 | ✅ 完整解析 |
| `<Communication>` | ❌ 不解析 | ✅ 完整解析 + 地址Map |
| `<IED>` | ✅ 解析到运行时模型 | ✅ 完整解析 |
| `<DataTypeTemplates>` | ✅ 解析到 TypeDefinitions | ✅ 完整解析 |
| 文件类型检测 | ❌ | ✅ ICD/CID/SCD 自动检测 |
| 严格模式 | ❌ | ✅ 合规验证 |
| 未支持元素检测 | ❌ | ✅ Line/Process 记录 |
| 输出模型 | `ServerModel`（运行时） | `SclDocument`（纯配置模型） |
| 整体定位 | 运行时通信专用 | 通用 SCL 配置解析 |

## 类索引

### 核心类

| 类 | 行数 | 说明 |
|---|------|------|
| `SclReader` | ~330 | SCL 文件解析器，DOM 解析 + 严格验证 |
| `SclDocument` | ~100 | SCL 文档根模型，文件类型检测 |

### 模型类

| 文件 | 行数 | 对应 SCL 元素 |
|------|------|--------------|
| `SclHeader.java` | 30 | `<Header>` |
| `SclHitem.java` | 26 | `<History>/<Hitem>` |
| `SclSubstation.java` | ~180 | `<Substation>` + 14个内部类 |
| `SclCommunication.java` | 106 | `<Communication>` + 6个内部类 |
| `SclDataTypeTemplates.java` | ~165 | `<DataTypeTemplates>` + 8个内部类 |
| `SclIED.java` | ~400 | `<IED>` + 24个内部类 |

### 模块统计

| 指标 | 数值 |
|------|------|
| **Java 文件数** | 8 |
| **总行数** | ~1300 |
| **模型类数（含内部类）** | ~65 |
| **覆盖的 SCL 元素** | ~40+ |
````

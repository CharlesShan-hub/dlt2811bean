# jcms-data — §7 数据结构 Java 封装

## 职责

jcms-data 是 DL/T 2811 标准第 7 章定义的**所有数据结构**在 Java 侧的实现，继承自 jcms-core 的 `CmsType` 基类体系，每个 Java 类对应一个 ASN.1 类型。

一句话概括：**§7 数据结构的 Java 镜像，每个类 = 一个 ASN.1 类型**。

> 不是AI写的总结：设计这一部分的时候的主要思路是，（1）做的更薄，不考虑编码（具体编码在ccms实现了，连接java和c的桥梁在jcms-core实现了）jcms-data只考虑数据结构的定义本身。（2）更贴近使用：很多枚举类型都是采用一个整数进行编码的，但是具体使用的时候大家更希望用某一个名字，而不关心这个名字对应哪一个整数。另外很多bit string也是，用户应该去考虑每一个flag是true还是false，而不应该考虑这个flag是biestring的第几个bit。

## 与 ccms-data 的关系

| 层面   | ccms-data（C）      | jcms-data（Java）                     |
| ---- | ----------------- | ----------------------------------- |
| 角色   | PER 编解码执行者        | 类型声明 + 编解码编排者                       |
| 内存   | C 结构体（stack/heap） | 通过 JNA 持有 native 内存                 |
| 编解码  | C 函数直接操作字节流       | 委托 `NativeBridge.Codec` 调用 C 函数     |
| 字段定义 | `.h` 头文件中的 struct | Java 类中的 `public` 字段 + `children()` |

简言之：Java 侧不重复实现 PER 编解码逻辑，而是通过 `CmsType.write()/read()` 与 C 侧同步内存，编解码回调 C 动态库。

## 包结构

```
data/
├── scalar/       # §7.1 基础数值类型
├── string/       # §7.1.5 字符串类型
├── time/         # §7.2 时间类型
├── common/       # §7.3 公共基础类型
├── fc/           # §7.4 功能约束
├── control/      # §7.5 控制相关类型
├── block/        # §7.6 控制块
├── choice/       # §7.7 数据 union / §7.8 数据定义
└── extended/     # (预留) §7.9 扩展类型
```

***

## 子包详解

### 1. `scalar` — 基础数值类型（§7.1）

| 类            | ASN.1 类型 | native 大小 | PER 说明                                  |
| ------------ | -------- | --------- | --------------------------------------- |
| `CmsBoolean` | BOOLEAN  | 4B        | INTEGER (0..1)，1 bit                    |
| `CmsInt8`    | INT8     | 4B        | INTEGER (-128..127)，8 bits aligned      |
| `CmsInt16`   | INT16    | 4B        | INTEGER (-32768..32767)，16 bits aligned |
| `CmsInt32`   | INT32    | 4B        | INTEGER (-2^31..2^31-1)，32 bits aligned |
| `CmsInt64`   | INT64    | 8B        | INTEGER，length + content bytes          |
| `CmsInt8U`   | INT8U    | 4B        | INTEGER (0..255)，8 bits aligned         |
| `CmsInt16U`  | INT16U   | 4B        | INTEGER (0..65535)，16 bits aligned      |
| `CmsInt24U`  | INT24U   | 4B        | INTEGER (0..16777215)，24 bits aligned   |
| `CmsInt32U`  | INT32U   | 4B        | INTEGER (0..2^32-1)，32 bits aligned     |
| `CmsInt64U`  | INT64U   | 8B        | INTEGER，length + content bytes          |
| `CmsFloat32` | FLOAT32  | 4B        | OCTET STRING (SIZE(4))                  |
| `CmsFloat64` | FLOAT64  | 8B        | OCTET STRING (SIZE(8))                  |

每个类都是一个**叶节点**（`children()` 返回空列表），直接管理 `nativePtr` 的读写。模式统一：

- 私有 `value` 字段 + 公开 getter/setter
- `write()` 将 Java value 写入 native 内存
- `read()` 从 native 内存读取到 Java value
- `calcNativeSize()` 返回 C 结构体大小

***

### 2. `string` — 字符串类型（§7.1.5）

| 类                  | ASN.1 类型      | 基类              | 说明                     |
| ------------------ | ------------- | --------------- | ---------------------- |
| `CmsUint8Array`    | OCTET STRING  | `CmsType`       | 字节数组的通用容器，也是其他字符串类型的基类 |
| `CmsVisibleString` | VisibleString | `CmsUint8Array` | ISO-646 可见字符串          |
| `CmsUtf8String`    | UTF8String    | `CmsUint8Array` | UTF-8 字符串              |
| `CmsOctetString`   | OCTET STRING  | `CmsUint8Array` | 原始字节串                  |
| `CmsBitString`     | BIT STRING    | `CmsUint8Array` | 比特串，`len` 存储 bit 数     |

`CmsUint8Array` 是核心，提供 `value()`/`value(byte[])` 方法，管理 `data` 指针 + `len` 长度。其余子类基本只是标记类型，编码解码行为相同。

***

### 3. `time` — 时间类型（§7.2）

| 类                | ASN.1 类型    | 说明                                   |
| ---------------- | ----------- | ------------------------------------ |
| `CmsUtcTime`     | UtcTime     | UTC 时间，OCTET STRING (SIZE(8))，以毫秒为单位 |
| `CmsBinaryTime`  | BinaryTime  | 二进制时间，OCTET STRING (SIZE(6))         |
| `CmsTimeQuality` | TimeQuality | 时间品质，BIT STRING (SIZE(8))            |

***

### 4. `common` — 公共基础类型（§7.3）

| 类                    | ASN.1 类型        | 说明                                                           |
| -------------------- | --------------- | ------------------------------------------------------------ |
| `CmsObjectName`      | ObjectName      | VisibleString (SIZE(0..64))，对象名称                             |
| `CmsObjectReference` | ObjectReference | VisibleString (SIZE(0..129))，对象引用（完整路径）                      |
| `CmsSubReference`    | SubReference    | VisibleString (SIZE(0..129))，子引用（路径片段）                       |
| `CmsEntryId`         | EntryID         | OCTET STRING (SIZE(8))，日志条目标识                                |
| `CmsEntryTime`       | EntryTime       | BinaryTime 别名，日志时间                                           |
| `CmsTimeStamp`       | TimeStamp       | UtcTime 别名，时间戳                                               |
| `CmsQuality`         | Quality         | BIT STRING (SIZE(13))，品质（含有效性、来源等标志）                         |
| `CmsDbpos`           | Dbpos           | BIT STRING (SIZE(2))，双点位置（0=中间/1=分/2=合/3=无效）                 |
| `CmsTcmd`            | Tcmd            | BIT STRING (SIZE(2))，命令类型                                    |
| `CmsServiceError`    | ServiceError    | INTEGER (0..12)，服务错误码                                        |
| `CmsPhyComAddr`      | PhyComAddr      | SEQUENCE，物理通信地址（addr + priority + vid + appid）               |
| `CmsFileEntry`       | FileEntry       | SEQUENCE，文件条目（fileName + fileSize + lastModified + checkSum） |

***

### 5. `fc` — 功能约束（§7.4）

| 类       | 说明                                                       |
| ------- | -------------------------------------------------------- |
| `CmsFC` | FunctionalConstraint 的 Java 数据表示，VisibleString (SIZE(2)) |

注意区分：

- `data.fc.CmsFC` — 运行时数据类型，参与编解码
- `info.FunctionalConstraint` — 纯枚举，提供 FC 的语义文档

***

### 6. `control` — 控制相关类型（§7.5）

| 类               | ASN.1 类型   | 说明                                                |
| --------------- | ---------- | ------------------------------------------------- |
| `CmsOriginator` | Originator | SEQUENCE { orCat, orIdent }，控制操作发起方               |
| `CmsOrCat`      | —          | Originator 中的 orCat 字段，INTEGER (0..8)             |
| `CmsCheck`      | Check      | BIT STRING { syncheck, interlock } (SIZE(2))，校验标志 |
| `CmsAddCause`   | AddCause   | INTEGER (0..27)，附加原因码                             |

***

### 7. `block` — 控制块（§7.6）

| 类                      | 对应标准              | 说明                               |
| ---------------------- | ----------------- | -------------------------------- |
| `CmsBrcb`              | BRCB              | 缓存报告控制块（15 个字段含 OPTIONAL）        |
| `CmsUrcb`              | URCB              | 非缓存报告控制块                         |
| `CmsLcb`               | LCB               | 日志控制块                            |
| `CmsSgcb`              | SGCB              | 定值组控制块（numOfSG + actSG + editSG） |
| `CmsGoCb`              | GoCB              | GOOSE 控制块                        |
| `CmsMsvcb`             | MSVCB             | 多播采样值控制块                         |
| `CmsRcbOptFlds`        | RcbOptFlds        | BIT STRING (SIZE(10))，报告可选域      |
| `CmsLcbOptFlds`        | LcbOptFlds        | BIT STRING (SIZE(1))，日志可选域       |
| `CmsMsvcbOptFlds`      | MsvcbOptFlds      | BIT STRING (SIZE(5))，采样值可选域      |
| `CmsReasonCode`        | ReasonCode        | BIT STRING (SIZE(7))，原因码         |
| `CmsTriggerConditions` | TriggerConditions | BIT STRING (SIZE(6))，触发条件        |
| `CmsSmpMod`            | SmpMod            | INTEGER (0..2)，采样模式              |

控制块类是典型的**容器类型**：`children()` 返回所有字段列表，`write()/read()` 由 `CmsType` 基类自动递归处理。

***

### 8. `choice` — 数据 union 与数据定义（§7.7 / §7.8）

| 类                             | ASN.1 类型       | 说明                              |
| ----------------------------- | -------------- | ------------------------------- |
| `CmsData`                     | Data           | CHOICE of 24 种类型，核心的通用数据载体      |
| `CmsDataDefinition`           | DataDefinition | CHOICE of 24 种类型，数据定义描述         |
| `CmsDataDefinitionArray`      | —              | SEQUENCE OF DataDefinition，定义数组 |
| `CmsDataDefinitionStructElem` | —              | SEQUENCE，定义中的结构体元素（name + type） |

#### CmsData

这是最重要的类之一——它用 CHOICE 表达"一个数据可以是 24 种类型中的任意一种"，类似于 union。包含：

- `choice` 选择器（0..23）
- 24 个 alternative 字段，覆盖所有标量、字符串、时间、品质、控制类型
- `alt_sequence`（`CmsArray<CmsData>`）用于表达 ARRAY 和 STRUCTURE 类型

关键设计：

- `resizeList()` 只返回选中的 alternative，避免未选中分支的无效扩容
- `allocSize = 0` 防止嵌套 `CmsData` 时的递归栈溢出

***

## 与 ccms-data 的对应关系

所有类的字段布局、`nativeSize` 计算均与 C 结构体严格对齐：

- 标量：`sizeof = 4`（JNA int 宽度）或 8（long 宽度）
- 容器：`children().size() * 8`（每个子节点一个 `Pointer` 槽位）
- `CmsArray`：固定 16 字节（`elements + count`）
- `CmsData`：固定 192 字节（24 个 alternative × 8）


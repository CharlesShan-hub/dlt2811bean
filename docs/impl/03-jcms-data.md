# jcms-data — §7 数据结构 Java 封装（csasn1 自动生成）

## 职责

jcms-data（Maven `artifactId: inner-data`）是 DL/T 2811 标准第 7 章**所有数据结构**在 Java 侧的自动生成镜像：**每个 Java 类对应一个 ASN.1 类型**，类名统一为 `Inner*`，全部位于单个包 `com.ysh.jcms.data`。

> 本模块不手写任何数据结构类。所有类由 Rust 工具链 **csasn1** 从 ASN.1 规约 `csasn1/specs/dlt2811.asn` 生成；编解码逻辑不在 Java 侧，而是委托 `asn1.dll`（Rust `rasn` 实现）执行，Java 与原生层之间用 **JSON 中间表示**交换数据。

一句话概括：**§7 数据结构的自动生成 Java 镜像，编码委托 Rust 原生库，JSON 为中间表示。**

## 生成管线

```
csasn1/specs/dlt2811.asn   ← 唯一数据源（只改这个）
      │
      ├─ cargo build（build.rs 调 rasn-compiler）
      │    ├─ src/generated.rs   （Rust 类型）
      │    ├─ src/ffi_auto.rs    （FFI 分发，导出 4 个 C 函数）
      │    └─ target/release/asn1.dll
      │
      └─ csasn1 CLI（--lang java）
           └─ jcms-data/src/main/java/com/ysh/jcms/data/Inner*.java
```

日常重新生成（在 `cms/` 根目录，一条命令搞定）：

```powershell
just single-j-data-gen
```

它会：编译 `asn1.dll`（同时更新 `generated.rs` / `ffi_auto.rs`）→ 删除旧 `jcms-data` → 生成全部 `Inner*.java`（含测试）→ 把 `asn1.dll` 部署到 `jcms-data/src/main/resources/win32-x86-64/`。

> csasn1 目录内另有独立命令（产出到 `assets/`，不碰 jcms-data），仅用于调试：`just build` / `just gen-java` / `just test-java`。

## 包结构

单包平铺，无子包：

```
data/
├── InnerBase          # 抽象基类：_v 统一存储 + 序列化 + hex 工具
├── InnerNative        # JNA 桥：调用 asn1.dll（ping/encode/decode/free）
├── V                  # _v 语义助手（标量 / CHOICE / OPTIONAL 标记）
├── InnerEmpty         # 占位类型（无 ASN.1 定义）
├── DefaultInner*      # 内建 ASN.1 类型包装（VisibleString/UTF8String/OctetString）
├── Inner*             # 一个 ASN.1 类型一个类（标量 / 位串 / 控制块 / PDU / CHOICE…）
└── InnerAnonymous*    # PDU 内联的匿名 SEQUENCE
```

每个生成的类，Javadoc 内嵌对应 ASN.1 定义（从规约提取），便于追溯标准条款。

## 核心架构：`_v` 统一数据存储

**所有 Inner\* 类没有 Java 数据字段。** 数据全部存在 `InnerBase._v`（`LinkedHashMap<String, Object>`）里，且 `_v` 里只有 Map / List / 基本类型（String、Integer、Boolean、byte\[]），**没有任何 Inner\* 对象**。

### 元字段命名规则

| key                 | 用途                                                |
| ------------------- | ------------------------------------------------- |
| `"_"`               | 标量值 / CHOICE 选中值（如 `{"_": 42}`）                   |
| `"_choice"`         | CHOICE 当前选中的 variant 名                            |
| `"_present_<name>"` | OPTIONAL 字段显式出现标记（`V.isPresent` / `V.setPresent`） |
| 其他                  | ASN.1 字段名，直接作为 key                                |

### 三类结构的 `_v` 形态

| 结构                  | `_v` 内容                             | 序列化结果                         |
| ------------------- | ----------------------------------- | ----------------------------- |
| 标量（InnerInt32）      | `{"_": 42}`                         | `42`                          |
| CHOICE（InnerData）   | `{"_choice": "int32", "_": 42}`     | `{"int32": 42}`               |
| SEQUENCE（InnerBRCB） | `{"rptID": {...}, "rptEna": {...}}` | `{"rptID": "x", "rptEna": 1}` |

- **标量**：值存 `"_"` 下，`toJsonValue()` 展开为裸值。
- **CHOICE**：`"_choice"` 记录当前 variant，选中值统一存 `"_"` 下（与标量一致）；未选中的 variant 不在 `_v`。
- **SEQUENCE**：每个字段一个 key；字段值是子类型（Inner\*）的 `_v`（Map）或基本类型。
- **OPTIONAL 字段**：构造器默认不放入 `_v`（无 ASN.1 DEFAULT 的 OPTIONAL 字段被跳过），所以未赋值就不会出现在 JSON/编码结果中；要包含它，直接向 `_v` 放入该字段即可。

### V 语义助手

`V.java` 提供对 `_v` 的类型安全操作，避免手写魔法字符串：

- 标量：`V.getVal(m)` / `V.setVal(m, v)` / `V.isScalarWrapper(v)` / `V.wrapScalar(v)` / `V.unwrapScalar(v)`
- CHOICE：`V.choice(m)` / `V.setChoice(m, name)`
- OPTIONAL：`V.isPresent(m, field)` / `V.setPresent(m, field, bool)`
- 字段：`V.field(m, name)` / `V.setField(m, name, v)` / `V.removeField(m, name)`

## 编解码流：JSON 中间表示 + JNA

`InnerBase.DEFAULT_ENCODING = "aper"`（Aligned Packed Encoding Rules，生成时固定）。

```
encode(): _v → toJsonValue() → Jackson JSON → InnerNative.encode() → asn1.dll → APER 字节
decode(): APER 字节 → InnerNative.decode() → JSON → Jackson → _v
```

### InnerNative（JNA 桥）

`InnerNative` 通过 JNA 加载 `asn1.dll`（`Native.load("asn1", …)`），只暴露 4 个函数：

```c
char* csasn1_ping(void);                                        // 自检
char* csasn1_encode(const char* type, const char* enc, const char* json);
char* csasn1_decode(const char* type, const char* enc, const uint8_t* data, size_t len);
void  csasn1_free_string(char* s);                              // 释放 Rust 分配的内存
```

- 原生函数返回 JSON 字符串（`{"ok": true, "bytes": [...]}` / `{"ok": false, "error": ...}`）；encode 的二进制结果用 JSON 数字数组（非 hex/base64），这是刻意设计（绕开 Jackson 对 `byte[]` 字符串默认 base64 解析的坑，见 02-csasn1.md）。
- `_v` 中的 `byte[]` 字段值序列化为**大写 hex 字符串**，与 Rust JER 输出格式一致（`InnerBase.hex`/`unhex` 负责转换）。
- 所有从 Rust 返回的字符串由 Java 侧 `readAndFree()` 统一释放，无内存泄漏。

## 类分类详解

### 1. 标量（§7.1）

| 类                               | ASN.1 类型                   | 说明                          |
| ------------------------------- | -------------------------- | --------------------------- |
| `InnerBoolean`                  | `INTEGER (0..1)`           | 布尔，1 bit                    |
| `InnerInt8` / `InnerInt8U`      | `INTEGER`                  | 8 位有/无符号                    |
| `InnerInt16` / `InnerInt16U`    | `INTEGER`                  | 16 位有/无符号                   |
| `InnerInt24U`                   | `INTEGER (0..16777215)`    | 24 位无符号                     |
| `InnerInt32` / `InnerInt32U`    | `INTEGER`                  | 32 位有/无符号                   |
| `InnerInt64` / `InnerInt64U`    | `INTEGER`                  | 64 位有/无符号（length + content） |
| `InnerFloat32` / `InnerFloat64` | `OCTET STRING (SIZE(4/8))` | 浮点                          |

所有标量 `_v` 形态一致：`{"_": value}`。

### 2. 字符串与对象引用（§7.1.5 / §7.3）

| 类                                                                                  | 说明                                     |
| ---------------------------------------------------------------------------------- | -------------------------------------- |
| `DefaultInnerVisibleString` / `DefaultInnerUtf8String` / `DefaultInnerOctetString` | 内建字符串类型包装（无独立 ASN.1 定义，作为字段值容器）        |
| `InnerObjectName`                                                                  | ObjectName，VisibleString (0..64)       |
| `InnerObjectReference`                                                             | ObjectReference，VisibleString (0..129) |
| `InnerSubReference`                                                                | SubReference，VisibleString (0..129)    |
| `InnerACSIClass`                                                                   | ACSI 类标识                               |

### 3. 时间（§7.2）

| 类                                 | 说明                               |
| --------------------------------- | -------------------------------- |
| `InnerUtcTime`                    | UTC 时间，OCTET STRING (SIZE(8))，毫秒 |
| `InnerBinaryTime`                 | 二进制时间，OCTET STRING (SIZE(6))     |
| `InnerTimeQuality`                | 时间品质，BIT STRING (SIZE(8))        |
| `InnerTimeStamp`                  | UtcTime 别名                       |
| `InnerEntryID` / `InnerEntryTime` | 日志条目标识 / 时间                      |

### 4. 位串与枚举（§7.3 / §7.5 / §7.6 辅助）

| 类                                                                  | 说明                                          |
| ------------------------------------------------------------------ | ------------------------------------------- |
| `InnerQuality`                                                     | 品质，BIT STRING (SIZE(13))                    |
| `InnerDbpos` / `InnerTcmd`                                         | 双点位置 / 命令类型，BIT STRING (SIZE(2))            |
| `InnerCheck`                                                       | 校验标志，BIT STRING (SIZE(2))                   |
| `InnerReasonCode`                                                  | 原因码，BIT STRING (SIZE(7))                    |
| `InnerTriggerConditions`                                           | 触发条件，BIT STRING (SIZE(6))                   |
| `InnerRcbOptFlds` / `InnerLcbOptFlds` / `InnerMsvcbOptFlds`        | 报告/日志/采样值可选域                                |
| `InnerSmpMod`                                                      | 采样模式，INTEGER (0..2)                         |
| `InnerServiceError`                                                | 服务错误码，INTEGER (0..12)（枚举以整数编码，`_v` 结构与标量一致） |
| `InnerAddCause` / `InnerControlCode` / `InnerFunctionalConstraint` | 附加原因 / 控制码 / 功能约束                           |
| `InnerOriginator` / `InnerPhyComAddr` / `InnerFileEntry`           | 简单 SEQUENCE 结构                              |

### 5. 控制块（§7.6）

| 类            | 对应标准  | 说明                                          |
| ------------ | ----- | ------------------------------------------- |
| `InnerBRCB`  | BRCB  | 缓存报告控制块（15 字段，含 2 个 OPTIONAL：resvTms/owner） |
| `InnerURCB`  | URCB  | 非缓存报告控制块                                    |
| `InnerLCB`   | LCB   | 日志控制块                                       |
| `InnerSGCB`  | SGCB  | 定值组控制块                                      |
| `InnerGoCB`  | GoCB  | GOOSE 控制块                                   |
| `InnerMSVCB` | MSVCB | 多播采样值控制块                                    |

### 6. 数据与数据定义（§7.7 / §7.8）

| 类                                                           | 说明                                                            |
| ----------------------------------------------------------- | ------------------------------------------------------------- |
| `InnerData`                                                 | 核心 CHOICE，24 种 variant（error/array/structure/各标量/各字符串/时间/品质…） |
| `InnerDataDefinition`                                       | 数据定义描述 CHOICE                                                 |
| `InnerDataDefinitionArray` / `InnerDataDefinitionStructure` | 定义数组 / 结构体元素                                                  |

CHOICE 类为每个 variant 生成 `@JsonSetter` 选择方法（如 `setInt32(...)`），方法内部自动维护 `_choice`。

### 7. 服务 PDU 与匿名序列（第 8 章）

- **PDU 类型**：`InnerApdu` / `InnerApch` / `InnerAsdu`，以及每个服务的 `InnerXxxRequestPDU` / `InnerXxxResponsePDU` / `InnerXxxErrorPDU`（覆盖关联、报告、控制、定值组、数据集、文件、RPC、GOOSE/MSV 等全部服务）。
- **匿名序列**：`InnerAnonymous*` 是 PDU 内联的匿名 SEQUENCE/CHOICE 的生成命名（如 `InnerAnonymousSetBRCBValuesRequestPDUBrcb`），避免为无名字段建单独类型。

## 使用示例

```java
import com.ysh.jcms.data.*;

// 构造 SEQUENCE，字段值通过子类型的 _v 赋值
InnerBRCB brcb = new InnerBRCB();
V.setField((Map) brcb._v.get("rptID"), "_", "LD0/LLN0$BR$myReport"); // VisibleString 包装
V.setField((Map) brcb._v.get("rptEna"), "_", 1);                     // Boolean
// OPTIONAL 字段：默认不在 _v，需要时直接放入
brcb._v.put("resvTms", V.wrapScalar(60));

byte[] apdu = brcb.encode();          // → asn1.dll（APER）
InnerBRCB back = InnerBRCB.decode(apdu);

// CHOICE：用生成的 @JsonSetter 或直接操作 _v
InnerData data = new InnerData();
data.setInt32(42);                    // 等价于 V.setChoice(_v,"int32") + put("_",42)
```

- `encode()` 为严格编码；`encodeTest()` 为测试用宽松/调试编码（打印中间 JSON）。
- `toString()` / `equals()` / `hashCode()` 均基于 `_v` 的结构化 JSON，天然支持数据相等比较。

## 与 jcms-core 的关系

依赖方向：**jcms-core 依赖 jcms-data**。

- `com.ysh.jcms.core.data.core.CmsType` 是 `Inner*` 的**薄包装**：持有 `public InnerBase inner`，PDU 类型通过 `super(new InnerXxx())` 绑定对应 Inner 类。
- `CmsBrcb extends CmsSequence` → `super(new InnerBRCB())`，在 `inner` 之上叠加类型化字段（`@CmsField` 标注 + `@CbField` 生命周期标注）与业务方法。
- 数据唯一来源是 `Inner*._v`；编解码编排（写读、帧组装、状态 overlay）在 jcms-core/jcms-app，jcms-data 只承载数据结构与原生编码委托。

## 测试

- 生成器为每个类型生成 `src/test/java/.../InnerXxxTest`。
- `pom.xml` 的 surefire 配置：`-Djava.library.path=${project.basedir}/src/main/resources`，保证测试加载 `asn1.dll`。

## 重新生成

只改 `csasn1/specs/dlt2811.asn`，其余全自动。在 `cms/` 根目录跑一条命令：

```powershell
just single-j-data-gen
```

等价 CLI（脚本内部实际执行）：

```powershell
cargo run --release -- --src specs/dlt2811.asn --dest ../jcms/jcms-data --prefix Inner --enc aper --package com.ysh.jcms.data
```

生成的 `asn1.dll` 会自动拷贝到 `src/main/resources/` 与 `src/main/resources/win32-x86-64/`（JNA 平台目录）。

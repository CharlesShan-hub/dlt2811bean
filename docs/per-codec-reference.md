# PER 编解码模块参考手册

> 模块: `com.ysh.dlt2811bean.per`
> 标准: **GB/T 16263.2-2025** / ITU-T X.691 (APER — Aligned Packed Encoding Rules)
> 用途: DL/T 2811 协议 ASDU 的二进制编解码

---

## 目录

1. [基础设施 — PerOutputStream / PerInputStream](#1-基础设施--peroutputstream--perinputstream)
2. [PerBoolean — BOOLEAN](#2-perboolean--boolean)
3. [PerNull — NULL](#3-pernull--null)
4. [PerInteger — INTEGER](#4-perinteger--integer)
5. [PerEnumerated — ENUMERATED](#5-perenumerated--enumerated)
6. [PerChoice — CHOICE](#6-perchoice--choice)
7. [PerBitString — BIT STRING](#7-perbitstring--bit-string)
8. [PerOctetString — OCTET STRING](#8-peroctetstring--octet-string)
9. [PerVisibleString — VisibleString / IA5String](#9-pervisiblestring--visiblestring--ia5string)
10. [PerUtf8String — UTF8String / BMPString](#10-perutf8string--utf8string--bmpstring)
11. [PerReal — Float32 / Float64 (DL/T 2811 专用)](#11-perreal--float32--float64-dlt-2811-专用)
12. [PerObjectIdentifier — OBJECT IDENTIFIER](#12-perobjectidentifier--object-identifier)

---

## 1. 基础设施 — PerOutputStream / PerInputStream

### PerOutputStream

**包路径**: `com.ysh.dlt2811bean.per.io.PerOutputStream`

位级输出流，PER 编码写入数据。纯内存操作，自动扩容。

| 方法 | 对应标准 | 说明 |
|------|---------|------|
| `writeBit(boolean)` | X.691 §9 | 写 1 比特 |
| `writeBits(long, int)` | X.691 §9 | 写 n 比特（1~64），MSB 优先 |
| `writeByteAligned(byte)` | X.691 §9.2 | 自动对齐后写 1 字节 |
| `writeBytes(byte[])` | X.691 §9.2 | 自动对齐后写字节数组 |
| `align()` | X.691 §9.2 | 字节对齐（补零到下一字节边界） |
| `writeSignedInteger(long, int)` | X.691 §12 | 补码形式有符号整数 |
| `toByteArray()` | — | 获取编码结果（副本） |
| `reset()` | — | 重置流，复用缓冲区 |

**关键实现细节**:
- `writeBits()` 在字节对齐时走 **fast path**（整字节批量写入），否则逐 bit 写入
- 对齐时只推进 `bitPosition`，不修改已写入的字节（padding bit 默认为 0）

### PerInputStream

**包路径**: `com.ysh.dlt2811bean.per.io.PerInputStream`

位级输入流，PER 解码读取数据。与 `PerOutputStream` 对称。

| 方法 | 对应标准 | 说明 |
|------|---------|------|
| `readBit()` | X.691 §9 | 读 1 比特 |
| `readBits(int)` | X.691 §9 | 读 n 比特（1~64），返回 unsigned long |
| `readByteAligned()` | X.691 §9.2 | 自动对齐后读 1 字节 |
| `readBytes(int)` | X.691 §9.2 | 自动对齐后读 n 字节 |
| `align()` | X.691 §9.2 | 跳到下一字节边界 |
| `readSignedInteger(int)` | X.691 §12 | 读补码有符号整数 |
| `readUnsignedInteger(int)` | X.691 §12 | 读无符号整数 |

**溢出保护**: 读取前检查 `ensureAvailable()`，数据不足时抛出 `PerDecodeException`。

---

## 2. PerBoolean — BOOLEAN

**包路径**: `com.ysh.dlt2811bean.per.types.PerBoolean`

| 标准 | 章节 |
|------|------|
| ASN.1 定义 | X.680 §16 |
| PER 编码 | X.691 §12 |
| 编码长度 | **1 比特** |

**编码规则**:
- `FALSE` = 比特 `0`
- `TRUE` = 比特 `1`

**使用示例**:
```java
PerBoolean.encode(pos, true);       // 写入 1 比特: 1
PerBoolean.encode(pos, false);      // 写入 1 比特: 0
boolean val = PerBoolean.decode(pis);
```

---

## 3. PerNull — NULL

**包路径**: `com.ysh.dlt2811bean.per.types.PerNull`

| 标准 | 章节 |
|------|------|
| ASN.1 定义 | X.680 §18 |
| PER 编码 | X.691 §13 |
| 编码长度 | **0 比特** |

**编码规则**: 不写入/读取任何数据。

**使用示例**:
```java
PerNull.encode(pos);   // 写入 0 比特
PerNull.decode(pis);   // 读取 0 比特
```

---

## 4. PerInteger — INTEGER

**包路径**: `com.ysh.dlt2811bean.per.types.PerInteger`

| 标准 | 章节 |
|------|------|
| ASN.1 定义 | X.680 §21 |
| PER 编码 | X.691 §12 |

### 4.1 有约束 INTEGER (Constrained)

编码方式取决于取值范围 `range = upperBound - lowerBound + 1`:

| range 范围 | 编码方式 | 对齐要求 |
|:----------:|----------|:--------:|
| `= 1` | 0 比特（值已确定） | 无 |
| `= 2 .. 256` | `ceil(log2(range))` 比特 | 无 |
| `= 257 .. 65536` | 16 比特 | **对齐** |
| `> 65536` | `ceil(log2(range)/8)` 字节 | **对齐** |

编码的是偏移量 `offset = value - lowerBound`。

**使用示例**:
```java
// range=1: 0 bit
PerInteger.encode(pos, 42, 42, 42);

// range=0..7: 3 bits
PerInteger.encode(pos, 5, 0, 7);
long v = PerInteger.decode(pis, 0, 7);  // 5

// range=0..65535: align + 16 bits
PerInteger.encode(pos, 1000, 0, 65535);

// 负范围: -128..127, 8 bits
PerInteger.encode(pos, -50, -128, 127);
```

### 4.2 小非负整数 (Normally Small Non-negative) — X.691 §11.6

| 值范围 | 编码格式 |
|:------:|----------|
| `0 .. 63` | `1-bit flag(0)` + `6-bit value` = 7 比特 |
| `≥ 64` | `1-bit flag(1)` + 半约束编码 |

**使用示例**:
```java
PerInteger.encodeSmallNonNegative(pos, 5);   // 7 bits: 0_000101
long v = PerInteger.decodeSmallNonNegative(pis);  // 5
```

### 4.3 半约束 INTEGER (Semi-Constrained, lb..MAX) — X.691 §12.3.2

编码格式: `[length L][content bytes]`

```java
PerInteger.encodeSemiConstrained(pos, 999, 0);
long v = PerInteger.decodeSemiConstrained(pis, 0);
```

### 4.4 无约束 INTEGER (Unconstrained) — X.691 §12.3.3

编码格式: `[length L][content bytes]`（补码形式）

```java
PerInteger.encodeUnconstrained(pos, -100);
PerInteger.encodeUnconstrained(pos, Long.MAX_VALUE);
long v = PerInteger.decodeUnconstrained(pis);
```

### 4.5 长度编码 (Length Determinant) — X.691 §11.9

| 长度范围 | 编码格式 | 首字节高位 |
|:--------:|----------|:----------:|
| `0 .. 127` | 1 字节 | `0xxxxxxx` |
| `128 .. 16383` | 2 字节 | `10xxxxxxxxxxxxxx`（14 位） |
| `≥ 16384` | 多片段 | `11` + 6 位 `(fragments-1)` + 2B 每段 |

**使用示例**:
```java
PerInteger.encodeLength(pos, 42);      // 1 byte
PerInteger.encodeLength(pos, 1000);    // 2 bytes
int len = PerInteger.decodeLength(pis);
```

---

## 5. PerEnumerated — ENUMERATED

**包路径**: `com.ysh.dlt2811bean.per.types.PerEnumerated`

| 标准 | 章节 |
|------|------|
| ASN.1 定义 | X.680 §20 |
| PER 编码 | X.691 §15 |

### 5.1 不可扩展枚举 (Non-extensible)

按序号编码为有约束 INTEGER（`0 .. maxOrdinal`）。

```java
// objectClass ENUMERATED { reserved(0), logical-device(1) }
PerEnumerated.encode(pos, 1, 1);   // maxOrdinal=1
int ordinal = PerEnumerated.decode(pis, 1);  // 1
```

### 5.2 可扩展枚举 (Extensible)

编码格式: `[preamble 1-bit][root/extension value]`

| field | 含义 |
|:----:|------|
| preamble = 0 | root 值，接有约束 INTEGER(`0..rootMaxOrdinal`) |
| preamble = 1 | extension 值，接小非负整数 |

```java
// root ordinal=1, rootMaxOrdinal=2
PerEnumerated.encodeExtensible(pos, false, 1, 2);
EnumeratedResult r = PerEnumerated.decodeExtensible(pis, 2);
// r.isExtension=false, r.ordinal=1
```

---

## 6. PerChoice — CHOICE

**包路径**: `com.ysh.dlt2811bean.per.types.PerChoice`

| 标准 | 章节 |
|------|------|
| ASN.1 定义 | X.680 §15 |
| PER 编码 | X.691 §17 |

### 6.1 不可扩展 CHOICE

编码格式: `[normally small non-negative: index][chosen alternative data]`

### 6.2 可扩展 CHOICE

编码格式: `[preamble 1-bit][normally small non-negative: index][chosen alternative data]`

```java
// 非扩展: 选择第 5 项
PerChoice.encode(pos, 5);
int idx = PerChoice.decode(pis);  // 5

// 扩展: extension 第 2 项
PerChoice.encodeExtensible(pos, true, 2);
ChoiceResult r = PerChoice.decodeExtensible(pis);
```

---

## 7. PerBitString — BIT STRING

**包路径**: `com.ysh.dlt2811bean.per.types.PerBitString`

| 标准 | 章节 |
|------|------|
| ASN.1 定义 | X.680 §22 |
| PER 编码 | X.691 §16 |

### 7.1 定长 BIT STRING (SIZE(n))

| n 范围 | 编码方式 | 对齐 |
|:------:|----------|:----:|
| `= 0` | 0 比特 | 无 |
| `1 .. 16` | n 比特直接编码 | 无 |
| `17 .. 65536` | n 比特，对齐后字节编码 | **对齐** |
| `> 65536` | [4 字节长度][对齐][内容字节] | **对齐** |

**使用示例**:
```java
// 6-bit: TriggerConditions
PerBitString.encodeFixedSize(pos, 0b000110, 6);
long v = PerBitString.decodeFixedSize(pis, 6);

// 10-bit: RCBOptFlds
PerBitString.encodeFixedSize(pos, 0b0000001011, 10);

// >64 bits: byte array
byte[] data = new byte[]{...};
PerBitString.encodeFixedSize(pos, data, 80);
byte[] out = PerBitString.decodeFixedSizeBytes(pis, 80);
```

### 7.2 变长 BIT STRING (SIZE(lb..ub))

编码格式: `[constrained integer: actual bit length][对齐][bit content]`

```java
PerBitString.encodeConstrained(pos, data, 8, 0, 65535);
byte[] out = PerBitString.decodeConstrained(pis, 0, 65535);
```

### 7.3 无约束 BIT STRING

编码格式: `[length determinant][对齐][content bytes][unused bits count(1byte)]`

```java
PerBitString.encodeUnconstrained(pos, data, 13);  // 13 valid bits
BitStringResult br = PerBitString.decodeUnconstrained(pis);
```

---

## 8. PerOctetString — OCTET STRING

**包路径**: `com.ysh.dlt2811bean.per.types.PerOctetString`

| 标准 | 章节 |
|------|------|
| ASN.1 定义 | X.680 §23 |
| PER 编码 | X.691 §18 |

### 8.1 定长 OCTET STRING (SIZE(n))

对齐后直接写入 n 字节。**无长度字段**。

| 方法 | 说明 |
|------|------|
| `encodeFixedSize(pos, data, n)` | n 字节对齐写入 |
| `decodeFixedSize(pis, n) → byte[]` | n 字节对齐读取 |
| `encodeInt2(pos, v)` | 2 字节大端（SIZE(2) 快捷方式） |
| `decodeInt2(pis) → int` | 2 字节大端 → int |
| `encodeInt4(pos, v)` | 4 字节大端（SIZE(4) 快捷方式） |
| `decodeInt4(pis) → int` | 4 字节大端 → int |
| `encodeUtcTime(pos, "YYMMDDHHMMSSZ")` | 8 字节 ASCII |
| `decodeUtcTime(pis) → String` | → "YYMMDDHHMMSSZ" |
| `encodeGeneralizedTime(pos, "YYYYMMDDHHMMSSZ")` | 13 字节 ASCII |
| `decodeGeneralizedTime(pis) → String` | → "YYYYMMDDHHMMSSZ" |

### 8.2 变长 OCTET STRING (SIZE(lb..ub))

编码格式: `[constrained integer: byte length][对齐][content bytes]`

| 方法 | 说明 |
|------|------|
| `encodeConstrained(pos, data, lb, ub)` | 编码长度偏移 + 内容 |
| `decodeConstrained(pis, lb, ub) → byte[]` | |

### 8.3 无约束 OCTET STRING

编码格式: `[length determinant][content bytes]`

| 方法 | 说明 |
|------|------|
| `encodeUnconstrained(pos, data)` | 长度编码 + 内容 |
| `decodeUnconstrained(pis) → byte[]` | |

**使用示例**:
```java
// 固定 64 字节: associationId
byte[] assocId = new byte[64];
PerOctetString.encodeFixedSize(pos, assocId, 64);
byte[] decoded = PerOctetString.decodeFixedSize(pis, 64);

// 变长: authenticationParameter (SIZE(0..8192))
PerOctetString.encodeConstrained(pos, cert, 0, 8192);

// 无约束: fileData
PerOctetString.encodeUnconstrained(pos, fileData);
byte[] out = PerOctetString.decodeUnconstrained(pis);
```

---

## 9. PerVisibleString — VisibleString / IA5String

**包路径**: `com.ysh.dlt2811bean.per.types.PerVisibleString`

| 标准 | 章节 |
|------|------|
| ASN.1 定义 | X.680 §40 (VisibleString) / §39 (IA5String) |
| PER 编码 | X.691 §41 |

**字符编码**: ISO 8859-1（ASCII 兼容），每字符 8 比特。若含 FROM 约束，则每字符 `ceil(log2(charsetSize))` 比特。

### 9.1 定长

无 FROM 约束时，每字符 8 比特，字符串短于定长时**右侧填充空格**（`0x20`）。

```java
PerVisibleString.encodeFixedSize(pos, "S1.AccessPoint1", 129);
String ref = PerVisibleString.decodeFixedSize(pis, 129);
```

### 9.2 定长 + FROM 约束

每字符使用 `ceil(log2(charsetSize))` 比特。

```java
PerVisibleString.encodeFixedSizeConstrained(pos, "ABC", 3, "ABCDEFG");
String s = PerVisibleString.decodeFixedSizeConstrained(pis, 3, "ABCDEFG");
```

### 9.3 变长

编码格式: `[constrained integer: char count][每字符 8 比特]`

```java
PerVisibleString.encodeConstrained(pos, "LD1/LN0.DO1", 0, 255);
String s = PerVisibleString.decodeConstrained(pis, 0, 255);
```

### 9.4 无约束

编码格式: `[length determinant][每字符 8 比特]`

```java
PerVisibleString.encodeUnconstrained(pos, "any length string");
String s = PerVisibleString.decodeUnconstrained(pis);
```

---

## 10. PerUtf8String — UTF8String / BMPString

**包路径**: `com.ysh.dlt2811bean.per.types.PerUtf8String`

| 标准 | 章节 |
|------|------|
| ASN.1 定义 | X.680 §42 (UTF8String) / §41 (BMPString) |
| PER 编码 | X.691 §42 |

### 10.1 UTF8String

以 UTF-8 字节数作为 OCTET STRING 编码。

| 方法 | 说明 |
|------|------|
| `encodeUtf8(pos, value)` | 无约束，[length][UTF-8 bytes] |
| `decodeUtf8(pis) → String` | |
| `encodeUtf8Constrained(pos, value, lb, ub)` | 约束字节数 |
| `decodeUtf8Constrained(pis, lb, ub) → String` | |

```java
PerUtf8String.encodeUtf8(pos, "device name");
String name = PerUtf8String.decodeUtf8(pis);

PerUtf8String.encodeUtf8Constrained(pos, "desc", 0, 255);
String desc = PerUtf8String.decodeUtf8Constrained(pis, 0, 255);
```

### 10.2 BMPString (UCS-2)

每字符 2 字节（UCS-2 big-endian），编码为 OCTET STRING。

```java
PerUtf8String.encodeBmpFixedSize(pos, "AB", 2);
String bmp = PerUtf8String.decodeBmpFixedSize(pis, 2);
```

---

## 11. PerReal — Float32 / Float64 (DL/T 2811 专用)

**包路径**: `com.ysh.dlt2811bean.per.types.PerReal`

> ⚠️ **注意**: 本实现并非标准 ASN.1 PER REAL (X.691 §19)。
> 标准 REAL 使用 base/exponent/mantissa 编码，而 DL/T 2811 的 Float32/Float64 直接映射为 IEEE 754 二进制格式。
> 因此本类使用简化的 1-bit 零值标志 + 对齐后的 IEEE 754 字节。

| 类型 | 编码格式 |
|:----:|----------|
| 零值 | `1-bit(0)` — 0 比特后续 |
| 非零值 | `1-bit(1)` + `align()` + IEEE 754 字节 |

```java
// FLOAT64 (double)
PerReal.encodeFloat64(pos, 220.5);
double v = PerReal.decodeFloat64(pis);   // 220.5

// FLOAT32 (float)
PerReal.encodeFloat32(pos, 3.14f);
float f = PerReal.decodeFloat32(pis);    // 3.14f
```

---

## 12. PerObjectIdentifier — OBJECT IDENTIFIER

**包路径**: `com.ysh.dlt2811bean.per.types.PerObjectIdentifier`

| 标准 | 章节 |
|------|------|
| ASN.1 定义 | X.680 §31 |
| PER 编码 | X.691 §23 |

**编码规则**:
- 前两个子标识符 `(a, b)` 合并为第一字节: `40*a + b`
- 后续子标识符使用 BER 风格变长编码（每 7 位一组，MSB 为 continuation 标志）
- 整体格式: `[length determinant][content bytes]`

```java
// OID: 1.3.6.1 (iso.org.dod.internet)
PerObjectIdentifier.encode(pos, new int[]{1, 3, 6, 1});
int[] oid = PerObjectIdentifier.decode(pis);  // {1, 3, 6, 1}

// String convenience
String s = PerObjectIdentifier.toString(oid);          // "1.3.6.1"
int[] parsed = PerObjectIdentifier.fromString("1.3.6.1");
```

---

## 附录: 标准章节索引

| 本模块类 | 标准章节 (GB/T 16263.2 / X.691) | PER 编码类型 |
|----------|--------------------------------|-------------|
| `PerOutputStream` | §9 (Encoding of a bit/byte) | 位级输出流 |
| `PerInputStream` | §9 (Encoding of a bit/byte) | 位级输入流 |
| `PerBoolean` | §12 | BOOLEAN |
| `PerNull` | §13 | NULL |
| `PerInteger` | §12 | INTEGER |
| `PerEnumerated` | §15 | ENUMERATED |
| `PerChoice` | §17 | CHOICE |
| `PerBitString` | §16 | BIT STRING |
| `PerOctetString` | §18 | OCTET STRING |
| `PerVisibleString` | §41 | VisibleString / IA5String |
| `PerUtf8String` | §42 | UTF8String / BMPString |
| `PerReal` | (自定义) | Float32/Float64 (IEEE 754) |
| `PerObjectIdentifier` | §23 | OBJECT IDENTIFIER |
| — | §11.9 | Length determinant |
| — | §11.6 | Normally small non-negative integer |

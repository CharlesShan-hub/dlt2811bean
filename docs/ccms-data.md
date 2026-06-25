# data 零件模块参考手册

> 模块路径: [`cms/ccms/include/data/`](../ccms/include/data/) + [`cms/ccms/src/data/`](../ccms/src/data/)
> 实现语言: **C11**
> 位置: ccms 中间层，位于 per 之上、svc 之下

---

## 目录

1. [总览](#1-总览)
2. [数据类型分类](#2-数据类型分类)
3. [统一设计模式](#3-统一设计模式)
4. [编码策略分类](#4-编码策略分类)
5. [与 per 层的关系](#5-与-per-层的关系)

---

## 1. 总览

data 层是 DL/T 2811 协议中定义的**数据"零件"类型**的 C 实现。每个零件对应协议 ASN.1 定义中的一个基础类型（INTEGER、BOOLEAN、BIT STRING 等）或复合类型（SEQUENCE、CHOICE），负责完成该类型与 PER 二进制流之间的转换。

data 层不感知协议服务语义（那是 svc 层的职责），也不直接操作比特（那是 per 层的职责）——它处于"知道每个字段的协议约束，但不关心怎么逐比特写入"的中间位置。

### 层次定位

```
┌──────────────────────────────────┐
│  svc  — 服务数据单元 (SDU)         │  由 data 零件组装
├──────────────────────────────────┤
│  data — 协议定义的零件类型          │  ← 你在这里
├──────────────────────────────────┤
│  per  — PER 编码原语              │  bit-level 编解码
└──────────────────────────────────┘
```

---

## 2. 数据类型分类

data 层按协议类型组织为以下子目录：

| 目录 | 用途 | 示例 |
|------|------|------|
| `scalar/` | 基础标量类型，直接映射为 PER 有约束整数或定长字节 | `boolean`, `int8`~`int64`, `int8u`~`int64u`, `float32`, `float64` |
| `enum/` | ENUMERATED 类型 | `cms_enumerated` |
| `string/` | 字符串/字节数组类型 | `octet_string`, `visible_string`, `utf8_string`, `bit_string`, `uint8_array` |
| `time/` | 时间类型 | `utc_time`, `binary_time`, `time_quality` |
| `common/` | 协议层面复用的公用类型 | `quality`, `object_name`, `object_reference`, `dbpos`, `tcmd`, `entry_id`, `entry_time` 等 |
| `control/` | 控制相关类型 | `check`, `or_cat`, `originator`, `add_cause` |
| `fc/` | 功能约束类型 | `functional_constraint` |
| `block/` | 复合块类型（SEQUENCE），对应协议定义的块结构 | `brcb`, `urcb`, `lcb`, `msvcb`, `sgcb`, `go_cb`, `rcb_opt_flds`, `trigger_conditions` 等 |
| `choice/` | CHOICE 联合体类型 | `Data`, `DataDefinition`, `DataDefinitionArray` |

---

## 3. 统一设计模式

所有 data 类型遵循完全一致的代码模式。

### 3.1 类型定义 — 全指针结构体

每种类型是一个 struct，**所有字段都是指针**。指针指向的类型本身也是全指针结构体，最终指向一个 `int value` 或 `uint8_t* + int32_t len` 的叶子。

```c
// 叶子类型示例（scalar）
typedef struct { int value; } cms_boolean_t;        // 单值
typedef struct { uint32_t value; } cms_int32u_t;     // 单值

// 叶子类型示例（string）
typedef struct { uint8_t *value; int32_t len; } cms_uint8_array_t;  // 通用字节数组

// 复合类型示例（block）
typedef struct {
    cms_boolean_t          *rptEna;        // BOOLEAN 指针
    cms_int32u_t           *confRev;       // INT32U 指针
    cms_trigger_conditions_t *trgOps;      // 子结构体指针
    cms_boolean_t          *resvTms_present; // OPTIONAL 标记
    cms_int16_t            *resvTms;       // OPTIONAL 值
    // ...
} cms_brcb_t;
```

全指针设计的意义：
- **编码/解码时不需要拷贝数据**，直接操作原对象
- **OPTIONAL 字段**通过独立的 `xxx_present` 布尔指针标记存在性
- **CHOICE 类型**通过 selector（`cms_enumerated_t*`）决定当前选中的 alternative

### 3.2 函数签名 — 两层四函数

每个类型暴露 **4 个函数**，分两个层级：

```
Stream 层（内部使用，供父类型组装调用）:
  int xxx_encode_stream(per_stream_t *s, const void *ptr);
  int xxx_decode_stream(per_stream_t *s, void *ptr);

Buffer 层（外部使用，独立编解码）:
  int xxx_encode(const void *ptr, uint8_t *out_buf, int *out_len);
  int xxx_decode(void *ptr, const uint8_t *in_buf, int in_len);
```

两层的关系：

```c
// Buffer 层是 Stream 层的简单包装
int xxx_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = xxx_encode_stream(&s, ptr);     // 调用 stream 层
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int xxx_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return xxx_decode_stream(&s, ptr);       // 调用 stream 层
}
```

### 3.3 返回值

所有函数返回 `int`：
- `CMS_OK` (0) — 成功
- `CMS_ERR` (-1) — 失败（传入 NULL 指针、值越界、数据截断等）

```c
#define CMS_OK   0
#define CMS_ERR -1
```

---

## 4. 编码策略分类

不同种类的数据采用不同的编码策略，但都通过 stream 层组合 per 原语实现。

### 4.1 叶子标量 — 直接委托 per 原语

标量类型（scalar, enum）的编码最直接：调用一次 per 原语完成。

```c
// Int32U: INTEGER (0..4294967295)
int cms_int32u_encode_stream(per_stream_t *s, const void *ptr) {
    uint32_t val = *(const uint32_t*)ptr;
    return (int)per_encode_constrained_int(s, val, 0, 4294967295U);
}

// Boolean: INTEGER (0..1) → 1 bit
int cms_boolean_encode_stream(per_stream_t *s, const void *ptr) {
    return (int)per_encode_constrained_int(...);  // range=2
}

// Enumerated: INTEGER (-128..127) → 8 bits
int cms_enumerated_encode_stream(per_stream_t *s, const void *ptr) {
    return (int)per_encode_constrained_int(s, val, -128, 127);
}

// Float32: 直接映射为 4 字节 OCTET STRING
int cms_float32_encode_stream(per_stream_t *s, const void *ptr) {
    return cms_octet_string_fixed_encode_stream(s, (const uint8_t*)ptr, 4);
}
```

### 4.2 定长位域 — pack 成字节 + BIT STRING 编码

位域类型（quality, trigger_conditions, rcb_opt_flds 等）先 pack 为字节，再通过 BIT STRING 原语编码。

```c
// Quality: BIT STRING (SIZE(13))
// 1. 把 13 个布尔字段打包到 2 字节
// 2. 用 per bit_string_fixed 编码 13 位
int cms_quality_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t buf[2];
    pack_quality((const cms_quality_t*)ptr, buf);
    return cms_bit_string_fixed_encode_stream(s, buf, 13);
}

// TriggerConditions: BIT STRING (SIZE(6))
// 1. 把 5 个布尔字段打包到 1 字节
// 2. 用 per bit_string_fixed 编码 6 位
int cms_trigger_conditions_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t byte = pack_trigger((const cms_trigger_conditions_t*)ptr);
    return cms_bit_string_fixed_encode_stream(s, &byte, 6);
}
```

### 4.3 定长复合 — pack 成字节数组 + OCTET STRING 编码

时间类型将结构化字段打包为固定大小的字节数组，再通过 OCTET STRING 原语编码。

```c
// UtcTime: 固定 8 字节
//   4 bytes seconds + 3 bytes fraction + 1 byte time quality → OCTET STRING SIZE(8)
int cms_utc_time_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t buf[8];
    pack_utc_time((const cms_utc_time_t*)ptr, buf);
    return cms_octet_string_fixed_encode_stream(s, buf, 8);
}

// BinaryTime: 固定 6 字节
//   4 bytes msOfDay + 2 bytes daysSince1984 → OCTET STRING SIZE(6)
```

### 4.4 字符串类型 — 复用 `cms_uint8_array_t` 泛型结构

所有变长字符串（VisibleString、OctetString、UTF8String、BitString）共用 `cms_uint8_array_t`：

```c
typedef struct {
    uint8_t *value;     // 数据指针
    int32_t  len;       // 长度（字节数或比特数）
} cms_uint8_array_t;
```

data 层的字符串函数通过宏从 `uint8_array` 中提取 `(ptr, len)`，然后调用 per 字符串原语：

```c
int cms_visible_string_encode_stream(per_stream_t *s, const void *ptr, uint32_t max_len) {
    const uint8_t *vptr = ARRAY_PTR(ptr);        // 提取 value
    int32_t len = ARRAY_LEN(ptr);                 // 提取 len
    return (int)per_encode_visible_string(s, vptr, max_len);
}

int cms_octet_string_encode_stream(per_stream_t *s, const void *ptr, uint32_t max_len) {
    const uint8_t *vptr = ARRAY_PTR(ptr);
    int32_t len = ARRAY_LEN(ptr);
    return (int)per_encode_octet_string(s, vptr, (size_t)len, max_len);
}
```

对于固定长度字符串，另提供 `_fixed` 后缀的 stream 函数：

```c
int cms_visible_string_encode_stream_fixed(per_stream_t *s, const void *ptr, uint32_t fixed_len);
int cms_octet_string_fixed_encode_stream(per_stream_t *s, const uint8_t *data, int fixed_len);
```

### 4.5 SEQUENCE — OPTIONAL bitmap + 必选字段 + 可选值

SEQUENCE 类型（brcb, urcb, lcb 等）遵循 PER 标准（X.691 §22）：

1. **OPTIONAL bitmap** 编码在 SEQUENCE 的**最开头**（字节对齐后，按字段序号从 0 到 N 逐位标记）
2. **必选字段**按 ASN.1 定义的字段顺序依次编码
3. **OPTIONAL 字段的值**在末尾依 bitmap 决定是否编码

```c
int cms_brcb_encode_stream(per_stream_t *s, const void *ptr) {
    // 0. OPTIONAL bitmap：bool 数组，集中写在最前
    bool opt_present[2] = {
        resvTms_present && pdu->resvTms,
        owner_present   && pdu->owner
    };
    per_encode_optional_bitmap(s, opt_present, 2);

    // 1..13. 必选字段：按协议顺序逐个编码
    cms_visible_string_encode_stream(s, pdu->rptID, 129);
    cms_boolean_encode_stream(s, pdu->rptEna);
    // ...

    // 14..15. OPTIONAL 字段（依据 opt_present 决定是否编码）
    if (opt_present[0]) cms_int16_encode_stream(s, pdu->resvTms);
    if (opt_present[1]) cms_octet_string_encode_stream(s, pdu->owner, 64);
}
```

### 4.6 CHOICE — 索引 + 分支编码

CHOICE 类型（Data）先编码备选索引（small non-negative integer），再根据索引编码对应 alternative 的内容。

```c
int cms_data_encode_stream(per_stream_t *s, const void *ptr) {
    // 1. 编码 CHOICE 索引
    per_encode_small_non_negative(s, sel);

    // 2. 根据索引编码对应 alternative
    switch (sel) {
    case CMS_DATA_CHOICE_BOOLEAN:
        return cms_boolean_encode_stream(s, d->alt_boolean);
    case CMS_DATA_CHOICE_INT32U:
        return cms_int32u_encode_stream(s, d->alt_int32u);
    case CMS_DATA_CHOICE_UTC_TIME:
        return cms_utc_time_encode_stream(s, d->alt_utc_time);
    // ...
    }
}
```

---

## 5. 与 per 层的关系

```
per 层提供（bit-level 原语）          data 层使用（type-level 编码）
──────────────────────────────       ────────────────────────────
per_encode_constrained_int  ───────  cms_int32u / cms_boolean / cms_enumerated
per_encode_length           ───────  cms_array (SEQUENCE OF) 计数
per_encode_small_non_negative ─────  cms_data (CHOICE 索引)
per_encode_octet_string_fixed ────  cms_utc_time / cms_float32
per_encode_visible_string   ───────  cms_visible_string
per_encode_octet_string     ───────  cms_octet_string
per_encode_bit_string_fixed ───────  cms_quality / cms_trigger_conditions
per_encode_bit_string       ───────  cms_bit_string (data 中的 BIT STRING)
per_encode_optional_bitmap  ───────  block 类型中的 OPTIONAL 位图
```

data 层每个 `encode_stream` 函数的职责就是：
1. **知道每个字段的协议约束**（bound / max_len / bit position）
2. **把结构化 C 数据拆解为 per 原语能够接受的平坦参数**
3. **按协议顺序调用 per 原语**

它**不需要关心**以下细节（已由 per 层封装）：
- 比特如何写入缓冲区
- 何时需要字节对齐
- range=1 时如何自动省略编码
- range 不同范围时采用哪种编码格式

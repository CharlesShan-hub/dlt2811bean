# PER 编解码模块参考手册

> 模块路径: [`cms/ccms/src/per/`](../ccms/src/per/) + [`cms/ccms/include/per/`](../ccms/include/per/)
> 实现语言: **C11**
> 对应标准: **GB/T 16263.2-2025 / ITU-T X.691** (APER — Aligned Packed Encoding Rules)
> 用途: DL/T 2811 协议中所有数据类型的二进制编解码基础层

---

## 目录

1. [总览](#1-总览)
2. [错误类型 — `per_error_t`](#2-错误类型--per_error_t)
3. [位流 — `per_stream_t`](#3-位流--per_stream_t)
4. [整数编码 — `per/cms_integer`](#4-整数编码--percms_integer)
5. [字符串编码 — `per/cms_string`](#5-字符串编码--percms_string)
6. [CHOICE 索引编码 — `per/cms_choice`](#6-choice-索引编码--percms_choice)
7. [SEQUENCE OPTIONAL 位图 — `per/cms_sequence`](#7-sequence-optional-位图--percms_sequence)

---

## 1. 总览

PER 编解码层是整个 ccms 编码体系的**地基**。它向上为 data 层的"零件"类型提供编码原语，再经由 svc 层组装为协议报文。

### 层次关系

```
┌──────────────────────────────────┐
│  svc  — 服务数据单元 (SDU)         │  报文级
├──────────────────────────────────┤
│  data — 协议定义的零件类型          │  类型级
├──────────────────────────────────┤
│  per  — PER 编码                  │  编码级  ← 你在这里
└──────────────────────────────────┘
        ↓ 原始字节流 (wire)
```

### 文件清单

| 文件 | 职责 | 对应 X.691 |
|------|------|-----------|
| [`per/cms_types.h`](../ccms/include/per/cms_types.h) | 错误码枚举 `per_error_t` | — |
| [`per/cms_stream.h`](../ccms/include/per/cms_stream.h) | 位级读写流 `per_stream_t` | §9 |
| [`per/cms_stream.c`](../ccms/src/per/cms_stream.c) | 流实现 | §9 |
| [`per/cms_integer.h`](../ccms/include/per/cms_integer.h) | INTEGER 编码 | §12, §11.9 |
| [`per/cms_integer.c`](../ccms/src/per/cms_integer.c) | 整数实现 | §12, §11.9 |
| [`per/cms_string.h`](../ccms/include/per/cms_string.h) | 字符串/位串编码 | §16, §18, §41, §42 |
| [`per/cms_string.c`](../ccms/src/per/cms_string.c) | 字符串实现 | §16, §18, §41, §42 |
| [`per/cms_choice.h`](../ccms/include/per/cms_choice.h) | CHOICE 索引 | §17 |
| [`per/cms_choice.c`](../ccms/src/per/cms_choice.c) | CHOICE 实现 | §17 |
| [`per/cms_sequence.h`](../ccms/include/per/cms_sequence.h) | SEQUENCE OPTIONAL 位图 | §22 |
| [`per/cms_sequence.c`](../ccms/src/per/cms_sequence.c) | SEQUENCE 实现 | §22 |

### 设计原则

1. **所有函数返回 `per_error_t`** — 0 表示成功，负数表示具体错误
2. **编码/解码严格对称** — 每个 `per_encode_xxx` 都有对应的 `per_decode_xxx`
3. **纯内存操作** — 不涉及文件/网络 I/O，不依赖外部状态
4. **无动态内存分配（默认）** — 调用方提供缓冲区，仅 `per_stream_init_dynamic` 例外

---

## 2. 错误类型 — `per_error_t`

**头文件**: [`per/cms_types.h`](../ccms/include/per/cms_types.h)

所有 per 函数以及上层 data/svc 函数的返回值类型。

### 枚举值

| 常量 | 值 | 含义 | 典型触发场景 |
|------|:--:|------|-------------|
| `PER_OK` | `0` | 操作成功 | — |
| `PER_ERR_OVERFLOW` | `-1` | 缓冲区溢出 | 固定缓冲模式写入空间不足 |
| `PER_ERR_RANGE` | `-2` | 值超出约束范围 | 编码时 value < lower_bound 或 value > upper_bound |
| `PER_ERR_INVALID_ARG` | `-3` | 无效参数 | 传入 NULL 指针、nbits > 64、nfields > 64 |
| `PER_ERR_TRUNCATED` | `-4` | 输入数据被截断 | 解码时读取超出缓冲区长度 |
| `PER_ERR_LENGTH` | `-5` | 长度约束违规 | 字符串超长、解码长度 > 预期 |
| `PER_ERR_OOM` | `-6` | 内存不足 | 动态模式 `calloc`/`realloc` 失败 |

### 使用示例

```c
per_error_t err = per_encode_constrained_int(&s, value, 0, 100);
if (err != PER_OK) {
    // 处理错误
}
```

---

## 3. 位流 — `per_stream_t`

**头文件**: [`per/cms_stream.h`](../ccms/include/per/cms_stream.h)
**实现**: [`per/cms_stream.c`](../ccms/src/per/cms_stream.c)

### 数据结构

```c
typedef struct {
    uint8_t *buf;         // 底层字节缓冲区
    size_t   capacity;    // 缓冲区容量
    size_t   byte_pos;    // 当前字节位置 (0-based)
    int      bit_pos;     // 当前字节内的比特位置 (0-7, 0=MSB)
    bool     is_write;    // true=写入模式, false=读取模式
    bool     is_dynamic;  // true=堆分配自动扩容
} per_stream_t;
```

### 两种工作模式

| 模式 | 初始化函数 | 缓冲区来源 | 溢出处理 |
|------|-----------|-----------|---------|
| **固定缓冲** | `per_stream_init_read` / `per_stream_init_write` | 调用方提供（栈或静态） | 返回 `PER_ERR_OVERFLOW` |
| **动态自动扩容** | `per_stream_init_dynamic` | `calloc` 分配，按需 `realloc` | 自动增长（可能返回 `PER_ERR_OOM`） |

### 流生命周期

```c
// 固定缓冲写入
uint8_t buf[64];
per_stream_t s;
per_stream_init_write(&s, buf, sizeof(buf));
// ... 写入数据 ...
size_t written = per_stream_bytes_written(&s);

// 固定缓冲读取
per_stream_init_read(&s, buf, sizeof(buf));
// ... 读取数据 ...

// 动态写入
per_stream_t s;
per_stream_init_dynamic(&s, 64);
// ... 写入数据 ...
size_t out_len;
uint8_t *result = per_stream_detach(&s, &out_len);
// 调用方负责 free(result)
```

### 比特读写操作

| 函数 | 说明 | 对齐要求 |
|------|------|:--------:|
| `per_stream_write_bit(s, bit)` | 写 1 比特 | 无 |
| `per_stream_read_bit(s, &out)` | 读 1 比特 | 无 |
| `per_stream_write_bits(s, value, nbits)` | 写 n 比特 (1-64), MSB 优先 | **byte-aligned fast path** |
| `per_stream_read_bits(s, &out, nbits)` | 读 n 比特 (1-64) | **byte-aligned fast path** |

**Fast path 细节**: 当 `bit_pos == 0` 且 `nbits >= 8` 时，`write_bits`/`read_bits` 走整字节批量路径（`memcpy` 级别性能），剩余的不足 8 比特逐位处理。

### 字节对齐操作

| 函数 | 说明 |
|------|------|
| `per_stream_align(s)` | 对齐到下一字节边界（write 时进位，read 时跳过残余位） |
| `per_stream_write_byte_aligned(s, byte)` | 先 `align()` 再写 1 字节 |
| `per_stream_read_byte_aligned(s, &out)` | 先 `align()` 再读 1 字节 |
| `per_stream_write_bytes(s, data, len)` | 先 `align()` 再写 len 字节 |
| `per_stream_read_bytes(s, out, len)` | 先 `align()` 再读 len 字节 |

### 查询

| 函数 | 返回值 |
|------|--------|
| `per_stream_tell(s)` | 当前比特位置（从流起始算起） |
| `per_stream_bytes_written(s)` | 已写入的完整字节数（未对齐的字节进位计为 1） |

---

## 4. 整数编码 — `per/cms_integer`

**头文件**: [`per/cms_integer.h`](../ccms/include/per/cms_integer.h)
**实现**: [`per/cms_integer.c`](../ccms/src/per/cms_integer.c)

### 4.1 有约束整数 (Constrained INTEGER) — X.691 §12

编码偏移量 `offset = value - lower_bound`，取值范围 `range = upper_bound - lower_bound + 1`。

| range 范围 | 编码方式 | 对齐 |
|:----------:|----------|:----:|
| `= 1` | **0 比特**（值已隐含确定） | 无 |
| `2 .. 255` | `ceil(log2(range))` 比特直接编码 | 无 |
| `256 .. 65536` | 对齐后，`bytes_for_range(range)` 字节大端 | **对齐** |
| `> 65536` | `constrained_int(1..maxLen) + align + offset bytes` | **对齐** |

```c
// range=1: 0 比特，值必然是 42
per_encode_constrained_int(&s, 42, 42, 42);

// range=10 (0..9): 4 比特
per_encode_constrained_int(&s, 5, 0, 9);

// range=40001 (0..40000): align + 2 字节
per_encode_constrained_int(&s, 30000, 0, 40000);

// range=4294967296 (Int32U): constrained_int(1..4) + align + 4 字节
per_encode_constrained_int(&s, 100000, 0, 4294967295U);
```

### 4.2 长度编码 (Length Determinant) — X.691 §11.9

| 长度范围 | 编码格式 | 首字节高位 |
|:--------:|----------|:----------:|
| `0 .. 127` | 1 字节 | `0xxxxxxx` |
| `128 .. 16383` | 2 字节 | `10xxxxxx xxxxxxxx` (14 位长度) |
| `> 16383` | **不支持**（返回 `PER_ERR_RANGE`） | — |

**注意**: 本实现不支持 X.691 §11.9.3.8 的分片格式 (fragmented form)。长度超过 16383 的场景请在上层自行分片。

```c
per_encode_length(&s, 42);     // 1 字节: 0x2A
per_encode_length(&s, 1000);   // 2 字节: 0x83 0xE8

uint32_t len;
per_decode_length(&s, &len);   // len = 42
```

### 4.3 小非负整数 (Normally Small Non-negative) — X.691 §11.6

| 值范围 | 编码格式 |
|:------:|----------|
| `0 .. 63` | `1bit(0) + 6bit value` = 7 比特 |
| `≥ 64` | `1bit(1) + semi-constrained encoding` |

主要用于 CHOICE 索引和可扩展性标记 (extensibility marker)。

```c
per_encode_small_non_negative(&s, 5);    // 7 bits: 0_000101
per_encode_small_non_negative(&s, 100);  // 1bit(1) + length + content
```

### 4.4 半约束整数 (Semi-Constrained, lb..MAX) — X.691 §12.3.2

编码格式: `[length L][content bytes]`

偏移量 `offset = value - lb`，以最小字节数的大端形式写入。

```c
per_encode_semi_constrained(&s, 10000, 0);
// → length(2) + 0x27 0x10
```

### 4.5 无约束整数 (Unconstrained) — X.691 §12.3.3

编码格式: `[length L][content bytes]`，内容为补码形式。

```c
per_encode_unconstrained_int(&s, 42);       // length(1) + 0x2A
per_encode_unconstrained_int(&s, -128);     // length(1) + 0x80
per_encode_unconstrained_int(&s, -999999);  // length(3) + 0xF0 0xBD 0xC1
```

### 4.6 辅助函数 — `per_unsigned_to_bytes`

将 `uint64_t` 值转为最小字节数的大端字节数组。

```c
uint8_t out[8];
int n = per_unsigned_to_bytes(0xABCD, out, 8);
// n=2, out[0]=0xAB, out[1]=0xCD
```

### 内部辅助函数

| 函数 | 说明 |
|------|------|
| `bits_needed(range)` | 计算 `ceil(log2(range))` |
| `bytes_for_range(range)` | 计算 `ceil(log2(range) / 8)` |

---

## 5. 字符串编码 — `per/cms_string`

**头文件**: [`per/cms_string.h`](../ccms/include/per/cms_string.h)
**实现**: [`per/cms_string.c`](../ccms/src/per/cms_string.c)

本模块覆盖 **4 种字符串类型**，每种按约束方式分为 **3 类**编码形式：

| 字符串类型 | 对应 X.691 | 定长 | 变长（有约束） | 无约束 |
|-----------|:----------:|:----:|:-------------:|:------:|
| **OCTET STRING** | §18 | `octet_string_fixed` | `octet_string` | `octet_string_unconstrained` |
| **VisibleString** | §41 | `visible_string_fixed` | `visible_string` | `visible_string_unconstrained` |
| **UTF8String** | §42 | `utf8_string_fixed` | `utf8_string` | `utf8_string_unconstrained` |
| **BIT STRING** | §16 | `bit_string_fixed` | `bit_string` | `bit_string_unconstrained` |

编码格式规律：
- **定长**: 无长度前缀，直接编码内容（OCTET/UTF8: align+bytes; Visible/BIT: 按位写入）
- **变长**: `constrained_int(0..ub): len` + `align` + 内容
- **无约束**: `length_determinant: len` + `align` + 内容（BIT STRING 使用 semi-constrained）

### 5.1 OCTET STRING — X.691 §18

#### 定长 OCTET STRING — `SIZE(n)`

编码格式: `[align][n bytes]`（无长度前缀）

```c
// 固定 4 字节
per_encode_octet_string_fixed(&s, data, 4);
per_decode_octet_string_fixed(&s, out, 4);
```

#### 变长 OCTET STRING — `SIZE(lb..ub)`

编码格式: `[constrained_int(0..ub): len][align][len bytes]`

```c
// SIZE(0..10)
per_encode_octet_string(&s, data, 4, 10);

size_t out_len;
per_decode_octet_string(&s, out, &out_len, 10);
```

#### 无约束 OCTET STRING

编码格式: `[length_determinant: len][align][len bytes]`

```c
per_encode_octet_string_unconstrained(&s, data, len);

size_t out_len;
per_decode_octet_string_unconstrained(&s, out, &out_len);
```

### 5.2 VisibleString — X.691 §41

每个字符 8 比特（ISO 8859-1 / ASCII 兼容）。

#### 变长 VisibleString — `SIZE(0..max_len)`

编码格式: `[constrained_int(0..max_len): char_count][if max_len*8>16: align][chars × 8bit]`

对齐条件: 仅当 `max_len * 8 > 16`（即最大字符超 2 个）时才字节对齐。

```c
per_encode_visible_string(&s, (const uint8_t *)"hello", 10);
uint8_t out[16];
per_decode_visible_string(&s, out, 10);  // out = "hello\0"
```

#### 定长 VisibleString

编码格式: `[if fixed_len*8>16: align][chars × 8bit]`，短于定长时补零。

```c
per_encode_visible_string_fixed(&s, (const uint8_t *)"hello", 8);
per_decode_visible_string_fixed(&s, out, 8);
```

#### 无约束 VisibleString

编码格式: `[length_determinant: len][align][chars × 8bit]`

```c
per_encode_visible_string_unconstrained(&s, (const uint8_t *)"hello");
uint32_t out_len;
per_decode_visible_string_unconstrained(&s, out, &out_len);
```

### 5.3 UTF8String — X.691 §42

UTF-8 字节序列以 OCTET STRING 方式编码。

#### 变长 UTF8String

```c
per_encode_utf8_string(&s, (const uint8_t *)"héllo", 10);
uint8_t out[16];
per_decode_utf8_string(&s, out, 10);
```

#### 定长 UTF8String

编码格式: `[align][bytes][zero-pad to fixed_len]`

```c
per_encode_utf8_string_fixed(&s, (const uint8_t *)"héllo", 10);
per_decode_utf8_string_fixed(&s, out, 10);
```

#### 无约束 UTF8String

```c
per_encode_utf8_string_unconstrained(&s, (const uint8_t *)"héllo");
uint32_t out_len;
per_decode_utf8_string_unconstrained(&s, out, &out_len);
```

### 5.4 BIT STRING — X.691 §16

#### 定长 BIT STRING — `SIZE(n)`

| n 范围 | 编码方式 | 对齐 |
|:------:|----------|:----:|
| `0` | 0 比特 | 无 |
| `1 .. 16` | n 比特直接编码 | 无 |
| `> 16` | n 比特，对齐后字节写入 | **对齐** |

`data` 参数以字节数组传入，bit 以 MSB 优先从字节中提取。

```c
// 13 位: 1010101010101
uint8_t data[2] = { 0xAA, 0xA8 };
per_encode_bit_string_fixed(&s, data, 13);

uint8_t out[2] = {0};
per_decode_bit_string_fixed(&s, out, 13);
```

#### 变长 BIT STRING — `SIZE(lb..ub)`

编码格式: `[constrained_int(0..ub): len][align][bits]`

```c
per_encode_bit_string(&s, data, 13, 16);
int out_nbits;
per_decode_bit_string(&s, out, &out_nbits, 16);
```

#### 无约束 BIT STRING

编码格式: `[semi_constrained: len][align][bits]`

```c
per_encode_bit_string_unconstrained(&s, data, 13);
int out_nbits;
per_decode_bit_string_unconstrained(&s, out, &out_nbits);
```

---

## 6. CHOICE 索引编码 — `per/cms_choice`

**头文件**: [`per/cms_choice.h`](../ccms/include/per/cms_choice.h)
**实现**: [`per/cms_choice.c`](../ccms/src/per/cms_choice.c)

### 6.1 不可扩展 CHOICE — X.691 §17

编码格式: `[small_non_negative: index][alternative data]`

```c
per_encode_choice(&s, 5);     // 小非负整数编码索引 5
uint32_t idx;
per_decode_choice(&s, &idx);  // idx = 5
```

### 6.2 可扩展 CHOICE — X.691 §17

编码格式: `[preamble 1bit][small_non_negative: index][alternative data]`

| preamble | 含义 | 索引编码 |
|:--------:|------|----------|
| `0` | root 范围 | 小非负整数 |
| `1` | extension 范围 | 小非负整数 |

```c
// root 范围的索引 3
per_encode_choice_extensible(&s, false, 3);

// extension 范围的索引 200
per_encode_choice_extensible(&s, true, 200);

bool is_extension;
uint32_t idx;
per_decode_choice_extensible(&s, &is_extension, &idx);
```

---

## 7. SEQUENCE OPTIONAL 位图 — `per/cms_sequence`

**头文件**: [`per/cms_sequence.h`](../ccms/include/per/cms_sequence.h)
**实现**: [`per/cms_sequence.c`](../ccms/src/per/cms_sequence.c)

### X.691 §22 — OPTIONAL / DEFAULT 字段位图

PER 编码中，SEQUENCE 的所有 OPTIONAL 和 DEFAULT 字段用一个位图（bitmap）表示存在性。位图在第一个字段之前 **字节对齐**。

```c
int nfields = 3;  // 3 个 OPTIONAL 字段
uint64_t bitmap = 0;

// 字段 0 存在，字段 1 存在，字段 2 不存在
bitmap |= (1ULL << 0);  // field0 present
bitmap |= (1ULL << 1);  // field1 present
// field2 absent — 位保持 0

per_encode_optional_bitmap(&s, bitmap, nfields);  // align + 3 bits
// 然后编码存在的字段...

// 解码
uint64_t decoded;
per_decode_optional_bitmap(&s, &decoded, nfields);
if (decoded & (1ULL << 0)) decode_field0();
if (decoded & (1ULL << 1)) decode_field1();
// field2 absent — 不解码
```

### 约束

- `nfields` 最大 **64**（超出返回 `PER_ERR_INVALID_ARG`）
- `nfields <= 0` 时写入 0 位（无操作）
- 位图总是字节对齐**开始**（align before bits）

---

## 附录: 标准章节索引

| 本模块函数 | 对应 X.691 章节 | 编码类型 |
|-----------|:---------------:|----------|
| `per_stream_write_bit/read_bit` | §9 | 位级 I/O |
| `per_stream_write_bits/read_bits` | §9 | 多位 I/O |
| `per_stream_align` | §9.2 | 字节对齐 |
| `per_encode_constrained_int` | §12 | 有约束 INTEGER |
| `per_encode_length` | §11.9 | 长度编码 |
| `per_encode_small_non_negative` | §11.6 | 小非负整数 |
| `per_encode_semi_constrained` | §12.3.2 | 半约束整数 |
| `per_encode_unconstrained_int` | §12.3.3 | 无约束整数 |
| `per_encode_octet_string_fixed` | §18 | 定长 OCTET STRING |
| `per_encode_octet_string` | §18 | 变长 OCTET STRING |
| `per_encode_visible_string` | §41 | VisibleString |
| `per_encode_utf8_string` | §42 | UTF8String |
| `per_encode_bit_string_fixed` | §16 | 定长 BIT STRING |
| `per_encode_choice` | §17 | CHOICE 索引 |
| `per_encode_optional_bitmap` | §22 | SEQUENCE OPTIONAL 位图 |

# 01 rasn — ASN.1 编解码框架（第三方库）

## 1. ASN.1 工具/方案对比

| 工具/方案 | 编写语言 | BER | APER | 安全性 | 仓库活跃度 |
| --- | --- | --- | --- | --- | --- |
| pyasn1 | Python | ✓ | ✗ | 中：解释型 | 不活跃（2020-03 后无更新） |
| asn1tools | Python | ✓ | △ | 中：解释型 | 不活跃（2024-06 后无更新） |
| OSS ASN.1（商业） | Java / Python / C++ 等 | ✓ | △ | 视生成代码而定 | 活跃、质量高（许可证昂贵） |
| asn1c（vlm） | C | ✓ | ✗ | 中低：C 手动内存管理 | 不活跃（2021-2025 无更新） |
| ASN1bean | Java | ✓ | ✗ | 中：Java 托管 | 不活跃（偏 61850 一次性设计） |
| asn1js | JavaScript | ✓ | ✗ | 中：JS 引擎 | 活跃 |
| 自己开发 Java 库 | Java | — | — | 低：位操作手动处理易出错 | 不适用 |
| 自己开发 C 库 | C | — | — | 低：JNA 映射复杂、不支持批量生成 | 不适用 |
| **rasn**（本项目采用） | Rust | ✓ | ✓ | 高：纯 safe Rust + AFL++ 模糊测试 | 活跃（2026 持续发版，质量高） |

> △：部分支持或调研中未明确（asn1tools 的 PER 对复杂类型支持有限；OSS 调研中未提及 APER）。

### 1.1 [pyasn1](https://github.com/etingof/pyasn1)

* pyasn1 是一个 Python 库，用于 ASN.1 编解码。
* pyasn1 支持 BER、DER、CER 以及 Python 内置的编解码器。**不支持 APER 编码。**
* pyasn1 最新的更新在 2020-3-22，社区更新已经**不活跃**。

### 1.2 [asn1tools](https://github.com/eerimoq/asn1tools)

* asn1tools 是一个 Python 库，用于 ASN.1 编解码。
* asn1tools 支持 BER、DER、GSER、JER、OER、PER、UPER、XER 编码。但是经过测试**PER编码对于复杂类型支持有限**。
* asn1tools 最新的更新在 2024-6-11，社区更新已经**不活跃**。

### 1.3 [oss asn1](https://www.oss.com/)

* oss asn1 是一个商业 ASN.1 编解码框架，支持 java，python，c++ 等多语言。但是缺点是付费，**许可证昂贵**。
* oss asn1 支持 BER、DER、CER、PER、UPER、XER 编码。
* oss asn1 社区更新活跃，质量很高。

### 1.4 [asn1c](https://github.com/vlm/asn1c)

* asn1c 是一个 C 库，用于 ASN.1 编解码，历史悠久。
* asn1c 支持 BER、DER、CER、PER、UPER、XER 编码。但是 per **编码不支持 aper** 子类，只支持uper。
* asn1c 社区有维护，但不活跃（2021-2025年均没有任何更新），质量很高。

### 1.5 [ASN1bean](https://www.beanit.com/asn1/)

* ASN1bean 是一个 Java 库，用于 ASN.1 编解码。
* ASN1bean 支持 BER、DER、CER、PER、UPER、XER 编码。但是**不支持 APER 编码**。
* ASN1bean 已经**不活跃**，他更倾向于对61860的支持一次性设计。

### 1.6 [asn1js](https://github.com/PeculiarVentures/asn1.js/)

* asn1js 是一个 JavaScript 库，用于 ASN.1 编解码。
* asn1js 支持 BER 编码，**不支持 APER 编码**。
* asn1js 社区更新活跃。

### 1.7 自己开发java库

* 曾经尝试自己开发java asn1编解码库，但是**安全性很难保证**，因为位操作需要手动处理，容易出错。

### 1.8 自己开发c库

* 曾经尝试自己开发c asn1编解码库，c与java需要JNA映射，**复杂度很高**，并且如果收到新的asn1规范，需要手动更新c库，**不支持批量生成**，很容易出错。

### 1.9 [rasn](https://github.com/librasn/rasn) 本项目采用的库

* rasn 是一个 Rust 库，用于 ASN.1 编解码。
* rasn 支持 BER、DER、CER、PER、UPER、XER 编码。
* rasn 社区更新活跃，质量很高，目前大部分 rust 的 asn.1 都使用 rasn 进行二次开发。

### 1.10 综上所述

综合以上调研，最终选择 **rasn**，理由如下：

1. **APER 支持是硬性门槛**：DL/T 2811 标准要求对齐压缩编码规则（APER），开源工具中只有 rasn 完整支持；asn1c 的 PER 仅支持 UPER，asn1tools 对复杂类型支持有限，其余工具均不支持。商业方案（OSS）虽质量高但许可证昂贵。
2. **社区活跃度**：rasn 持续发版（2026 年仍有 0.28.x 更新），并被大量 Rust 项目作为 ASN.1 基础库二次开发；其余开源工具（pyasn1 / asn1tools / asn1c / ASN1bean）均已停更或低频维护。
3. **安全性**：rasn 为纯 safe Rust 实现，经 AFL++ 模糊测试 + 编译期 tag 校验，避免了位操作手动处理带来的隐患——这正是自研 Java / C 库的失败教训。
4. **批量生成能力**：rasn 生态自带 rasn-compiler，可将 `.asn` 规范直接编译成 Rust 类型（本项目 csasn1 即此用法），规范更新时无需手动改代码；自研 C 库恰恰卡在"不支持批量生成"。
5. **开源免费**：无商业许可证成本。

结论：rasn 在 **APER 支持、社区活跃度、安全性、自动化代码生成、成本** 五个维度上均为最优解，故选为本项目的 ASN.1 编解码基础。

## 2. rasn详细介绍（AI生成 可以参考）

| 项目 | 内容 |
| --- | --- |
| 性质 | 第三方开源库，**非本项目编写** |
| 版本 | 0.28.13（Cargo workspace 统一管理，2026-04 发布） |
| 仓库 | https://github.com/librasn/rasn |
| 许可证 | MIT OR Apache-2.0 |
| 定位 | safe `#[no_std]` 的 ASN.1 codec 框架 |
| 引入方式 | csasn1 中 `[patch.crates-io] rasn = { path = "../rasn" }` 锁定本地副本 |
| 在本项目的作用 | 为 csasn1 提供 APER 二进制编解码 + JER（JSON）编解码能力 |

## 2.1 这是什么

rasn（读作 "raisin"）是活跃维护的开源 Rust ASN.1 编解码框架：用 derive 宏描述一次数据结构，即可在 BER / DER / CER / APER / UPER / JER / OER / COER / XER 等任意规则下编解码。核心卖点：**数据与规则分离**、**纯 safe Rust + no_std + AFL++ 模糊测试**、**编译期 tag 唯一性校验**、自带 **rasn-compiler**（.asn → Rust）和 RFC 标准实现。

## 2.2 在本项目中的位置

rasn 是编解码栈最底层，Java 侧感知不到它。

```
specs/dlt2811.asn ──build.rs: rasn-compiler──▶ src/generated.rs（derive 类型）
      ──▶ ffi_auto.rs（csasn1_encode/decode，C ABI）──JNA──▶ jcms-data（JSON ↔ 二进制）
```

- **引入**：csasn1 通过 `[patch.crates-io] rasn = { path = "../rasn" }` 锁定本地副本（即 `cms/rasn`）。
- **运行时**：编码 = `jer::decode` → `aper::encode`；解码 = `aper::decode` → `jer::encode`。FFI 另暴露 ber / der / uper 兼容入口（默认 ber，标准实际用 APER）。

## 2.3 整体架构

Cargo workspace：

```
rasn/
├── src/       # 框架本体：types(类型系统) · enc/de(抽象 trait) · per/(APER/UPER 核心)
│              # 各规则层 ber|der|cer|jer|oer|coer|xer|avn · error
├── macros/    # rasn-derive：AsnType/Encode/Decode 派生宏（含编译期 tag 校验）
└── standards/ # RFC 标准实现（cms / kerberos / ldap / pkix / snmp / ...）
```

四层设计：**编码规则层**（每种规则 = 一个 Encoder/Decoder）→ **抽象接口层**（Encode/Decode trait）→ **类型系统层**（types::* + AsnType）→ **用户数据层**（generated.rs）。长度、tag、对齐、扩展位图等细节全封装在规则层，业务类型无感——即"数据与规则分离"。

## 2.4 核心概念

1. **三大 trait**：`AsnType` 提供元数据（`TAG`/`TAG_TREE`/`CONSTRAINTS`/`IDENTIFIER`/`IS_CHOICE`）；`Encode`/`Decode` 只需实现 `encode/decode_with_tag_and_constraints`，其余为便捷方法。
2. **derive 宏**：`#[derive(AsnType, Decode, Encode)]` + `#[rasn(...)]`（`choice`/`enumerated`/`automatic_tags`/`tag`/`value`/`size`/`extension_addition`/`default`/`identifier`）。映射：SEQUENCE→struct、CHOICE→enum、OF→Vec、OPTIONAL→Option、DEFAULT→默认值函数；编译期断言 tag 唯一。
3. **类型系统**：`Integer`/`ConstrainedInteger`（num-bigint）；`BitString`/`FixedBitString`、`OctetString`/`FixedOctetString`（bitvec）；9 种字符串类型；`SequenceOf`/`SetOf`；`Tag`/`TagTree`/`Constraints`（value/size/字母表/extensible）。
4. **编码规则**：

| 规则 | 标准 | 格式 | 本项目使用 |
| --- | --- | --- | --- |
| BER / CER / DER | X.690 | 二进制（TLV） | 仅 FFI 兼容入口 |
| **APER** | X.691 | 二进制（按位对齐） | **核心** |
| UPER | X.691 | 二进制（按位不对齐） | 仅 FFI 兼容入口 |
| **JER** | JSON 编码规则 | 文本 JSON | **核心** |
| OER / COER | X.696 | 二进制（八位组） | 未用 |
| XER | X.693 | 文本 XML | 未用 |
| AVN | X.680 | 文本值记法 | 未用 |

5. **APER 要点**：aper/uper 共享 `per/` 实现，仅靠 `DecoderOptions.aligned` 区分；解码 basic（解任意合法 APER）、编码 canonical（同值同输出）；按位编解码 + 对齐填充；约束整数最小位宽，无约束用长度前缀（16K/32K/48K/64K 阈值）；可选字段 presence bitmap、扩展类型 1bit 指示 + 扩展位图；递归深度限 128 防栈溢出；SET 用 BTreeMap 按 tag 排序输出。
6. **JER 要点**：基于 serde_json；枚举/CHOICE 输出 `{"Test1":3}`、位串 `{"length":n,"value":"hex"}`；**u32 > i32::MAX 有 bug**，ffi_auto 对整数 newtype 用 serde_json 绕过。

## 2.5 本项目实际用到的能力

| rasn 能力 | 用途 | 位置 |
| --- | --- | --- |
| `rasn::aper::encode` / `decode` | APER 二进制编解码 | csasn1 ffi_auto.rs |
| `rasn::jer::encode` / `decode` | JER JSON 编解码 | csasn1 ffi_auto.rs |
| `#[derive(AsnType, Decode, Encode)]` | generated.rs 全部类型 | csasn1 src/generated.rs |
| `rasn-compiler` | .asn → Rust 类型 | csasn1 build.rs |
| `FixedBitString` / `FixedOctetString` 等 | 定长位串 / 字节串类型 | generated.rs |
| `rasn::prelude::*` | 测试与工具代码 | csasn1 lib.rs |

## 2.6 与其他模块的关系

- 唯一使用方是 csasn1（patch 到本地 `cms/rasn`）；jcms-data 由 csasn1 生成 Inner\* 类，Java 侧不接触 rasn。
- 修改 rasn 源码 = fork 上游，升级会与 crates.io 冲突，需谨慎。

## 2.7 注意事项

rasn 是编解码栈根基，升级前先查上游 `rasn/CHANGELOG.md`（记录 PER/JER 修复），升级后回归 csasn1 `lib.rs` 的 APER/JER roundtrip 测试。

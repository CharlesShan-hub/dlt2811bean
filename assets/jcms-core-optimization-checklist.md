# jcms-core 优化待办清单

> 分析日期：2026-08-17
> 分析范围：jcms-core 模块（`CmsType` 层次结构、`_v` 共享机制、编解码流程）

---

## 一、设计模式规范性

### 1.1 `CmsData` 的 ARRAY/STRUCTURE 绕过 `@Choice` 机制

- [ ] **问题**：`CmsData.alt_sequence` 字段没有 `@Choice` 注解，ARRAY(1) 和 STRUCTURE(2) 两个变体在 `syncToInner()`/`syncFromInner()` 中手工处理，需要 override 父类方法
- [ ] **建议方案 A**：将 ARRAY/STRUCTURE 统一为 `@Choice` 注解变体，让基类自动处理
- [ ] **建议方案 B**：如果必须手工处理（两个变体共享同一个 Java 字段），添加明确注释说明原因

### 1.2 `CmsSequence` 子类构造函数重复初始化

- [ ] **问题**：`injectFields()` 已为 `@CmsField(sequenceOf = true)` 字段创建 `ArrayList`，但部分子类构造函数中又手动 `new ArrayList<>()` 覆盖（如 `CmsGetDataValuesRequest`、`CmsGetDataValuesResponse`），而其他子类不这样做（如 `CmsBrcb`），风格不一致
- [ ] **建议**：统一约定——要么子类不手动初始化（依赖 `injectFields`），要么 `injectFields` 不做初始化让子类自己负责

### 1.3 `CmsUtcTime`/`CmsBinaryTime` 继承 `CmsType` 而非 `CmsSequence`

- [ ] **问题**：这两个复合类型包含多个子字段，但继承了 `CmsType` 而不是 `CmsSequence`，子字段通过手工 `syncToInner()`/`syncFromInner()` 管理，没有 `_v` 共享
- [ ] **建议**：添加注释说明为什么选择手工方式（定长 OCTET STRING 编码而非 SEQUENCE），避免未来维护者困惑

### 1.4 `CmsBits` 的双数据源同步

- [ ] **问题**：`CmsBits` 使用 Java primitive 字段（`boolean`, `int`）存储位值，通过 `syncToInner()` 打包到 `_v`，通过 `syncFromInner()` 从 `_v` 解包。存在两个数据源，需要保证同步正确
- [ ] **建议**：检查 `CmsChoice` 嵌套 `CmsBits` 的场景（如 `CmsData` 的 `CHOICE_QUALITY`），确保 `_v` 共享链完整，`syncWrapperToInner()` 后值能正确写回

### 1.5 `CmsChoice` 中 `registerNullChoice` 的使用

- [ ] **问题**：`CmsData` 的 ARRAY/STRUCTURE 没有注册到 `variantByIndex`/`variantByName`，`CmsChoice.choice(int)` 中 `variantByIndex.get(v)` 返回 `null`，依赖 `CmsData.choice()` 中先设置 `selectedChoiceIndex` 的逻辑
- [ ] **建议**：考虑为 ARRAY/STRUCTURE 注册空变体，或统一使用 `registerNullChoice` 注册，减少对执行顺序的隐式依赖

---

## 二、性能优化

### 2.1 `LinkedHashMap` 内存开销

- [ ] **问题**：每个 `Inner*` 对象都有一个 `LinkedHashMap<String, Object>`，对于深嵌套结构（如 `CmsData` 嵌套在 SEQUENCE OF 中），每个层级都有独立的 Map 实例，内存开销大
- [ ] **建议**：暂不处理（低频场景下不是问题）。如果未来出现大规模场景（如 `all-data` 返回数千个值），可考虑：
  - 简单类型使用直接字段访问而非 Map
  - 在 `CmsScalar` 层缓存值到 Java 字段

### 2.2 每次 decode 全量 rebind

- [ ] **问题**：每次 decode 都会遍历所有 `@CmsField` 字段、共享 `_v` 子 map、递归 rebind、为 SEQUENCE OF 每个元素创建新的 wrapper 实例
- [ ] **建议**：暂不处理（低频场景）。如果高频 decode 成为瓶颈，可考虑池化 wrapper 实例或延迟 rebind

### 2.3 `CmsBits` 反射访问 `@Bit` 字段

- [ ] **问题**：`syncToInner()`/`syncFromInner()` 使用 `Field.getBoolean(this)`、`Field.setInt(this, ...)` 等反射调用
- [ ] **建议**：`ClassValue` 已缓存 Field 数组，反射开销在可接受范围内。如果 `CmsQuality` 等类型高频使用，可考虑代码生成直接访问

### 2.4 `InnerBase.encode()` 的 JSON 序列化开销

- [ ] **问题**：每次 encode 路径：`CmsType → syncToInner() → InnerBase.encode() → MAPPER.writeValueAsString() → InnerNative.encode()`，JSON 序列化涉及完整 Jackson 树遍历
- [ ] **建议**：架构决定（Rust FFI 接口是 JSON 字符串），除非性能瓶颈否则不处理

---

## 三、隐藏 Bug

### 3.1 `CmsData.syncFromInner()` 中 ARRAY/STRUCTURE 的 `_v` 格式假设

- [ ] **问题**：`syncFromInner()` 中假设 `InnerData.setArray` 总是将 list 包装为 `{"_": [...]}`：
  ```java
  Object raw = inner._v.get("_");
  if (raw instanceof java.util.LinkedHashMap) {
      raw = V.getVal((java.util.Map<String, Object>) raw);
  }
  ```
  如果 Rust 端的 JER 输出格式变化，这个假设可能不成立
- [ ] **建议**：增加单元测试覆盖 ARRAY/STRUCTURE 的 encode→decode→compare 循环

### 3.2 `CmsData.toValueString()` 中 `CHOICE_OCTET_STRING` 强转

- [ ] **问题**：
  ```java
  case CHOICE_OCTET_STRING :
      return (String) alt_octet_string.toJsonValue();
  ```
  如果 `DefaultInnerOctetString._v` 中存储的是 `byte[]` 而非 `String`，强转会抛 `ClassCastException`
- [ ] **建议**：改为 `String.valueOf(alt_octet_string.toJsonValue())` 或使用 `Objects.toString()`

### 3.3 `CmsUtcTime`/`CmsBinaryTime` 的嵌套 `_v` 共享链

- [ ] **问题**：`CmsUtcTime` 不继承 `CmsSequence`，它的子字段（`CmsInt32U`, `CmsTimeQuality` 等）通过手工 sync 管理。当 `CmsUtcTime` 作为 `CmsSgcb.tActEdt` 字段时，`CmsSequence.syncToInner()` 走 `w.syncToInner()` + `inner._v.put(innerKey, w.inner._v)` 路径，需要确保 `w.inner._v` 包含正确的编码格式
- [ ] **建议**：添加单元测试覆盖 `CmsSgcb` 的 encode→decode→compare 循环

---

## 四、其他改进

### 4.1 代码风格一致性

- [ ] **问题**：部分子类在构造函数中手动初始化字段，部分依赖 `injectFields()`，风格不统一
- [ ] **建议**：制定明确的编码规范并在 `CODING_STYLE.md` 中更新

### 4.2 添加 `CmsData` ARRAY/STRUCTURE 的 `@Choice` 注册

- [ ] **问题**：`CmsData` 的 `alt_sequence` 虽然通过 override 工作，但 `CmsChoice.choice(int)` 中 `variantByIndex.get(v)` 返回 `null`，依赖执行顺序
- [ ] **建议**：在 `CmsData` 构造函数中调用 `registerNullChoice(1, "array")` 和 `registerNullChoice(2, "structure")`，使 `variantByIndex` 和 `variantByName` 包含这两个变体

### 4.3 单元测试覆盖

- [ ] **建议**：为以下场景添加 encode→decode→compare 循环测试：
  - `CmsData` 的 ARRAY/STRUCTURE 变体
  - `CmsSgcb` 包含 `CmsUtcTime` 的 `tActEdt`
  - `CmsData` 包含 `CHOICE_QUALITY`
  - 大嵌套 SEQUENCE OF 的 decode 性能

---

## 优先级说明

| 优先级 | 标记 | 对应项 |
|--------|------|--------|
| P0 | 红色 | 影响正确性的 Bug（3.1, 3.2） |
| P1 | 橙色 | 设计不合理或可维护性问题（1.1, 1.2, 1.4, 1.5） |
| P2 | 黄色 | 性能优化（2.1, 2.2, 2.3, 2.4） |
| P3 | 绿色 | 代码风格、文档、测试补充（4.x） |
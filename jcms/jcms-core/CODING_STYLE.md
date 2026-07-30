# jcms-core 编码风格指南

本文档描述 Cms Sequence / Choice / Scalar / Bits 等类型的标准写法。

---

## 一、核心原则

### 1. 拷值不换引用（拷值模式）

**setter 永远不替换 wrapper 对象，只往里拷值。**

```java
// ✅ 正确
public CmsMySeq fieldA(CmsSomeType v) { this.fieldA.value(v); return this; }
//                        ─────────    ─────────────────
//                        传进来       拷进去，fieldA 引用不变

// ❌ 错误
public CmsMySeq fieldA(CmsSomeType v) { this.fieldA = v; return this; }
//                                      引用替换 inner 断了
```

原因：wrapper 在构造时就绑定了自己的 `inner` 引用，替换 wrapper 会导致新旧对象的 inner 树断裂。

### 2. 整体赋值要看 `isPresent`

对于 optional 字段，整体赋值时只拷已设置的：

```java
public CmsMySeq value(CmsMySeq v) {
    this.mandatoryField.value(v.mandatoryField);
    if (v.optField.isPresent()) this.optField.value(v.optField.value());
    return this;
}
```

### 3. CHOICE 必须显式选 variant

```java
// ❌ 错误：alt_sequence 只是数据，不自动设 choice
CmsData d = new CmsData();
d.alt_sequence.add(...);

// ✅ 正确：先选 choice
CmsData d = new CmsData().choice(CmsData.CHOICE_ARRAY);
d.alt_sequence.add(...);
```

encode 时若 choice 未选会抛 `IllegalStateException`。

---

## 二、CmsScalar

值类型的基类。所有 scalar 字段的类型包装。

```java
public class CmsMyType extends CmsScalar<Integer> {
    // 构造：传入 Inner* 实例
    public CmsMyType() {
        super(new InnerMyType());
    }

    // 便捷构造（可选）
    public CmsMyType(int v) {
        super(new InnerMyType());
        value(v);
    }

    // fluent setter：super.value() 返回 CmsScalar<T>，转型回自己
    public CmsMyType value(int v) {
        super.value(v);
        return this;
    }
}
```

典型已实现：
- `CmsBoolean` → `CmsScalar<Boolean>`
- `CmsInt8` / `CmsInt16` / `CmsInt32` / `CmsInt64` → `CmsScalar<Integer/Long>`

---

## 三、CmsSequence

SEQUENCE 类型的基类。

### 基本结构

```java
public class CmsMySeq extends CmsSequence {

    // ── 字段声明（按 ASN.1 顺序）──
    @CmsField public CmsSomeScalar scalarField;
    @CmsField public CmsSomeChoice choiceField;
    @CmsField public CmsSomeSeq seqField;
    @CmsField(optional = true) public CmsOptScalar optField;  // OPTIONAL

    // ── 构造 ──
    public CmsMySeq() {
        super(new InnerMySeq());  // Rust 生成的 Inner* 类
        // 所有字段手动 new 出来
        this.scalarField = new CmsSomeScalar();
        this.choiceField = new CmsSomeChoice();
        this.seqField = new CmsSomeSeq();
        this.optField = new CmsOptScalar();
    }

    // ── Fluent setter（拷值不换引用）──

    // Scalar：用 value() 拷值
    public CmsMySeq scalarField(int v) {
        this.scalarField.value(v);
        return this;
    }

    // Choice：用 value() 拷值
    public CmsMySeq choiceField(CmsSomeChoice v) {
        this.choiceField.value(v);
        return this;
    }

    // Sequence：用 value() 拷值
    public CmsMySeq seqField(CmsSomeSeq v) {
        this.seqField.value(v);
        return this;
    }

    // Optional scalar
    public CmsMySeq optField(int v) {
        this.optField.value(v);
        return this;
    }

    // ── 整体赋值 ──
    public CmsMySeq value(CmsMySeq v) {
        this.scalarField.value(v.scalarField);
        this.choiceField.value(v.choiceField);
        this.seqField.value(v.seqField);
        if (v.optField.isPresent()) this.optField.value(v.optField.value());
        return this;
    }
}
```

### Optional 字段规则

```java
// value() 方法中：
if (v.optField.isPresent()) this.optField.value(v.optField.value());
// 不检查 this.optField.isPresent() —— 只在源有值时拷

// 构造器中：手动 new 出来即可，inner 默认为 null
this.optField = new CmsOptScalar();
```

---

## 四、CmsChoice

CHOICE 类型的基类。

### 基本结构

```java
public class CmsMyChoice extends CmsChoice {

    // ── Choice index 常量（从 0 开始）──
    public static final int VAR_A = 0;
    public static final int VAR_B = 1;
    public static final int VAR_C = 2;

    // ── variant 声明（@Choice 注解）──
    @Choice(index = 0, name = "var-a", sync = Sync.SCALAR)
    public CmsScalarA altVarA;

    @Choice(index = 1, name = "var-b", sync = Sync.WRAPPER, innerField = "varB")
    public CmsSeqB altVarB;

    @Choice(index = 2, name = "var-c", sync = Sync.RAW)
    public byte[] altVarC;

    // ── 构造 ──
    public CmsMyChoice() {
        super(new InnerEmpty());  // CHOICE 通常用 InnerEmpty
    }

    // ── choice() fluent override ──
    @Override
    public CmsMyChoice choice(int v) { super.choice(v); return this; }

    // ── Fluent setter（设 choice + 拷值）──
    public CmsMyChoice altVarA(int v) {
        choice(VAR_A);
        this.altVarA.value(v);
        return this;
    }

    public CmsMyChoice altVarB(CmsSeqB v) {
        choice(VAR_B);
        this.altVarB.value(v);
        return this;
    }

    public CmsMyChoice altVarC(byte[] v) {
        choice(VAR_C);
        this.altVarC = v.clone();
        return this;
    }

    // ── 整体赋值 ──
    public CmsMyChoice value(CmsMyChoice v) {
        int ch = v.choice();
        super.choice(ch);
        switch (ch) {
            case VAR_A: this.altVarA.value(v.altVarA); break;
            case VAR_B: this.altVarB.value(v.altVarB); break;
            case VAR_C: this.altVarC = v.altVarC.clone(); break;
        }
        return this;
    }
}
```

### Sync 类型对照

| Sync 类型 | 适用场景 | 示例 |
|-----------|----------|------|
| `SCALAR` | 基本类型包装（int, boolean 等） | `CmsBoolean`, `CmsInt32` |
| `WRAPPER` | 嵌套的 CmsSequence / CmsChoice | `CmsUtcTime`, `CmsBrcb` |
| `RAW` | 裸 byte[] | `CmsData.alt_bit_string` |
| `INNER` | DefaultInner* 类型 | `DefaultInnerOctetString` |
| `LIST` | SEQUENCE OF 子元素 | `alt_sequence` 等 |
| `ARRAY` | Java 数组 | — |

`innerField`：WRAPPER / INNER 类型需要指定 Inner* 类中对应的字段名。SCALAR / RAW / LIST 不需要。

---

## 五、CmsBits（位串）

BIT STRING 类型的基类。

```java
public class CmsMyBits extends CmsBits {

    // 按位声明字段
    public boolean bit_one;
    public boolean bit_two;
    public boolean bit_three;

    public CmsMyBits() {
        super(3);  // 总位数
    }

    // 整体赋值
    public CmsMyBits value(CmsMyBits v) {
        this.bit_one = v.bit_one;
        this.bit_two = v.bit_two;
        this.bit_three = v.bit_three;
        return this;
    }

    // 同时设全部（可选）
    public CmsMyBits value(boolean b1, boolean b2, boolean b3) {
        this.bit_one = b1;
        this.bit_two = b2;
        this.bit_three = b3;
        return this;
    }
}
```

CmsBits 不继承 CmsScalar（它映射的不是单值），所以 `value()` 写法不同。

---

## 六、特殊类型：CmsData

CmsData 的 CHOICE_ARRAY / CHOICE_STRUCTURE 使用了 `alt_sequence`（`List<CmsData>`），这是非 @Choice 字段，需要手动处理。

### 正确用法

```java
// 构造 array
CmsData arr = new CmsData().choice(CmsData.CHOICE_ARRAY);
arr.alt_sequence.add(new CmsData().alt_int32(1));
arr.alt_sequence.add(new CmsData().alt_boolean(true));

// 构造 structure（同理）
CmsData st = new CmsData().choice(CmsData.CHOICE_STRUCTURE);
st.alt_sequence.add(new CmsData().alt_float64(3.14));
```

### 整体赋值

```java
CmsData a = new CmsData().choice(CmsData.CHOICE_ARRAY);
a.alt_sequence.add(new CmsData().alt_int32(42));

CmsData b = new CmsData();
b.value(a);  // 自动处理 ARRAY/STRUCTURE 的序列拷贝
```

CmsData 的 `value()` 和 `syncToInner()` / `syncFromInner()` 已经重载，可以正确处理。

---

## 七、CmsSequence 中的 CmsChoice 字段

当 SEQUENCE 的 Inner* 类有具体 Inner 字段时，`injectFields` 会替换 CmsChoice 的 `inner` 引用。这时需要在 `injectFields` 后调用 `rebindChoiceWrappers()`。

框架已经自动处理，你只需要：

```java
// 这已经在 CmsSequence 中自动完成
@CmsField public CmsCbValueChoice value;  // CHOICE 字段
```

### 在 CmsCbValueEntry 中的示例

```java
public class CmsCbValueEntry extends CmsSequence {

    @CmsField public CmsSubReference reference;
    @CmsField public CmsCbValueChoice value;

    public CmsCbValueEntry() {
        super(new InnerAnonymous...());
        this.reference = new CmsSubReference();
        this.value = new CmsCbValueChoice();
    }

    public CmsCbValueEntry reference(String v) { this.reference.value(v); return this; }
    public CmsCbValueEntry value(CmsCbValueChoice v) { this.value.value(v); return this; }
}
```

---

## 八、Factory 模式（CHOICE + 构造器）

某些 PDU Error 类型用 CmsServiceError 构造：

```java
public class CmsMyError extends CmsSequence {
    public CmsMyError() { ... }

    // 工厂构造器
    public CmsMyError(CmsServiceError err) {
        this();  // 先调默认构造
        this.error.value(err);
    }
}
```

---

## 九、常见错误

### 1. setter 替换了 wrapper 引用

```java
// ❌
public CmsMySeq field(CmsSomeType v) { this.field = v; return this; }

// ✅
public CmsMySeq field(CmsSomeType v) { this.field.value(v); return this; }
```

### 2. CHOICE 没选 variant 就 encode

```java
// ❌
CmsData d = new CmsData();
d.alt_sequence.add(...);  // 没设 choice
d.encode();               // 抛 IllegalStateException

// ✅
CmsData d = new CmsData().choice(CmsData.CHOICE_ARRAY);
d.alt_sequence.add(...);
d.encode();               // 正常
```

### 3. value() 方法忘记调用 `super.choice(ch)`

```java
// ❌
public CmsMyChoice value(CmsMyChoice v) {
    switch (v.choice()) {
        case VAR_A: this.altVarA.value(v.altVarA); break;
    }
    return this;
}

// ✅
public CmsMyChoice value(CmsMyChoice v) {
    int ch = v.choice();
    super.choice(ch);     // 必须先设 choice
    switch (ch) {
        case VAR_A: this.altVarA.value(v.altVarA); break;
    }
    return this;
}
```

### 4. Optional 字段整体赋值不检查 isPresent

```java
// ❌
this.optField.value(v.optField.value());  // v 没设值时抛 NPE

// ✅
if (v.optField.isPresent()) this.optField.value(v.optField.value());
```

---

## 十、模板速查

### CmsScalar

```java
public class CmsXxx extends CmsScalar<T> {
    public CmsXxx() { super(new InnerXxx()); }
    public CmsXxx value(T v) { super.value(v); return this; }
}
```

### CmsSequence

```java
public class CmsXxx extends CmsSequence {
    @CmsField public CmsTypeA fieldA;
    @CmsField(optional=true) public CmsTypeB fieldB;
    public CmsXxx() {
        super(new InnerXxx());
        this.fieldA = new CmsTypeA();
        this.fieldB = new CmsTypeB();
    }
    public CmsXxx fieldA(...) { this.fieldA.value(...); return this; }
    public CmsXxx fieldB(...) { this.fieldB.value(...); return this; }
    public CmsXxx value(CmsXxx v) {
        this.fieldA.value(v.fieldA);
        if (v.fieldB.isPresent()) this.fieldB.value(v.fieldB.value());
        return this;
    }
}
```

### CmsChoice（最常用，参考 CmsCbValueChoice / CmsData）

```java
public class CmsXxx extends CmsChoice {
    public static final int VAR_A = 0, VAR_B = 1;
    @Choice(index = 0, name = "a", sync = Sync.SCALAR)  public CmsTypeA altA;
    @Choice(index = 1, name = "b", sync = Sync.WRAPPER, innerField = "b") public CmsTypeB altB;
    public CmsXxx() { super(new InnerEmpty()); }
    @Override public CmsXxx choice(int v) { super.choice(v); return this; }
    public CmsXxx altA(int v) { choice(VAR_A); this.altA.value(v); return this; }
    public CmsXxx altB(CmsTypeB v) { choice(VAR_B); this.altB.value(v); return this; }
    public CmsXxx value(CmsXxx v) {
        int ch = v.choice();
        super.choice(ch);
        switch (ch) {
            case VAR_A: this.altA.value(v.altA); break;
            case VAR_B: this.altB.value(v.altB); break;
        }
        return this;
    }
}
```

### CmsBits

```java
public class CmsXxx extends CmsBits {
    public boolean bit1, bit2;
    public CmsXxx() { super(2); }
    public CmsXxx value(CmsXxx v) {
        this.bit1 = v.bit1; this.bit2 = v.bit2;
        return this;
    }
}
```

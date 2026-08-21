# jcms-core — Java 侧类型系统核心

## 职责

jcms-core 是整个 jcms 的**类型系统核心**：为 `jcms-data` 自动生成的 `Inner*` 数据类提供一层**类型安全、可链式调用、可读写的 Java 包装**。

一句话概括：**`Inner*` 是"数据"，`Cms*` 是"操作数据的视图"——编解码委托 Rust，数据只存一份，包装层零拷贝。**

- 编解码不在 jcms-core：由 `Inner*` 委托 `asn1.dll`（Rust）完成。
- 数据不在 jcms-core：永远只存一份在 `Inner*._v`（`LinkedHashMap`）里。
- jcms-core 的价值在于**让上层用起来像操作普通 Java 对象**，而不是裸的 Map。

## 模块布局

```
com.ysh.jcms.core
├── data/core/        CmsType / CmsSequence / CmsChoice / CmsScalar /
│                     CmsBits / CmsEnum / CmsNull + 注解 CmsField
├── data/scalar/      CmsInt32、CmsBoolean、CmsOctetString、CmsObjectReference…
├── data/choice/      CmsData（协议通用 Data CHOICE）、CmsCbValueChoice…
├── data/bitarray/    CmsQuality、CmsCheck、CmsReasonCode、CmsTriggerConditions…
├── data/enumerate/   CmsServiceError、CmsDbpos、CmsTcmd…
├── data/sequence/    CmsUtcTime、CmsAuthenticationParameter、CmsBrcb…
├── pdu/              按服务分类的报文段（connection/data/dataset/directory/…）
├── info/             CmsServiceInfo（服务码表元数据）、CmsCdcInfo 等
└── util/             CmsBytesUtil、CmsDataUtil、CmsFormatUtil、CmsPrinter
```

## 设计重点

### 1. 单一数据源 `inner._v`

所有 `Cms*` 类型都包装一个 `Inner*` 实例，挂在自己的 `inner` 字段上。数据的**唯一真相**是 `inner._v`（`InnerBase` 里的 `LinkedHashMap<String,Object>`）。

- `CmsType` 是万物基类，只做两件事：持有一个 `inner`，把 `encode()`/`decode()` 透传给 `inner`。
- 标量 `CmsInt32.value(42)` 写的是 `inner._v["_"] = 42`，不是某个 Java 字段。
- 因此**没有任何数据是"双份"的**——Java 侧字段只用于结构描述和便捷访问，最终都落在 `_v` 里，编码时直接序列化 `_v`。

### 2. 注解驱动 + 反射注入（声明式，零样板）

子类**只声明"长什么样"，不写任何编解码/序列化代码**。基类构造器通过注解反射，自动 new 出子包装并建立 `_v` 共享。

| 注解 | 作用 | 挂在哪 |
| --- | --- | --- |
| `@CmsField` | 标记 SEQUENCE 字段，`optional`/`sequenceOf`/`inner` 描述字段 | `CmsSequence` 子类的 public 字段 |
| `@Choice` | 标记 CHOICE variant，`index`/`name`/`sync` 描述变体 | `CmsChoice` 子类的 `alt_*` 字段 |
| `@Bit` | 标记 BIT STRING 的位，`value`/`length` 描述位区间 | `CmsBits` 子类的 `boolean`/`int` 字段 |
| `@ValueRange` | 枚举取值范围校验 | `CmsEnum` 子类 |

反射结果用 `ClassValue` 缓存（每类只扫一次），避免每次构造都扫字段。这是 jcms-core 的"省心"来源：加一个字段 = 加一行声明，其余自动。

### 3. 共享 `_v` 与「拷值不换引用」

这是最核心、也最容易被误用的机制。

**共享 `_v`**：父容器构造子包装时，把子包装的 `inner._v` 直接指向父 `_v` 里对应的那个子 Map（同一个引用），而不是复制一份。因此写子字段 = 直接写进父 Map，天然零拷贝、天然同步。

**拷值不换引用**：所有 setter 都是 `this.field.value(v)`（往里拷值），**绝不** `this.field = v`（换引用）。因为 wrapper 的 `inner` 引用在构造时就绑定了；一旦替换，新旧对象的 `_v` 树就断了，编码会丢数据。

```java
// ✅ 拷值：field 的 inner 引用不变，只改 _v 内容
public CmsSeq field(CmsX v) { this.field.value(v); return this; }

// ❌ 换引用：field 指向新对象，父 _v 里还是旧 Map，编码丢字段
public CmsSeq field(CmsX v) { this.field = v; return this; }
```

### 4. 双向同步协议 + 双 JSON 出口

`_v` 共享覆盖了大部分情况，但有两类例外需要显式同步，于是基类定义了一组固定钩子：

- `syncToInner()`：Java 字段 → `_v`。`CmsBits` 要把散落的 boolean 位打包成 hex 字符串；`CmsChoice` 要写入 `_choice` 与选中值。
- `syncFromInner()`：`_v` → Java 字段。解码后把 hex 拆回位字段。
- `rebind()`：`decode()`/`fromInnerJson()` 会**整体替换** `inner` 实例，此时所有子包装的共享 `_v` 引用都指向了旧 Map，必须重建。

调用时机统一：`encode()` 前 `syncToInner()`，`decode()` 后 `rebind()` + `syncFromInner()`。子类通常无需关心，基类已编排好。

**双 JSON 出口**——jcms-core 对上层暴露两种 JSON，各司其职：

| JSON | 方法 | 形态 | 用途 |
| --- | --- | --- | --- |
| domain JSON | `toJson()` / `fromJson()` | 人类友好：`{"int32":42}`、`{"fileName":"a.txt"}` | CLI / SCL / HTTP API |
| JER JSON | `fromInnerJson()` | ASN.1 风格：`{"_choice":"int32","_":42}` | 与 Rust FFI 交换的中间表示 |

domain JSON 走 `toJsonValue()`/`fromJsonValue()`（子类按字段语义展开），JER JSON 直接映射 `_v`。

## 典型结构速写

```java
// SEQUENCE：声明字段 + 构造传 Inner，其余自动
public class CmsAssociateRequest extends CmsSequence {
    @CmsField(optional = true) public CmsString serverAccessPointReference;
    public CmsAssociateRequest() { super(new InnerAssociateRequestPDU()); }
    public CmsAssociateRequest serverAccessPointReference(String v) {
        this.serverAccessPointReference.value(v);
        setPresent("serverAccessPointReference", v != null);
        return this;
    }
}

// CHOICE：@Choice 标注 variant，alt_*() 自动选 choice + 拷值
public class CmsData extends CmsChoice {
    @Choice(index = 6, name = "int32", sync = Sync.SCALAR) public CmsInt32 alt_int32;
    public CmsData alt_int32(int v) { choice(CHOICE_INT32); this.alt_int32.value(v); return this; }
}
```

完整写法约定见 [CODING_STYLE.md](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-core/CODING_STYLE.md)。

## 与上下层的关系

| 模块 | 关系 |
| --- | --- |
| jcms-data（下层） | 提供 `Inner*` 数据类 + `asn1.dll` 编解码；jcms-core 只做包装 |
| jcms-utils（同层） | 复用 `CmsData` 等类型做 SCL 解析 / 配置 |
| jcms-app（上层） | 用 `Cms*` 类型 + 链式 setter 组报文，`toJson()` 输出 |

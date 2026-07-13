# jcms-core — Java 侧核心基础层

## 职责

jcms-core 是整个 jcms 项目的**基石**，负责将 C 语言编写的 ccms 动态库（PER 编码/解码 + 基础数据结构）映射到 Java 世界，并定义所有上层类型共同依赖的基类体系。

一句话概括：**ccms 的 Java 胶水层 + 类型系统基类**。

> 不是AI生成的内容：jcms-core主要就是四部分
>
> （1）NativeBridge：负责把代码层面的java调用连接到c函数调用，整体结构简单。
>
> （2）CmsType（核心）：一切结构的核心，万物的基类，调用NativeBridge，负责实现自动重试机制，子类只需要说明好一个数据的结构就好，编码解码的各种细节都不用考虑了。
>
> （3）CmsArray、CmsChoice、CmsEnumerated、CmsCodedEnum：很多子类更细分的基类，做一个整体实现的规范，也为了降低CmsType的复杂度。
>
> （4）CmsFormatUti、CmsEqualityUtil：一些工具函数，同样为了简化CmsType的复杂度，把一些功能拆解出来。

## 整体架构

```
┌─────────────────────────────────────────────────┐
│              jcms-data / jcms-svc                │  ← 具体数据类型、服务报文段
├─────────────────────────────────────────────────┤
│                  jcms-core                       │  ← 基类 + FFI 桥接
│  ┌───────────┐ ┌───────────┐ ┌───────────────┐  │
│  │  CmsType   │ │ CmsArray  │ │ CmsChoice      │  │
│  │  (基类)    │ │ (SEQUENCE │ │ (CHOICE 基类)  │  │
│  │           │ │  OF)      │ │               │  │
│  └─────┬─────┘ └───────────┘ └───────────────┘  │
│  ┌─────┴─────┐ ┌───────────┐ ┌───────────────┐  │
│  │CmsEnumerated│ │CmsCodedEnum │ │NativeBridge  │  │
│  │(枚举标量)  │ │(CODEDENUM) │ │(FFI 桥接)    │  │
│  └───────────┘ └───────────┘ └───────────────┘  │
│  ┌───────────┐ ┌───────────┐                    │
│  │CmsFormatUtil│ │CmsEqualityUtil│               │
│  │(格式化工具)│ │(等值比较)   │                   │
│  └───────────┘ └───────────┘                    │
├─────────────────────────────────────────────────┤
│                  ccms (C 动态库)                  │  ← PER 编解码 + C 数据结构
└─────────────────────────────────────────────────┘
```

## 核心类详解

### 1. `CmsType` — 一切类型的基类

包路径：`com.ysh.jcms.core.CmsType`

这是整个 jcms 类型系统的根。所有数据类型（标量、结构体、数组、CHOICE 等）都直接或间接继承自它。它定义了三种核心能力：

#### 内存管理（Java ↔ Native 同步）

每个 `CmsType` 实例在堆外（native memory）持有一块对应 C 结构体的内存：

- **构造时**：通过 `calcNativeSize()` 计算需要的 C 结构体大小，调用 JNA 的 `new Memory(nativeSize)` 分配堆外内存。
- **`write()`**：将 Java 侧的字段值写入 native 内存。容器类型递归调用子节点的 `write()`，然后将子节点的 `nativePtr` 指针写入父节点内存的对应槽位。
- **`read()`**：从 native 内存读取值回 Java 字段。容器类型从父节点内存读取子节点指针，赋值给子节点的 `nativePtr`，再递归调用子节点的 `read()`。
- **`zero()`**：将 native 内存全部置零。

#### PER 编解码编排

`CmsType` 本身**不执行** PER 编解码，而是通过 `NativeBridge.Codec` 代理给 C 动态库：

- **`encode()`**：先调用 `write()` 确保 Java 状态已同步到 native 内存，然后调用 `codec.encode(nativePtr)`。
- **`decode(byte[] data)`**：包含**自动重试机制**——如果 C 侧返回 `CMS_RETRY (-2)`（意味着 SEQUENCE OF 数组元素数超过预分配槽位），Java 会读取 C 侧写的实际 `count`，调用 `resize()` 扩容，然后重试解码，最多 200 次。

#### 子类扩展点

| 方法                 | 用途               | 默认行为                                      |
| ------------------ | ---------------- | ----------------------------------------- |
| `children()`       | 返回子字段列表          | 空列表（叶节点）                                  |
| `calcNativeSize()` | 计算 native 内存大小   | `children().size() * 8`（容器类型，每个子节点一个指针槽位） |
| `write()`          | Java → Native 同步 | 遍历 children 递归写                           |
| `read()`           | Native → Java 同步 | 遍历 children 递归读                           |
| `resizeList()`     | 解码重试时哪些子节点需要扩容   | 全部 children（CHOICE 可重写为只返回选中分支）           |

***

### 2. `CmsArray<T>` — 泛型数组容器（SEQUENCE OF）

包路径：`com.ysh.jcms.core.CmsArray`

对应 C 结构 `cms_array_t`（`void** elements + int32_t count`，共 16 字节），用于表达 ASN.1 中的 SEQUENCE OF 类型。

关键特性：

- **泛型**：`CmsArray<CmsBoolean>`、`CmsArray<CmsData>` 等。
- **自动扩容解码**：通过 `allocSize` 控制预分配槽位。解码时若 C 侧发现元素数超过 `allocSize`，返回 `CMS_RETRY`，Java 侧读取实际 count、调整 `allocSize`、创建新元素实例、重试。
- **`itemClass`**：元素类型的 Class 对象，用于解码时反射自动创建子元素实例。若不指定则需要手动预填充。

***

### 3. `CmsChoice` — CHOICE 基类

包路径：`com.ysh.jcms.core.CmsChoice`

CHOICE 类型（多选一）的抽象基类。仅提供一个 `choice` 字段（`CmsEnumerated` 实例）作为选择器。子类自行管理全部指针布局。

> `resizeList()` 重写使其只返回选中分支对应的子节点，避免对未选中的整棵子树做无意义的扩容。

***

### 4. `CmsEnumerated` — 枚举标量

包路径：`com.ysh.jcms.core.CmsEnumerated`

对应 ASN.1 的 `ENUMERATED` 类型，内部映射为 `Int8 (-128..127)`，native size = 4 字节。是所有枚举类的基类（`CmsDbpos`、`CmsTcmd`、`CmsServiceError`、`CmsOrCat` 等均继承自它）。

支持构造时做范围校验（`CmsEnumerated(min, max, value)`）。

***

### 5. `CmsCodedEnum` — CODEDENUM 类型

包路径：`com.ysh.jcms.core.CmsCodedEnum`

对应 `CODEDENUM ::= BIT STRING (SIZE(0..n))`（§7.1.7）。直接继承自 `CmsUint8Array`，`len` 字段存储 bit 数。

***

### 6. `NativeBridge` — FFI 桥接层

包路径：`com.ysh.jcms.core.NativeBridge`

这是连接 Java 和 C 动态库（`ccms.dll`/`ccms.dylib`/`ccms.so`）的桥梁，基于 **JNA** 实现。

核心设计：`Codec` 枚举。

- 每个枚举条目对应一对 C 函数：`cms_{name}_encode` 和 `cms_{name}_decode`。
- 共约 **250 个条目**，覆盖 DL/T 2811 协议中所有需要编解码的数据结构和服务报文段。
- 每个条目提供三个方法：
  - `encode(Pointer ptr)` → `byte[]`：编码，输出自动释放
  - `decode(Pointer ptr, byte[] data)`：解码，失败抛异常
  - `decodeRaw(Pointer ptr, byte[] data)` → `int`：解码，返回原始 rc 码（供 `CmsType.decode()` 的重试逻辑使用）

***

### 7. `CmsFormatUtil` — 格式化工具

包路径：`com.ysh.jcms.core.CmsFormatUtil`

将 `CmsType` 树渲染为人类可读的字符串或机器可读的 JSON：

- **`toString(type, depth, fieldNames)`**：YAML 风格的层级输出，用于调试。通过反射获取 Java 字段名作为子节点名称。
- **`toJson(type)`**：紧凑 JSON 输出。`CmsArray` → JSON 数组，容器 → JSON 对象，CHOICE → 只输出选中分支，`CmsUint8Array` → JSON 字符串（可打印文本直接输出，二进制转 `hex:` 前缀）。

***

### 8. `CmsEqualityUtil` — 等值与哈希

包路径：`com.ysh.jcms.core.CmsEqualityUtil`

`CmsType` 树的深度等值比较和哈希计算：

- **容器类型**：递归比较每个子节点。
- **叶节点**：直接比较 native 内存字节（按 `nativeSize` 分 1/2/4/8 字节对齐比较，避免分配临时数组）。
- **`CmsUint8Array`** **子类**：按内容比较而不是按具体子类比较，避免因 `CmsObjectReference` vs `CmsObjectName` 等同类不同名导致的误判。

***

## `CmsType.decode()` 自动重试机制

这是理解编码解码流程的关键：

```
decode(byte[] data)
    │
    ├─ allocate()       ← 分配新 native 内存
    ├─ write()          ← Java → Native 同步
    ├─ codec.decodeRaw() ← 调用 C 侧解码
    │
    ├─ rc == 0 (OK) ─── read() → 返回
    ├─ rc == -2 (CMS_RETRY)
    │     └─ resize() ← 读取 C 侧写的实际 count
    │         └─ CmsArray.resize() ← 扩容 items
    │            └─ 递归子节点
    │     └─ 回到 allocate() 重试
    └─ rc < 0 (其他错误) ─── 抛异常
```

为什么需要重试？因为 C 侧 `SEQUENCE OF` 解码时，需要 Java 侧预分配足够多的元素槽位。如果不够，C 侧先写一个 `count` 到 native 内存，然后返回 `-2`，Java 侧扩容后再试。

> 不是AI生成的内容：这样做的好处是，之前采用自动重试机制之前，java不知道要预留多大的空间，每次都按照最大空间来分配，但是对于嵌套结构而言，三层的嵌套就会直接爆炸，但是三层嵌套在协议中会有遇到。所以就设计了自动重试机制，默认不预留空间，c进行解码发现没空间就把长度写好，先返回去，让java预留恰好匹配的空间。然后这一层满足了，如果内部还有子列表，一样重试。这样可以支持线性的复杂度，比如三层嵌套的结构，理论上只需要重试三次，空间也是分配成恰好匹配的大小，时间上和空间上的代价都可以接收了。

***

## 与上层模块的关系

| 模块            | 依赖关系                                                      |
| ------------- | --------------------------------------------------------- |
| **jcms-data** | 继承 `CmsType`、`CmsArray`、`CmsEnumerated` 等基类，实现 §7 的所有数据结构 |
| **jcms-svc**  | 继承 `CmsType`、`CmsArray`、`CmsChoice` 等基类，实现 §8 的所有服务报文段结构  |
| **jcms-app**  | 通过 `NativeBridge.Codec` 调用编解码，使用 `CmsFormatUtil` 格式化输出    |
| **ccms (C)**  | `NativeBridge` 通过 JNA 调用 ccms 动态库的编解码函数                   |


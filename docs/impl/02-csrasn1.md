# 02 csasn1 — 多语言 Bean 代码生成器

用 rasn 把 ASN.1 规约打包成 `asn1.dll`，并生成 Java / Python 的 Bean 类。

## 1. 使用说明

### 1.1 怎么用（99% 的情况只需要这一行）

改完 `csasn1/specs/dlt2811.asn` 后，在 `cms/` 根目录跑：

```powershell
just single-j-data-gen
```

这一行会依次：

1. 用 rasn-compiler 把 `.asn` 编译成 Rust 类型（`generated.rs`）
2. 生成 FFI 分发层（`ffi_auto.rs`）并编译出 `asn1.dll`
3. 生成 Java `Inner*` 类到 `jcms/jcms-data`
4. 把 `asn1.dll` 部署到 `jcms-data/src/main/resources/win32-x86-64/`

**规则：只改 `.asn` 文件，其余全自动。** 生成的 `generated.rs` / `ffi_auto.rs` 不要手动编辑，下次生成会被覆盖。

> `single-j-data-gen` 定义在 `cms/justfile`，实际调用 `scripts/single-j-data-gen-win.ps1`：
> 进入 `csasn1/` → 删除旧 `jcms-data` → `cargo run --release -- --src specs/dlt2811.asn --dest ../jcms/jcms-data --prefix Inner --enc aper --package com.ysh.jcms.data` → 拷贝 DLL。

### 1.2 其他用法（不常用）

csasn1 目录内也有一个 justfile，用于**独立调试/测试**（产出到 `assets/`，不碰 jcms-data）：

```powershell
just build            # 编译 Rust（DLL + CLI）
just gen-java         # 生成 Java 类到 assets/java
just gen-python       # 生成 Python 包到 assets/python
just gen-all          # build + 生成全部
just test-java        # 生成 Java + mvn test
just test-python      # 生成 Python + pixi run test
just rust-all         # 列出所有 ASN.1 类型名
just jer <类型> <json>  # 查看某类型的 JER "上帝格式"
```

手动等价 CLI（`--lang python` 即生成 Python）：

```powershell
cargo run --release -- --lang java --src specs/dlt2811.asn \
  --dest assets/java --prefix Inner --enc aper --package com.ysh.jcms.data
```

## 2. 模块介绍

### 2.1 模块档案

| 项目 | 内容 |
| --- | --- |
| 目录 | `cms/csasn1`（本项目编写，Rust） |
| Cargo 包名 | `csasn1`，库名 `asn1`（`crate-type = ["cdylib", "lib"]`） |
| 依赖 | rasn 0.28（patch 到本地 `../rasn`）、syn、quote、serde_json；build 依赖 rasn-compiler 0.16 |
| 唯一入口 | `csasn1/specs/dlt2811.asn`（相对 cms 根；改规范只改这一个文件） |
| 产物 | `asn1.dll`（FFI 动态库）+ 生成的 Bean 类（`jcms-data` / `assets/java` / `assets/python`） |

### 2.2 目录结构

```
csasn1/
├── specs/dlt2811.asn      ← 唯一要改的文件：ASN.1 规约
├── build.rs               ← 编译期自动生成 generated.rs + ffi_auto.rs
├── asn1.def               ← Windows DLL 导出符号表（4 个函数）
├── src/
│   ├── main.rs            ← CLI（代码生成器入口）
│   ├── lib.rs             ← 库入口（编译为 asn1.dll）
│   ├── generated.rs       ← 自动生成（Rust 类型，勿手动编辑）
│   ├── ffi_auto.rs        ← 自动生成（FFI 分发，勿手动编辑）
│   └── generator/
│       ├── mod.rs         ← 类型提取（syn 解析 AST → TypeInfo）
│       ├── java/          ← Java 生成器
│       └── python/        ← Python 生成器
├── examples/jer_god.rs    ← JER "上帝格式" 调试示例
├── scripts/list_types.ps1 ← 列出所有生成类型
└── assets/                ← 独立测试产物（java / python，可重新生成）
```

### 2.3 工作原理

#### 数据流

```
specs/dlt2811.asn ──build.rs① rasn-compiler──▶ src/generated.rs（Rust 类型）
      ──build.rs② 扫描类型名──▶ src/ffi_auto.rs（FFI 分发）──▶ asn1.dll
      ──main.rs③ 代码生成器（syn 解析 AST）──▶ Java / Python Bean 类
```

运行时（Java 侧）：编码 = Java 对象 → JSON → `csasn1_encode` → APER 二进制；解码相反。**所有语言通过 JSON 作为中间表示交换数据，Java/Python 完全不碰位操作。**

#### 三个生成环节

**① build.rs** —— 编译期自动执行两步：rasn-compiler 把 `.asn` 编译成 `generated.rs`；再正则扫描类型名，为每个类型生成 `csasn1_encode/decode` 的 match 分支到 `ffi_auto.rs`（含 Jackson↔JER 适配）。

**② lib.rs → asn1.dll** —— 导出 4 个 C 函数（见 `asn1.def`）：

```c
char* csasn1_ping(void);                                       // 返回 "pong"
char* csasn1_encode(const char* type_name, const char* encoding, const char* json);
char* csasn1_decode(const char* type_name, const char* encoding, const uint8_t* data, size_t len);
void  csasn1_free_string(char* s);                             // 释放返回的 JSON 字符串
```

返回 JSON：encode → `{"ok": true, "bytes": [...]}`，decode → `{"ok": true, "value": ...}`。编码方式支持 `ber`（默认）/ `der` / `aper`（**本项目用**）/ `uper`。

**③ main.rs** —— CLI 代码生成器，用 syn 解析 `generated.rs` 的 AST，识别 newtype（`delegate`）/ struct（SEQUENCE）/ enum（CHOICE）三类，分派到 Java / Python 生成器。

| 参数 | 默认 | 说明 |
| --- | --- | --- |
| `--lang` | `java` | `java` / `python` |
| `--src` | `specs/dlt2811.asn` | `.asn` 自动映射到 `generated.rs` |
| `--dest` | `java/src` | 输出目录 |
| `--prefix` | `Cms` | 类名前缀（**本项目用 `Inner`**） |
| `--enc` | `ber` | 生成时固定的编码方式（**本项目用 `aper`**） |
| `--package` | 空 | Java 包名（**`com.ysh.jcms.data`**） |

#### Java / Python 生成产物

**Java**（`generator/java/`）：每个类型一个 `Inner*` POJO（数据统一存 `_v`）+ `InnerBase`（基类，`encode()`/`decode()`）+ `InnerNative`（JNA 调 DLL）+ `V`（`_v` 助手）+ `DefaultInner*`/`InnerEmpty` + 自动测试 + `pom.xml`。`encode()` 严格（只编码 `_set` 标记的 OPTIONAL），`encodeTest()` 宽松（全字段）。依赖 Jackson + Lombok + JNA。

**Python**（`generator/python/`）：`_native.py`（ctypes）+ `_base.py` + `_types.py`（dataclass，按拓扑排序）+ `__init__.py` + 自动测试 + `pixi.toml`。

### 2.4 与 jcms 其他模块的关系

- **rasn** ← csasn1 的直接依赖（编解码引擎，见 01-rasn.md）。
- **jcms-data** ← `just single-j-data-gen` 生成的 `Inner*` 类 + `InnerNative`（JNA 调 asn1.dll）。
- **jcms-core / utils / app** ← 通过 jcms-data 间接使用，不直接接触 csasn1。

### 2.5 注意事项

- **命名**：目录与 Cargo 包名是 `csasn1`，口头有时称 "csrasn"。
- **前缀 `Inner`**：jcms-data 的生成类统一用 `Inner` 前缀（`--prefix Inner`），README 示例里的 `Cms` 只是演示。
- **编码固定 `aper`**：生成时 `--enc aper` 把编码方式固化进生成代码，运行时无需再指定。
- **DLL 位置**：生成器自动把 `asn1.dll` 拷到 `jcms-data/src/main/resources/win32-x86-64/`（及 `assets/java`、`assets/python` 对应目录）。

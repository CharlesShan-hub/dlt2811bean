# Configuration Mapping Specification（CMS）

---

## CMS概述

### 概述

**CMS（通信报文规范）**就是国家电网为了摆脱对国外技术的依赖，专门搞出来的**“MMS协议国产替代版”**。它的核心使命是实现电力通信的**自主可控**，同时解决老协议笨重、低效的问题。

* **瘦身提速（PER编码）**：把原来MMS那种臃肿的BER编码换成了紧凑的**PER编码**。
* **砍掉中间商（直接映射）**：以前MMS需要把电力指令层层转译（七层模型），现在CMS**直接映射TCP/IP**。
* **穿上防弹衣（国密加密）**：最大的亮点在于**安全性**。CMS原生支持国密算法加密。

本项目就是制作CMS标准的代码实现。主要的思路是，使用C语言编写per编码部分和基础数据结构部分。后续开发可以基于C/C++，也可以将该模块打包成dll，后续迁移到其他语言平台。项目目录包含下边内容：

* **ccms**
  * [per](docs/ccms-per.md): 底层编码，dlt2811协议使用per编码将asn1转换成字节码。
  * [data](docs/ccms-data.md): dlt2811 §7 数据结构部分的实现。
  * [svc](docs/ccms-svc.md): dlt2811 §8 报文段部分的实现。
* **jcms-core**
  * core: 负责将ccms api映射到java，并且设计基础的`CmsType`等基类。
  * info: 一些文档性质的枚举和说明。
  * data: dlt2811 §7的java封装。
  * svc: dlt2811 §8的java封装。
* **jcms-utils**
  * config: 配置模块。
  * scl: scd文件解析模块。
  * security：安全协议。
  * transport：传输层基础构建。
* **jcms-app**
  * node：dlt2811 §6传输层封装。
  * handler: 各种处理器，比如客户端，服务器，命令行交互界面。
  * console：命令行交互界面。

---

### 资料

* [国产自主可控新一代通信标准CMS之总览篇](https://zhuanlan.zhihu.com/p/520653213)

---

## 使用方法

### 8.1 连接

```bash
# 非加密连接，使用8102端口。需要输入ip和accesspoint。会自动negociate和associate。
connect 127.0.0.1 C_B5041X/S1;
# 加密连接，使用9102端口。
connect-tls 127.0.0.1 C_B5041X/S1;
# 两种连接都可以指定negociate的参数，如果不指定，就是默认16384 65531 1。
connect 127.0.0.1 C_B5041X/S1 16384 65531 1;
# 也可以只进行连接，不自动进行后续操作（没有negociate和associate）
connect 127.0.0.1;
# 断开连接
disconnect
# 退出程序
exit
```

```bash
cms> connect 127.0.0.1 C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> disconnect
  OK  Disconnected.
cms> connect-tls 127.0.0.1 C_B5041X/S1 16384 65531 1;
  TLS connecting to 127.0.0.1:9102 ...
  TLS connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  TLS associated: C_B5041X/S1
cms> associate C_B5041X/S1; # 不能重复associate
  ERR Already associated. Use 'release' or 'disconnect' first.
cms> disconnect
  OK  Disconnected.
cms> exit
Bye.
```

### 8.2.1 确定访问点

```bash
# 需要先建立tcp连接
connect 127.0.0.1;
# 再指定访问点
associate C_B5041X/S1;
```

```bash
cms> connect 127.0.0.1;
  Connecting to 127.0.0.1:8102 ...
  OK  Connected: 127.0.0.1:8102
cms> associate C_B5041X/S1;
  OK  Associated: C_B5041X/S1
```

### 8.2.2 正常释放

```bash
# release是正常释放关联，可以后续更换访问点
release
```

```bash
cms> associate C_B5041X/S1;
  OK  Associated: C_B5041X/S1
cms> release
  OK  Released.
cms> associate C_B5041X/G1;
  OK  Associated: C_B5041X/G1
```

### 8.2.3 异常释放

```bash
#客户端因为某种异常需要断开连接，不需要服务器内容。
#服务器任务客户端下线了，会关闭tcp连接
#abort <reason>
abort 0; # 默认reason就是0 `others`
```

```bash
cms> connect-tls 127.0.0.1 C_B5041X/S1 16384 65531 1;
  TLS connecting to 127.0.0.1:9102 ...
  TLS connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  TLS associated: C_B5041X/S1
cms> abort; # tcp会关掉，后续需要重新connect
  OK  Abort sent (reason=0)
cms> associate C_B5041X/G1;
  ERR Not connected. Use 'connect' first.
```

### 8.3.1 获取逻辑设备

```bash
# 获取某一个accesspoint下边的逻辑设备
server-dir;
# 指定referenceAfter
server-dir LD0;
```

```bash
cms> server-dir;
  Logical Devices:
    [0] LD0
    [1] MEAS
    [2] CTRL
cms> server-dir LD0;
  Logical Devices:
    [0] MEAS
    [1] CTRL
```

### 8.3.2 获取指定逻辑设备下的所有逻辑节点

```bash
# ld-dir <ldName> <referenceAfter>;
# LD0这个设备下的所有节点
ld-dir LD0;
# LD0下边有很多节点，获取LTSM6之后的节点
ld-dir LD0 LTSM6;
```

```bash
cms> ld-dir LD0;
  Logical Nodes:
    [0] LLN0
    [1] LPHD1
    [2] RSYN1
    [3] GGIO1
    ... 这里省略一些
    [76] LTSM6
    [77] LTSM7
    [78] LTSM8
    [79] LTSM9
cms> ld-dir LD0 LTSM6;
  Logical Nodes:
    [0] LTSM7
    [1] LTSM8
    [2] LTSM9
```

### 8.3.3 获取逻辑节点目录

```bash
# data-object：数据对象
ln-dir LD0 data-object;
# data-set: 数据集
ln-dir LD0 data-set;
# brcb
ln-dir LD0 brcb;
# urcb
ln-dir LD0 urcb;
# gocb
ln-dir CTRL gocb;
```
* data-object
* data-set
* brcb
* urcb
* gocb
```bash
cms> connect-tls 127.0.0.1 C_B5041X/S1 16384 65531 1;
  TLS connecting to 127.0.0.1:9102 ...
  TLS connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  TLS associated: C_B5041X/S1
cms> ln-dir LD0 data-object;
  References (data-object):
    [0] Mod
    [1] Mod.stVal
    [2] Mod.q
    [3] Mod.t
    [4] Beh
    [5] Beh.stVal
    [6] Beh.q
    [7] Beh.t
    ... 省略一些
    [193] DUTSynOfs
    [194] DUTSynOfs.stVal
    [195] DUTSynOfs.q
    [196] DUTSynOfs.t
cms> ln-dir LD0 data-set;
  References (data-set):
    [0] dsAlarm
    [1] dsWarning
    [2] dsCommState
    [3] dsAin
    [4] dsAin1
    [5] dsParameter1
    [6] dsParameter2
    [7] dsParameter5
    [8] dsParameter8
cms> ln-dir LD0 brcb;
  References (brcb):
    [0] brcbAlarm
    [1] brcbWarning
    [2] brcbCommState
cms> ln-dir LD0 urcb;
  References (urcb):
    [0] urcbAin
    [1] urcbAinA
cms> ln-dir CTRL gocb
  References (gocb):
    [0] gocb0
```

* lcb
* log
```bash
cms> connect 127.0.0.1 P_B5041A/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with P_B5041A/S1 ...
  OK  Associated: P_B5041A/S1
cms> server-dir
  Logical Devices:
    [0] LD0
    [1] PROT
    [2] RCD
cms> ln-dir LD0 lcb
  References (lcb):
    [0] lcblog
cms> ln-dir LD0 log
  References (log):
    [0] LD0
cms> ln-dir LD0 sgecb
  References (sgecb):
    [0]
cms> ln-dir LD0 msvcb
  References (msvcb):
    [0]
```

### 8.3.4 获取数据值

```bash
# 1. 返回有值的内容，跳过没设置值的内容
# 2. 已经可以正常支持中文，使用unicode-string
# 3. 可以进行FC的筛选，默认是0，不筛选
# 4. 可以进行referenceAfter的筛选
all-data LD0 1 LicIP17
```

```bash
cms> connect 127.0.0.1 P_B5041A/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with P_B5041A/S1 ...
  OK  Associated: P_B5041A/S1
cms> all-data LD0 # 默认是0，不进行ST筛选
  Data values (62 items):
    [0] Mod  [visible-string] status-only
    [1] NamPlt  [unicode-string] 逻辑节点铭牌
cms> all-data LD0 1 # FC=ST
  Data values (62 items):
    [0] Mod  [visible-string] status-only
    [1] NamPlt  [unicode-string] 逻辑节点铭牌
    ...
    [59] LicIP16  [unicode-string] 白名单IP地址16
    [60] LicIP17  [unicode-string] 白名单IP地址17
    [61] LicIP18  [unicode-string] 白名单IP地址18
cms> all-data LD0 1 LicIP17 # 继续加入referenceAfter
  Data values (1 items):
    [0] LicIP18  [unicode-string] 白名单IP地址18
```

### 8.3.5 获取

### 8.15 协商

```bash
# 使用默认参数（从配置文件读取）
negotiate
# 手动指定参数
negotiate 16384 65531 1;
```

```bash
cms> negotiate
  OK  Negotiate completed.
cms> negotiate 16384 65531 1;
  OK  Negotiate completed.
```

### 8.16 测试ip

```bash
# 也可以用来实现心跳机制
test
```

```bash
cms> test
  OK  Ping/pong OK
---

## ccms

### 使用方法

本模块主要的功能是封装基础数据结构并提供对应的编码解码功能，采用c语言编写。可以将本模块打包成动态库供后续开发使用，也可以基于本模块进一步开发。

#### 构建要求

- **CMake**（≥ 3.14）
- **C 编译器**：clang 或 gcc
  - Windows 推荐 [LLVM-MinGW (UCRT)](https://github.com/mstorsjo/llvm-mingw)
  - macOS 可通过 `xcode-select --install` 安装
  - Linux 可通过系统包管理器安装

#### 构建产物

| 平台 | 输入产物 | 打包后名称 |
|------|---------|-----------|
| Windows | `libccms.dll` | `ccms.dll` |
| macOS   | `libccms.dylib` | `ccms.dylib` |
| Linux   | `libccms.so` | `ccms.so` |

> 去除 `lib` 前缀是为了适配 JNA 的 `Native.load("ccms", ...)` 加载约定。

#### 构建命令

```bash
# Windows（PowerShell）
cd ccms
.\win_ccms.ps1

# macOS / Linux
cd ccms
chmod +x ccms.sh
./ccms.sh
```

构建完成后产物输出至 `ccms/dist/` 目录。

### per

- **INTEGER**（`cms_integer.h`）
  - 没有前导码、没有 Tag、没有自带的 Length；是否对齐，只看它的取值范围（`lb..ub`）
  - 对于 2811，小的不需要对齐（比如 `Boolean`、`Quality`），其他的都需要对齐（因为 **range > 255**）
  - 不需要对齐的举例：`Boolean ::= INTEGER (0..1)`、`SmpMod ::= INTEGER (0..2)`
  - 需要对齐的举例：`Int16U ::= INTEGER (0..65535)`
- **OCTET STRING**（`cms_string.h`）
  - 永远都是对齐，长度字段视情况而定。
  - `OCTET STRING (SIZE(n))`：固定长度。不需要编码长度，因为长度已在 ASN.1 文件中规定。
  - `OCTET STRING (SIZE(lb..ub)), ub < 64K`：变长。长度为字符串实际长度。若长度超过 64K，长度编码会越界，必须改用不受约束长度决定因子。
  - 无约束（只在 Data 中有一个）：需要对齐（实际与带约束的编码一样，只是不检查长度）。
- **BIT STRING**（`cms_string.h`）
  - `BIT STRING (SIZE(n))`：固定 BIT STRING，n ≤ 16 → 不对齐；n > 16 或变长 → 对齐。
  - `BIT STRING (SIZE(lb..ub)), ub < 64K`：需要对齐，需要编码长度，长度是 **bit 数**。
  - 无约束（只在 Data 中有一个）：需要对齐（实际与带约束的编码一样，只是不检查长度）。
- **VisibleString**（`cms_string.h`）
  - 已知倍数字符串，1 字符 = 1 字节。
  - 约束不编码；只有实际长度（字符数 = 字节数）可能编码。
  - 固定 `SIZE(n)`：`n × 8 < 16` → 不对齐；`n × 8 > 16` → 对齐；无长度字段。
  - 变长 / 无约束：先编码长度决定因子，内容对齐（`> 16 bit` 时）。
- **UTF8String**（`cms_string.h`）
  - 标准实际上并没有定义，这里按 VisibleString 实现，长度也为字节数。仅在 Data 中使用。
- **OPTIONAL**（`cms_sequence.h`）
  - 提供了一个函数：若字段为可选，标准要求在前面加一个 bit 表示该字段是否存在。
- **SEQUENCE OF**（`cms_sequence.h`）
  - 提供了一个函数：此类数组需要编码元素个数。
- **工具流**（`cms_stream.h`）

### data

- 这个模块主要实现了dlt2811协议第七章的数据结构，
  - 7.1基础数值类型（basic）：包括了布尔类型、整数、浮点数、字符串等类型。
  - 7.2扩展数值类型（extended）：包括了两种时间。
  - 7.3公共基础类型（common）：包括了比如双点位置(Dbpos)、品质(Quality)等专门对应dlt2811场景语义信息的结构。
  - 7.4功能约束（fc）：只包含功能约束一个内容。
  - 7.5控制块相关（control）：控制块是某一种报文的整体的一组数据结构，这种数据结构本身有一些配套的信息，比如控制操作的发出者(Originator)、控制操作的检测(Check)、控制操作的附加原因(AddCause）。
  - 7.6控制块（block）：各种控制块本身、触发原因以及对应控制块的选项域。
  - 7.7数据union以及数据类型（data）：Data是一个union，用于表达某一种类型的数据。DataDefinition用于表达类型本身。


- 主要包含两种api
  - 提供给内部使用的api。它接收的参数是流的结构，这样可以避免频繁创建新的流。因为可能有一些组合类型需要多个基础类型组合起来依次编码解码。
  - 提供给外部使用的api。它编码导出的是byte数组，主要提供给外部作为一个整体使用。

### svc

ccms主要进行的是基础数据结构的构建。所以svc（service）模块虽然负责对第八章的数据结构进行编码解码，但是主要负责的是对数据结构的编码解码，而不是对协议报文段本身的实现。























----

# 老内容



三步构建，每步一个独立模块、一个 ps1 脚本：

| 步骤 | 文件夹 | 脚本 | 产出 | 依赖 |
|------|--------|------|------|------|
| 1 | `cmsper/` | `win_cmsper.ps1` | `libcmsper.a` | 无 |
| 2 | `cmsgenerator/` | `win_cmsgen.ps1` | `libcmsper_datatypes.a` + `gen_*.h` | libcmsper.a |
| 3 | `cmsapp/` | `win_cmsapp.ps1` | 示例程序 | 两个 .a |

前一步产出后一步需要，后一步不干扰前一步。

## 快速开始

### 环境

```powershell
winget install Kitware.CMake
winget install MartinStorsjo.LLVM-MinGW.UCRT
```

### Step 1: PER 编解码核心库

```powershell
cd cmsper
.\win_cmsper.ps1
```

产出: `build/libcmsper.a`

### Step 2: 生成数据类型库

```powershell
cd cmsgenerator
.\win_cmsgen.ps1 -InputAsn ..\docs\dlt2811b-datatypes.asn -OutputDir generated
```

产出: `build/libcmsper_datatypes.a` + `generated/gen_*.h`

### Step 3: 编译运行示例

```powershell
cd cmsapp
.\win_cmsapp.ps1
```

## 目录

```
cmsper/           ← PER 编解码原语（C）
├── include/cmsper/  ← API 头文件
├── src/             ← 实现
├── tests/           ← 单元测试
└── win_cmsper.ps1

cmsgenerator/     ← ASN.1 → C 代码生成器
├── include/         ← 词法/语法分析器
├── src/             ← 生成器实现
├── generated/       ← 生成的 .h/.c（运行时产生）
└── win_cmsgen.ps1

cmsapp/           ← 示例应用
├── examples/        ← 案例代码
└── win_cmsapp.ps1
```






-----

# CMS Experiment — ASN.1 PER Codec & FFI

## 目录结构

```
cmsper/          ← PER 编解码核心库 (C)
cmsgenerator/    ← ASN.1 → C 代码生成器 (仅用于生成 SEQUENCE/CHOICE 结构)
cmsapp/          ← 应用示例 (roundtrip 测试)
jcms/            ← Java JNA 封装
docs/
  cms.asn1       ← CMS ASN.1 定义
```

## 架构说明

本项目的编解码分为**两层**：

### 1. FFI 层（手写，推荐使用）

`cmsgenerator/src/cms_ffi*.c` + `cmsgenerator/include/cms_ffi*.h`

- **纯手写**，不依赖代码生成器
- 提供 `byte[]` 进 `byte[]` 出的纯 C ABI 接口
- 各语言（Java/Python/C# 等）通过 FFI 直接调用
- 所有基本数据类型（Float32/Float64/Boolean/Time 等）的编解码都在此层手写实现

### 2. 生成器层（仅用于复杂 SEQUENCE/CHOICE 结构）

`cmsgenerator/src/c_gen.c` 解析 ASN.1 定义，自动生成 C 结构体和 PER 编解码函数。

- 适用于 SGCB、Associate-Request 等复杂嵌套结构
- 基本数据类型（如 Float32/Float64）在 ASN.1 中定义为 `OCTET STRING (SIZE(4/8))`，生成器将其映射为固定长度的字节数组
- **如果你只使用 FFI 接口，则完全不需要运行代码生成器**

## 构建顺序

### 1. PER 核心库

```powershell
cd cmsper
.\win_cmsper.ps1
```

### 2. FFI DLL（跨语言调用，推荐）

```powershell
cd cmsgenerator
.\win_dll.ps1
```

输出：`cmsgenerator/build/bin/libcmsper_datatypes.dll`

### 3. 代码生成器 + 数据类型库（仅当需要重新生成 SEQUENCE/CHOICE 结构时）

```powershell
cd cmsgenerator
.\win_cmsgen.ps1 -InputAsn ..\docs\cms.asn1
```

### 4. 应用示例

```powershell
cd cmsapp
.\win_cmsapp.ps1
```

## 运行示例

`win_cmsapp.ps1` 会依次运行五个 roundtrip 测试：

- **sgcb_roundtrip** — 普通数据编解码
- **seq_of_roundtrip** — SEQUENCE OF 嵌套编解码
- **service_roundtrip** — 服务报文编解码 (Associate/Release/Abort)
- **apdu_roundtrip** — APDU 帧编解码 (APCH 4B 帧头 + ASDU)
- **ffi_roundtrip** — FFI 层编解码 (byte[] in/out)

## FFI 接口

`cms_ffi.h` 提供纯 C ABI 接口，各语言通过 FFI 加载 DLL 直接调用：

| 函数 | 说明 |
|------|------|
| `cms_ffi_encode_associate_request` | Associate-Request 编码 |
| `cms_ffi_decode_associate_request` | Associate-Request 解码 |
| `cms_ffi_encode_release_request` | Release-Request 编码 |
| `cms_ffi_decode_release_request` | Release-Request 解码 |
| `cms_ffi_encode_abort` | Abort 编码 |
| `cms_ffi_decode_abort` | Abort 解码 |

所有接口均为 `byte[]` 进 `byte[]` 出，与语言无关。

## APDU 帧格式

APDU = APCH(4B) + ASDU(PER 编码)，与 Java 端 `CmsApdu` 二进制兼容：

```
┌──────────┬────────┬──────────┬─────────────┐
│ CC (1B)  │ SC(1B) │ FL (2B)  │ ASDU (nB)   │
│ Control  │ 服务码  │ 帧长度   │ PER 编码数据 │
│ Code     │        │ (大端)   │             │
└──────────┴────────┴──────────┴─────────────┘
```

CC 字节：`bit7=Next | bit6=Resp | bit5=Err | bit4=bak | bit3~0=PI(0x01)`

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
  * 负责dlt2811协议的per编码
  * 第七章数据结构实现
  * 第八章报文段中基础结构实现。
* **ccmsapp**：（仅用于演示）基于ccms可以继续开发其他功能。
* **jcms**
  * 负责将ccms api一一映射到java。同时jcms在dll未成功加载时本身也可以支持per编码。
  * 第七章数据结构实现
  * 第八章报文段基础结构与报文段本身实现。
* **jcmsapp**
  * 传输层实现
  * 配置解析
  * 应用层实现
  * 客户端服务器cli

### 资料

* [国产自主可控新一代通信标准CMS之总览篇](https://zhuanlan.zhihu.com/p/520653213)

































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

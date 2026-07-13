# Configuration Mapping Specification（CMS）

---

## CMS概述

**CMS（通信报文规范）**就是国家电网为了摆脱对国外技术的依赖，专门搞出来的**“MMS协议国产替代版”**。它的核心使命是实现电力通信的**自主可控**，同时解决老协议笨重、低效的问题。

* **瘦身提速（PER编码）**：把原来MMS那种臃肿的BER编码换成了紧凑的**PER编码**。
* **砍掉中间商（直接映射）**：以前MMS需要把电力指令层层转译（七层模型），现在CMS**直接映射TCP/IP**。
* **穿上防弹衣（国密加密）**：最大的亮点在于**安全性**。CMS原生支持国密算法加密。

本项目就是制作CMS标准的代码实现。主要的思路是，使用C语言编写per编码部分和基础数据结构部分。后续开发可以基于C/C++，也可以将该模块打包成dll，后续迁移到其他语言平台。项目目录包含下边内容：

* **ccms**
  * [per](docs/impl/ccms-per.md): 底层编码，dlt2811协议使用per编码将asn1转换成字节码。
  * [data](docs/impl/ccms-data.md): dlt2811 §7 数据结构部分的实现。
  * [svc](docs/impl/ccms-svc.md): dlt2811 §8 报文段部分的实现。
* **jcms-core**
  * [core](docs/impl/jcms-core.md): 负责将ccms api映射到java，并且设计基础的`CmsType`等基类。
  * [info](docs/impl/jcms-info.md): 一些文档性质的枚举和说明。
  * [data](docs/impl/jcms-data.md): dlt2811 §7的java封装。
  * [svc](docs/impl/jcms-svc.md): dlt2811 §8的java封装。
* **jcms-utils**
  * [config](docs/impl/jcms-config.md): 配置模块。
  * [scl](docs/impl/jcms-scl.md):  scd文件解析模块。
  * [security](docs/impl/jcms-security.md): 安全协议。
  * [transport](docs/impl/jcms-transport.md): 传输层基础构建。
* **jcms-app**
  * [node](docs/impl/jcms-node.md): dlt2811 §6传输层封装。
  * [handler](docs/impl/jcms-handler.md): 各种处理器，比如客户端，服务器，命令行交互界面。
  * [console](docs/impl/jcms-console.md): 命令行交互界面。
* [国产自主可控新一代通信标准CMS之总览篇](https://zhuanlan.zhihu.com/p/520653213)

---

## 使用方法

* 自定义方法
  * json格式输出
  * 清空命令行
  * 显示报文信息
* 8.2 连接
  * 8.2.1 [associate](docs/usage/8-2-1.md)
  * 8.2.2 [release](docs/usage/8-2-2.md)
  * 8.2.3 [abort](docs/usage/8-2-3.md)
* 8.3 目录
  * 8.3.1 [server-dir](docs/usage/8-3-1.md)
  * 8.3.2 [ld-dir](docs/usage/8-3-2.md)
  * 8.3.3 [ln-dir](docs/usage/8-3-3.md)
  * 8.3.4 [all-data](docs/usage/8-3-4.md)
  * 8.3.5 [all-def](docs/usage/8-3-5.md)
  * 8.3.6 [all-cb](docs/usage/8-3-6.md)
* 8.4 数据
  * 8.4.1 [get-data-values](docs/usage/8-4-1.md)
  * 8.4.2 [set-data-values](docs/usage/8-4-2.md)
  * 8.4.3 [data-dir](docs/usage/8-4-3.md)
  * 8.4.4 [get-data-def](docs/usage/8-4-4.md)
* 8.5 数据库
  * 8.5.1 [get-dataset-values](docs/usage/8-5-1.md)
  * 8.5.2 [set-dataset-values](docs/usage/8-5-2.md)
  * 8.5.3 [create-dataset](docs/usage/8-5-3.md)
  * 8.5.4 [delete-dataset](docs/usage/8-5-4.md)
  * 8.5.5 [get-dataset-dir](docs/usage/8-5-5.md)
* 8.6 定值（SG Block）
  * 8.6.1 [select-active-sg](docs/usage/8-6-1.md)
  * 8.6.2 [select-edit-sg](docs/usage/8-6-2.md)
  * 8.6.3 [set-edit-sg](docs/usage/8-6-3.md)
  * 8.6.4 [confirm-edit-sg](docs/usage/8-6-4.md)
  * 8.6.5 [get-edit-sg](docs/usage/8-6-5.md)
  * 8.6.6 [sgcb-vals](docs/usage/8-6-6.md)
* 8.7 报告（Report）
  * 8.7.1 [report](docs/usage/8-7-1.md)（服务器推送）
  * 8.7.2 [get-brcb-vals](docs/usage/8-7-2.md)
  * 8.7.3 [set-brcb-vals](docs/usage/8-7-3.md)
  * 8.7.4 [get-urcb-vals](docs/usage/8-7-4.md)
  * 8.7.5 [set-urcb-vals](docs/usage/8-7-5.md)
* 8.8 日志（Log）
  * 8.8.2 [get-lcb-vals](docs/usage/8-8-2.md)
  * 8.8.3 [set-lcb-vals](docs/usage/8-8-3.md)
  * 8.8.4 [query-log-by-time](docs/usage/8-8-4.md)
  * 8.8.5 [query-log-after](docs/usage/8-8-5.md)
  * 8.8.6 [get-log-status](docs/usage/8-8-6.md)
* 8.12 文件（File）
  * 8.12.1 [get-file](docs/usage/8-12-1.md)
  * 8.12.2 [set-file](docs/usage/8-12-2.md)
  * 8.12.3 [delete-file](docs/usage/8-12-3.md)
  * 8.12.4 [get-file-attrs](docs/usage/8-12-4.md)
  * 8.12.5 [get-file-dir](docs/usage/8-12-5.md)
* 8.14 测试：[test](docs/usage/8-14.md)
* 8.15 协商：[negotiate](docs/usage/8-15.md)

### 支持json格式输出

```bash
PS D:\project\work\standard\dlt2811bean> cms server-dir --json
{"success":true,"data":["LD0","PROT","RCD"]}

PS D:\project\work\standard\dlt2811bean> cms server-dir       
  Logical Devices:
    [0] LD0
    [1] PROT
    [2] RCD
```

### 自定义功能

```bash
# 清空显示：有时候屏幕内容输出杂乱，可以手动清空
clear
# 显示帮助信息
help
# 设置显示报文具体信息，可以用来调试
trace-pdu --value true
trace-pdu --value false
```

### 8.1 连接

```bash
# 非加密连接，默认IP 127.0.0.1、端口 8102。指定访问点会自动 negotiate + associate。
connect --ap C_B5041X/S1;
# 加密连接（TLS），使用 9102 端口
connect --secure --ap C_B5041X/S1;
# 两种连接都可以指定 negotiate 参数，不指定则使用默认值
connect --ap C_B5041X/S1 --apdu 16384 --asdu 65531 --version 1;
# 也可以只进行 TCP 连接，不自动后续操作（没有 negotiate 和 associate）
connect;
# 断开连接
disconnect
# 退出程序
exit
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> disconnect
  OK  Disconnected.
cms> connect --secure --ap C_B5041X/S1;
  TLS connecting to 127.0.0.1:9102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  TLS Associated: C_B5041X/S1
cms> disconnect
  OK  Disconnected.
cms> connect --ap C_B5041X/S1 --apdu 16384 --asdu 65531 --version 1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> disconnect
  OK  Disconnected.
cms> connect
  Connecting to 127.0.0.1:8102 ...
  OK  Connected: 127.0.0.1:8102
cms> disconnect
  Not connected.
cms> exit
Bye.
```

### 8.9.1 发送GOOSE消息服务(SendGOOSEMessage)

这个是服务器通过给其他设备发送GOOSE消息，不属于客户端服务。

### 8.9.2 读GOOSE引用服务(GetGoReference)

```bash

```

### 8.9.3 读GOOSE元素序号服务(GetGOOSEElementNumber)

```bash

```

#### 8.9.4 读GOOSE控制块值服务(GetGoCBValues)

```bash
get-gocb-vals --refs "CTRL/LLN0.gocb0"
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> get-gocb-vals --refs "CTRL/LLN0.gocb0"
  Fetching GoCB values for 1 reference(s)
    [CTRL/LLN0.gocb0] goEna=false goID=C_B5041XCTRL/LLN0.gocb0 datSet=dsGOOSE confRev=1 ndsCom=false
```

#### 8.9.5 设置GOOSE控制块值服务(SetGoCBValues)

```bash
set-gocb-vals --ref CTRL/LLN0.gocb0 --go-ena true --go-id "MyGoCB"
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> get-gocb-vals --refs "CTRL/LLN0.gocb0"
  Fetching GoCB values for 1 reference(s)
    [CTRL/LLN0.gocb0] goEna=false goID=C_B5041XCTRL/LLN0.gocb0 datSet=dsGOOSE confRev=1 ndsCom=false
cms> set-gocb-vals --ref CTRL/LLN0.gocb0 --go-ena true --go-id "MyGoCB"
  Setting GoCB values: ref=CTRL/LLN0.gocb0
  OK  GoCB values set for CTRL/LLN0.gocb0
cms> get-gocb-vals --refs "CTRL/LLN0.gocb0"
  Fetching GoCB values for 1 reference(s)
    [CTRL/LLN0.gocb0] goEna=true goID=MyGoCB datSet=dsGOOSE confRev=1 ndsCom=false
```

### 8.11.1 Select

这组服务也是需要操作实际设备的，本实现采用缓存模拟

```bash
select --ref LD0/CTRL1.SPC1 --value true --origin 1 --ctlNum 5 --check syncheck --test true
```

```bash

```


### 8.11.

备注：参数含义

**`--ref LD0/CTRL1.SPC1`** — 你要操作哪个设备
- `LD0` = 逻辑设备（比如"1号主变"）
- `CTRL1` = 控制逻辑节点（比如"断路器控制"）
- `SPC1` = 控制对象（Single Point Control，比如"合闸/分闸"）

**`--value true/false`** — 你想让它变成啥
- 合闸 = `true`，分闸 = `false`
- 标准里叫 `ctlVal`（control value）

**`--origin 1`** — 操作源是谁
- 0 = 本地操作（人在设备面板上按）
- 1 = 远程操作（调度员在后台点的）
- 标准里叫 `orCat`（originator category）

**`--ctlNum 5`** — 操作的序号
- 每次操作递增，服务器可以检测是否有乱序或重放攻击
- 不传的话服务器就用默认值 0

**`--check syncheck`** — 要不要做校验
- `syncheck` = 同步校验（需要两个操作员同时确认）
- `interlock-check` = 联锁校验（检查开关之间的互锁条件）
- 不传 = 不做额外校验

**`--test true/false`** — 是测试还是真操作
- `true` = 模拟操作，设备不实际动作
- `false` = 真实操作（默认）

简单来说就是：**ref** 选谁 → **value** 调成啥 → **origin** 谁点的 → **ctlNum** 第几次 → **check** 要不要复核 → **test** 是真的还是演练。

### 8.15 协商

```bash
# 使用默认参数（从配置文件读取）
negotiate
# 手动指定参数（--apduSize --asduSize --protocolVersion）
negotiate --apduSize 16384 --asduSize 65531 --protocolVersion 1
```

```bash
cms> negotiate
  OK  Negotiate completed.
cms> negotiate --apduSize 16384 --asduSize 65531 --protocolVersion 1
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


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

### 8.2.1 确定访问点

```bash
# 需要先建立 tcp 连接
connect;
# 再指定访问点
associate --ap C_B5041X/S1;
# 单独的 associate 可以指定加密（是associate的加密，和connect的加密是并行的）
associate --ap C_B5041X/S1 --secure;
```

```bash
cms> connect;
  Connecting to 127.0.0.1:8102 ...
  OK  Connected: 127.0.0.1:8102
cms> associate --ap C_B5041X/S1;
  OK  Associated: C_B5041X/S1
cms> release
  OK  Released.
cms> associate --ap C_B5041X/S1 --secure;
  OK  Associated: C_B5041X/S1 (secure)
```

### 8.2.2 正常释放

```bash
# release是正常释放关联，可以后续更换访问点
release
```

### 8.2.3 异常释放

```bash
# 客户端因为某种异常需要断开连接，不需要服务器响应。
# 服务器认为客户端已下线，会关闭 tcp 连接。
# abort <reason>
abort --reason 0; # 默认 reason 就是 0 `others`
```

```bash
cms> associate --ap C_B5041X/S1 --secure;
  OK  Associated: C_B5041X/S1 (secure)
cms> abort 0
  OK  Abort sent (reason=0)
```

### 8.3.1 获取逻辑设备

```bash
# 获取某一个 accesspoint 下边的逻辑设备
server-dir;
# 指定 referenceAfter
server-dir --after LD0;
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> server-dir;
  Logical Devices:
    [0] LD0
    [1] MEAS
    [2] CTRL
cms> server-dir --after LD0;
  Logical Devices:
    [0] MEAS
    [1] CTRL
```

### 8.3.2 获取指定逻辑设备下的所有逻辑节点

```bash
# LD0 这个设备下的所有逻辑节点
ld-dir --ld LD0;
# LD0 下有很多节点，获取 LTSM6 之后的节点
ld-dir --ld LD0 --after LTSM6;
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> ld-dir --ld LD0;
  Logical Nodes:
    [0] LLN0
    [1] LPHD1
    [2] RSYN1
    [3] GGIO1
    ...
    [75] LTSM5
    [76] LTSM6
    [77] LTSM7
    [78] LTSM8
    [79] LTSM9
cms> ld-dir --ld LD0 --after LTSM6;
  Logical Nodes:
    [0] LTSM7
    [1] LTSM8
    [2] LTSM9
```

### 8.3.3 获取逻辑节点目录

```bash
# data-object：数据对象
ln-dir --ln LD0 --acsi data-object;
# data-set：数据集
ln-dir --ln LD0 --acsi data-set;
# brcb
ln-dir --ln LD0 --acsi brcb;
# urcb
ln-dir --ln LD0 --acsi urcb;
# gocb
ln-dir --ln CTRL --acsi gocb;
```
* data-object
* data-set
* brcb
* urcb
* gocb
```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> ln-dir --ln LD0 --acsi data-object;
  References (data-object):
    [0] Mod
    [1] Mod.stVal
    [2] Mod.q
    [3] Mod.t
    [4] Beh
    [5] Beh.stVal
    [6] Beh.q
    [7] Beh.t
    ...
    [171] Frdb
    [172] TapChgMod
    [173] CtrlOPTmms1
    [174] CtrlCPTmms1
    [175] CtrlOPTmms2
    [176] CtrlCPTmms2
cms> ln-dir --ln LD0 --acsi data-set;
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
cms> ln-dir --ln LD0 --acsi brcb;
  References (brcb):
    [0] brcbAlarm
    [1] brcbWarning
    [2] brcbCommState
cms> ln-dir --ln LD0 --acsi urcb;
  References (urcb):
    [0] urcbAin
    [1] urcbAinA
cms> ln-dir --ln CTRL --acsi gocb
  References (gocb):
    [0] gocb0
```

* lcb
* log
```bash
cms> connect --ap P_B5041A/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with P_B5041A/S1 ...
  OK  Associated: P_B5041A/S1
cms> ln-dir --ln LD0 --acsi lcb
  References (lcb):
    [0] lcblog
cms> ln-dir --ln LD0 --acsi log
  References (log):
    [0] LD0
cms> ln-dir --ln LD0 --acsi sgecb
  References (sgecb):
    [0]
cms> ln-dir --ln LD0 --acsi msvcb
  References (msvcb):
    [0]
```

### 8.3.4 获取全部数据值

```bash
# 1. 返回有值的内容，跳过没设置值的内容
# 2. 已经可以正常支持中文，使用 unicode-string
# 3. 可以进行 FC 筛选，默认 XX 不筛选
# 4. 可以进行 referenceAfter 筛选
# all-data --ln <LD|LD/LN> [--fc FC] [--after REF]
all-data --ln LD0 --fc XX --after LicIP17
all-data --ln LD0/LLN0;
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> all-data --ln LD0/LLN0
  Data values (13 items):
    [0] Mod  [visible-string] status-only
    [1] NamPlt  [visible-string] Name Plate
    [2] LEDRs  [visible-string] direct-with-normal-security
    [3] Outage  [visible-string] Out of range
    [4] CommTest  [visible-string] communication for testing
    [5] CommTstMet  [visible-string] communication for measuring test
    [6] U0AlarmEn  [visible-string] U0 Alarm enable
    [7] Set3U0  [visible-string] setting for 3U0
    [8] Meter2En  [visible-string] Meter2En
    [9] YKYXASSO  [visible-string] YKYXASSO
    [10] TimeZone  [visible-string] TimeZone
    [11] SntpAddr  [visible-string] SntpAddr
    [12] YCDeadZone  [visible-string] YCDeadZone
cms> all-data --ln LD0/LLN0 --after SntpAddr
  Data values (1 items):
    [0] YCDeadZone  [visible-string] YCDeadZone
cms> all-data --ln LD0/LLN0 --fc CF
  Data values (2 items):
    [0] Mod  [visible-string] status-only
    [1] LEDRs  [visible-string] direct-with-normal-security
cms> all-data --ln LD0/LLN0 --fc MX
  Data values (0 items): (empty)
cms> all-data --ln LD0
  Data values (616 items):
    [0] Mod  [visible-string] status-only
    [1] NamPlt  [visible-string] Name Plate
    [2] LEDRs  [visible-string] direct-with-normal-security
    ...
```

### 8.3.5 获取数据定义

```bash
# all-def --ln <LD|LD/LN> [--fc FC] [--after REF]
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> all-def --ln LD0/LLN0
  Data definitions (16 items):
    [0] Mod  [structure]  cdc=INC
    [1] Beh  [structure]  cdc=INS
    [2] Health  [structure]  cdc=INS
    [3] NamPlt  [structure]  cdc=LPL
    [4] Loc  [structure]  cdc=SPS
    [5] LEDRs  [structure]  cdc=SPC
    [6] Outage  [structure]  cdc=SPS
    [7] CommTest  [structure]  cdc=SPS
    [8] CommTstMet  [structure]  cdc=SPS
    [9] U0AlarmEn  [structure]  cdc=SPG
    [10] Set3U0  [structure]  cdc=ASG
    [11] Meter2En  [structure]  cdc=SPG
    [12] YKYXASSO  [structure]  cdc=SPG
    [13] TimeZone  [structure]  cdc=ING
    [14] SntpAddr  [structure]  cdc=ING
    [15] YCDeadZone  [structure]  cdc=ASG
cms> all-def --ln LD0/LLN0 --after SntpAddr
  Data definitions (1 items):
    [0] YCDeadZone  [structure]  cdc=ASG
cms> all-def --ln LD0/LLN0 --fc CF
  Data definitions (6 items):
    [0] Mod  [structure]  cdc=INC
    [1] LEDRs  [structure]  cdc=SPC
    [2] Set3U0  [structure]  cdc=ASG
    [3] TimeZone  [structure]  cdc=ING
    [4] SntpAddr  [structure]  cdc=ING
    [5] YCDeadZone  [structure]  cdc=ASG
cms> all-def --ln LD0/LLN0 --fc MX
  Data definitions (0 items): (empty)
cms> all-def --ln LD0
  Data definitions (256 items):
    [0] Mod  [structure]  cdc=INC
    [1] Beh  [structure]  cdc=INS
    [2] Health  [structure]  cdc=INS
    [3] NamPlt  [structure]  cdc=LPL
    [4] Loc  [structure]  cdc=SPS
    [5] LEDRs  [structure]  cdc=SPC
    [6] Outage  [structure]  cdc=SPS
    [7] CommTest  [structure]  cdc=SPS
    [8] CommTstMet  [structure]  cdc=SPS
    ...
    [253] Beh  [structure]  cdc=INS
    [254] Health  [structure]  cdc=INS
    [255] NamPlt  [structure]  cdc=LPL   # 因为我设置了做多256后边的需要再次请求
```

### 8.3.6 获取控制块的值

```bash
all-cb --ln LD0 --acsi brcb;
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> all-cb --ln LD0 --acsi brcb;
  Fetching CB values: target=LD0 type=BRCB
  CB values (3 items):
    [0] LLN0.brcbAlarm  [BRCB]
    [1] LLN0.brcbWarning  [BRCB]
    [2] LLN0.brcbCommState  [BRCB]
cms> all-cb --ln LD0 --acsi urcb
  Fetching CB values: target=LD0 type=URCB
  CB values (2 items):
    [0] LLN0.urcbAin  [URCB]
    [1] LLN0.urcbAinA  [URCB]
cms> all-cb --ln LD0 --acsi lcb
  Fetching CB values: target=LD0 type=LCB
  No CB values found
cms> all-cb --ln LD0 --acsi gocb
  Fetching CB values: target=LD0 type=MSVCB
  No CB values found
cms> all-cb --ln LD0 --acsi msvcb
  Fetching CB values: target=LD0 type=MSVCB
  No CB values found
cms> all-cb --ln LD0 --acsi log # 输入错误
  ERR Invalid acsiClass: log. Valid values: brcb, urcb, lcb, sgecb, gocb, msvcb   
cms> all-cb --ln LD0 --acsi sgecb
  Fetching CB values: target=LD0 type=GOCB
  CB values (80 items):
    [0] LLN0.LLN0.SG1  [SGECB]
    [1] LPHD1.LPHD1.SG1  [SGECB]
    [2] RSYN1.RSYN1.SG1  [SGECB]
    [3] GGIO1.GGIO1.SG1  [SGECB]
    [4] GGIO2.GGIO2.SG1  [SGECB]
    [5] GGIO3.GGIO3.SG1  [SGECB]
    ...
    [75] LTSM5.LTSM5.SG1  [SGECB]
    [76] LTSM6.LTSM6.SG1  [SGECB]
    [77] LTSM7.LTSM7.SG1  [SGECB]
    [78] LTSM8.LTSM8.SG1  [SGECB]
    [79] LTSM9.LTSM9.SG1  [SGECB]
```

### 8.4.1 获取指定数据值

```bash
# 用法: get-data-values --refs "<ref1> <ref2> ..." [--fc FC]
get-data-values --refs "LD0/GGIO9.AnIn1 LD0/GGIO9.AnIn2 LD0/GGIO1.HostTPortAlarm";
```

### 8.4.2 设置指定数据值

```bash
set-data-values --pairs "<ref1>=<value1> <ref2>=<value2> ...";
```

```bash
cms> connect --ap C_B5041X/S1;
cms> 
cms> 
cms> # 数值类型
cms> get-data-values --refs "LD0/LLN0.Mod.stVal LD0/LLN0.Beh.stVal"
  Fetching data values for 2 reference(s)
  Data values (2 items):
    [0] LD0/LLN0.Mod.stVal  [visible-string] (unavailable)
    [1] LD0/LLN0.Beh.stVal  [visible-string] (unavailable)
cms> set-data-values --pairs "LD0/LLN0.Mod.stVal=10 LD0/LLN0.Beh.stVal=20"
  Setting 2 data value(s)...
  OK  Set 2 data value(s) successfully
cms> get-data-values --refs "LD0/LLN0.Mod.stVal LD0/LLN0.Beh.stVal"
  Fetching data values for 2 reference(s)
  Data values (2 items):
    [0] LD0/LLN0.Mod.stVal  [int32] 10
    [1] LD0/LLN0.Beh.stVal  [int32] 20
cms>
cms>
cms> # 浮点数类型
cms> get-data-values --refs "LD0/GGIO2.BusVRtgPri.setMag.f"
  Fetching data values for 1 reference(s)
  Data values (1 items):
    [0] LD0/GGIO2.BusVRtgPri.setMag.f  [visible-string] (unavailable)
cms> set-data-values --pairs "LD0/GGIO2.BusVRtgPri.setMag.f=123.45"
  Setting 1 data value(s)...
  OK  Set 1 data value(s) successfully
cms> get-data-values --refs "LD0/GGIO2.BusVRtgPri.setMag.f"
  Fetching data values for 1 reference(s)
  Data values (1 items):
    [0] LD0/GGIO2.BusVRtgPri.setMag.f  [float32] 123.45
cms>
cms>
cms> # 布尔类型
cms> get-data-values --refs "LD0/LLN0.CommTstMet.stVal"
  Fetching data values for 1 reference(s)
  Data values (1 items):
    [0] LD0/LLN0.CommTstMet.stVal  [visible-string] (unavailable)
cms> set-data-values --pairs "LD0/LLN0.CommTstMet.stVal=111" # 错误的类型会被拒绝
  Setting 1 data value(s)...
  ERR SetDataValues rejected: [0] error=12
cms> set-data-values --pairs "LD0/LLN0.CommTstMet.stVal=true"
  Setting 1 data value(s)...
  OK  Set 1 data value(s) successfully
cms> get-data-values --refs "LD0/LLN0.CommTstMet.stVal"
  Fetching data values for 1 reference(s)
  Data values (1 items):
    [0] LD0/LLN0.CommTstMet.stVal  [boolean] true
cms>
cms>
cms> # 字符串类型
cms> get-data-values --refs "LD0/GGIO9.AnIn1 LD0/GGIO1.HostTPortAlarm"
  Fetching data values for 2 reference(s)
  Data values (2 items):
    [0] LD0/GGIO9.AnIn1  [visible-string] (unavailable)
    [1] LD0/GGIO1.HostTPortAlarm  [visible-string] (unavailable)
cms> set-data-values --pairs "LD0/GGIO9.AnIn1=光口1发功率修改 LD0/GGIO1.HostTPortAlarm=对时信号状态修改"
  Setting 2 data value(s)...
  OK  Set 2 data value(s) successfully
cms> get-data-values --refs "LD0/GGIO9.AnIn1 LD0/GGIO1.HostTPortAlarm"
  Fetching data values for 2 reference(s)
  Data values (2 items):
    [0] LD0/GGIO9.AnIn1  [visible-string] 光口1发功率修改
    [1] LD0/GGIO1.HostTPortAlarm  [visible-string] 对时信号状态修改
```

### 8.4.3 获取数据目录

```bash
# LN 级：列出该 LN 下的所有 DO（不含 fc）
data-dir --ref LD0/LLN0
# DO 级：列出该 DO 下的所有 DA（含 fc）
data-dir --ref LD0/LLN0.Mod
# 分页
data-dir --ref LD0/LLN0 --after Beh
```

```bash
cms> connect --ap C_B5041X/S1;
cms> data-dir --ref LD0/LLN0
  Data directory (16 items):
    [0] Mod
    [1] NamPlt
    [2] LEDRs
    [3] Outage
    [4] CommTest
    [5] CommTstMet
    [6] U0AlarmEn
    [7] Set3U0
    [8] Meter2En
    [9] YKYXASSO
    [10] TimeZone
    [11] SntpAddr
    [12] YCDeadZone
    [13] Beh
    [14] Health
    [15] Loc
cms> data-dir --ref LD0/LLN0.Mod
  Data directory (5 items):
    [0] [CF]  ctlModel
    [1] [DC]  dU
    [2] [ST]  stVal
    [3] [ST]  q
    [4] [ST]  t
cms> data-dir --ref LD0/LLN0 --after Health
  Fetching data directory for LD0/LLN0
  Data directory (1 items):
    [0] Loc
cms> data-dir --ref LD0/LLN0.Mod --after q
  Fetching data directory for LD0/LLN0.Mod
  Data directory (1 items):
    [0] [ST]  t
```

### 8.4.4 获取数据定义

```bash
# 用法: get-data-def --refs "<ref1> <ref2>..." [--fc FC]
# DO 级定义（含 CDC 类型）
get-data-def --refs "LD0/LLN0.Mod LD0/LLN0.Beh"
# DA 级定义
get-data-def --refs "LD0/LLN0.Mod.stVal"
# 带 fc 过滤
get-data-def --refs "LD0/LLN0.Mod" --fc ST
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> get-data-def --refs "LD0/LLN0.Mod LD0/LLN0.Beh"
  Fetching data definitions for 2 reference(s)
  Data definitions (2 items):
    [0] LD0/LLN0.Mod  [structure]  cdc=INC
    [1] LD0/LLN0.Beh  [structure]  cdc=INS
cms> get-data-def --refs "LD0/LLN0.Mod.stVal"
  Fetching data definitions for 1 reference(s)
  Data definitions (1 items):
    [0] LD0/LLN0.Mod.stVal  [boolean]
cms> get-data-def --refs "LD0/LLN0.Mod" --fc ST
  Fetching data definitions for 1 reference(s)
  Data definitions (1 items):
    [0] LD0/LLN0.Mod  [structure]  cdc=INC
```

### 8.5.1 读数据集的值

```bash
# 用法: get-dataset-values --ds <完整数据集引用> [--after REF]
# 获取数据集所有成员的值
get-dataset-values --ds "LD0/LLN0.dsAlarm"
# 从指定成员之后开始（分页）
get-dataset-values --ds "LD0/LLN0.dsAlarm" --after "LD0/GGIO1.Alm1"
```

```bash
# 具体内容看后边的8.5.5
```


### 8.5.2 写数据集的值

```bash
# 用法: set-dataset-values --ds <ref> --values "<val1> <val2>..." [--after REF]
set-dataset-values --ds "LD0/LLN0.dsAlarm" --values "true false true"
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> set-dataset-values --ds "LD0/LLN0.dsAlarm" --values "true false true"
cms> get-dataset-values --ds "LD0/LLN0.dsAlarm"
  Fetching dataset values for LD0/LLN0.dsAlarm
  No dataset values returned
  Setting 3 dataset value(s) for LD0/LLN0.dsAlarm
  OK  Set 3 dataset value(s) successfully
cms> get-dataset-values --ds "LD0/LLN0.dsAlarm"
  DataSet values (3 items):
  Fetching dataset values for LD0/LLN0.dsAlarm
    [0] [boolean] true
    [1] [boolean] false
    [2] [boolean] true
```

### 8.5.3 新建数据库

```bash
# 用法: create-dataset --ds <ref> --members "<ref1>,<fc1> <ref2>,<fc2>..."
# 创建新数据集
create-dataset --ds "LD0/LLN0.myDs" --members "LD0/GGIO1.Alm1,ST LD0/GGIO1.Alm2,ST"
# 追加成员到已有数据集
create-dataset --ds "LD0/LLN0.myDs" --members "LD0/GGIO1.Alm3,ST" --after "LD0/GGIO1.Alm2"
```

```bash
# 案例请看 8.5.4
```

### 8.5.4 删除数据库

```bash
# 用法: delete-dataset --ds <ref>
delete-dataset --ds "LD0/LLN0.myDs"
```

下边这个案例是创建一个新的数据集，然后添加值，查找，删除，确认删除成功
```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> create-dataset --ds "LD0/LLN0.myDs" --members "LD0/GGIO1.Alm1,ST LD0/GGIO1.Alm2,ST"
  Creating dataset LD0/LLN0.myDs with 2 member(s)
  OK  Created dataset LD0/LLN0.myDs successfully
cms> get-dataset-dir --ds "LD0/LLN0.myDs"
  Fetching dataset directory for LD0/LLN0.myDs
  DataSet directory (2 items):
    [0] [ST]  LD0/GGIO1.Alm1
    [1] [ST]  LD0/GGIO1.Alm2
cms> set-dataset-values --ds "LD0/LLN0.myDs" --values "false true"
  Setting 2 dataset value(s) for LD0/LLN0.myDs
  OK  Set 2 dataset value(s) successfully
cms> get-dataset-values --ds "LD0/LLN0.myDs"
  Fetching dataset values for LD0/LLN0.myDs
  DataSet values (2 items):
    [0] [boolean] false
    [1] [boolean] true
cms> delete-dataset --ds "LD0/LLN0.myDs"
  Deleting dataset LD0/LLN0.myDs
  OK  Deleted dataset LD0/LLN0.myDs successfully
cms> get-dataset-dir --ds "LD0/LLN0.myDs"
  Fetching dataset directory for LD0/LLN0.myDs
  ERR GetDataSetDirectory rejected: error=1
```

### 8.5.5 读数据集的成员目录（key）

```bash
# 用法: get-dataset-dir --ds <完整数据集引用> [--after REF]
# 列出数据集的所有成员引用（含 fc）
get-dataset-dir --ds "LD0/LLN0.dsAlarm"
# 从指定成员之后开始（分页）
get-dataset-dir --ds "LD0/LLN0.dsAlarm" --after "LD0/GGIO1.Alm1"
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> get-dataset-dir --ds "LD0/LLN0.dsAlarm"
  Fetching dataset directory for LD0/LLN0.dsAlarm
  DataSet directory (3 items):
    [0] [ST]  LD0/GGIO1.Alm2
    [1] [ST]  LD0/GGIO1.Alm1
    [2] [ST]  LD0/GGIO1.Alm3
cms> get-dataset-values --ds "LD0/LLN0.dsAlarm" # 因为还没有设置值呢，这三个应该是boolean
  Fetching dataset values for LD0/LLN0.dsAlarm
  No dataset values returned
cms> get-dataset-dir --ds "LD0/LLN0.dsAlarm" --after "LD0/GGIO1.Alm1"
  Fetching dataset directory for LD0/LLN0.dsAlarm
  DataSet directory (1 items):
    [0] [ST]  LD0/GGIO1.Alm3
cms> get-dataset-values --ds "LD0/LLN0.dsAlarm" --after "LD0/GGIO1.Alm1"
  Fetching dataset values for LD0/LLN0.dsAlarm # 还没设置值所以是空的
  No dataset values returned
```

### 8.6.1 选择激活定值组

```bash
# 用法: select-active-sg --ref <sgcbRef> --num <groupNumber>
# 选择定值组 2 为激活组
select-active-sg --ref "LD0/LLN0.SG1" --num 2
```

```bash
cms> connect --ap C_B5041X/S1; # 连接
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> sgcb-vals --refs "LD0/LLN0.SG1" # 先看
  Fetching SGCB values for 1 reference(s)
  SGCB values (1 items):
    [0] LD0/LLN0.SG1  numOfSG=4 actSG=1 editSG=1
cms> select-active-sg --ref "LD0/LLN0.SG1" --num 2 # 修改
  Selecting active SG: ref=LD0/LLN0.SG1 num=2
  OK  Active SG set to 2 for LD0/LLN0.SG1
cms> sgcb-vals --refs "LD0/LLN0.SG1" # 再看
  Fetching SGCB values for 1 reference(s)
  SGCB values (1 items):
    [0] LD0/LLN0.SG1  numOfSG=4 actSG=2 editSG=1
```

### 8.6.2 选择编辑定值组

```bash
# 用法: select-edit-sg --ref <sgcbRef> --num <groupNumber>
select-edit-sg --ref "LD0/LLN0.SG1" --num 2
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> sgcb-vals --refs "LD0/LLN0.SG1"
  Fetching SGCB values for 1 reference(s)
  SGCB values (1 items):
    [0] LD0/LLN0.SG1  numOfSG=4 actSG=1 editSG=1
cms> select-edit-sg --ref "LD0/LLN0.SG1" --num 2
  Selecting edit SG: ref=LD0/LLN0.SG1 num=2
  OK  Edit SG set to 2 for LD0/LLN0.SG1
cms> sgcb-vals --refs "LD0/LLN0.SG1"
  Fetching SGCB values for 1 reference(s)
  SGCB values (1 items):
    [0] LD0/LLN0.SG1  numOfSG=4 actSG=1 editSG=2
```

### 8.6.6 读定值组控制块值

```bash
get-edit-sg --refs "PROT/OCPTOC2.StrVal"
```

```bash
具体内容看下边8.6.6
```

### 8.6.5 设置定值组值

```bash
set-edit-sg --refs "PROT/OCPTOC2.StrVal" --values "100" --type int32
```

```bash
具体内容看下边8.6.6
```

### 8.6.6 定制组生效（confirm）

```bash
confirm-edit-sg --ref "PROT/DeZonePTOC1.SG1"
```

```bash
cms> connect --ap P_B5041A/S1
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with P_B5041A/S1 ...
  OK  Associated: P_B5041A/S1
cms> select-edit-sg --ref "PROT/DeZonePTOC1.SG1" --num 1
  Selecting edit SG: ref=PROT/DeZonePTOC1.SG1 num=1
  OK  Edit SG set to 1 for PROT/DeZonePTOC1.SG1
cms> set-edit-sg --refs "PROT/OCPTOC2.StrVal" --values "100" --type int32
  Setting edit SG values (int32) for 1 ref(s)
  OK  Edit SG values set successfully
cms> get-edit-sg --refs "PROT/OCPTOC2.StrVal"
  Fetching edit SG values (SG) for 1 ref(s)
  Edit SG values (1 items):
    [0] PROT/OCPTOC2.StrVal  [visible-string] (unavailable)
cms> confirm-edit-sg --ref "PROT/DeZonePTOC1.SG1"
  Confirming edit SG values: ref=PROT/DeZonePTOC1.SG1
  OK  Edit SG values confirmed for PROT/DeZonePTOC1.SG1
cms> get-edit-sg --refs "PROT/OCPTOC2.StrVal"
  Fetching edit SG values (SG) for 1 ref(s)
  Edit SG values (1 items):
    [0] PROT/OCPTOC2.StrVal  [int32] 100
```

### 8.6.7 定制组信息查看

```bash
# 用法: sgcb-vals --refs "<ref1> <ref2>..."
sgcb-vals --refs "LD0/LLN0.SG1"
```

```bash
cms> connect --ap C_B5041X/S1;
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1
cms> sgcb-vals --refs "LD0/LLN0.SG1"
  Fetching SGCB values for 1 reference(s)
  SGCB values (1 items):
    [0] LD0/LLN0.SG1  numOfSG=4 actSG=1 editSG=1
```

### 8.7.1 REPORT

这个是服务器定期发送，客户端接收的。案例：URCB — 连接 → 启用报告 → 收到 REPORT

```bash
cms> # ======== 1. 连接服务器 ========
cms> connect --ap C_B5041X/S1
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with C_B5041X/S1 ...
  OK  Associated: C_B5041X/S1

cms> # ======== 2. 查看当前 URCB 配置 ========
cms> get-urcb-vals --refs "LD0/LLN0.urcbAin"
  Fetching URCB values for 1 reference(s)
  URCB values (1 items):
    [0] LD0/LLN0.urcbAin  rptID=LD0/LLN0$RP$urcbAin rptEna=false datSet=dsAin confRev=1 bufTm=0 sqNum=0 intgPd=0

cms> # ======== 3. 启用报告 ========
cms> set-urcb-vals --ref LD0/LLN0.urcbAin --rpt-ena true
  Setting URCB values: ref=LD0/LLN0.urcbAin
  OK  URCB values set for LD0/LLN0.urcbAin

cms> # ======== 4. 触发总召唤（GI），服务端立即推送 REPORT ========
cms> set-urcb-vals --ref LD0/LLN0.urcbAin --gi true
  Setting URCB values: ref=LD0/LLN0.urcbAin
  OK  URCB values set for LD0/LLN0.urcbAin
cms>
  Report Received: rptID=LD0/LLN0$RP$urcbAin sqNum=1 dataSet=dsAin entries=35
```

### 8.7.2 读缓存报告控制块值服务(GetBRCBValues)

```bash
get-brcb-vals --refs "LD0/LLN0.brcbDeviceState"
```

```bash
看8.7.3
```

### 8.7.3 设置缓存报告控制块值服务(SetBRCBValues)

```bash
set-brcb-vals --ref LD0/LLN0.brcbDeviceState --dat-set "dsDeviceState" --buf-tm 2000
```

```bash
cms> connect --ap P_B5041A/S1
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with P_B5041A/S1 ...
  OK  Associated: P_B5041A/S1
cms> get-brcb-vals --refs "LD0/LLN0.brcbDeviceState"
  Fetching BRCB values for 1 reference(s)
  BRCB values (1 items):
    [0] LD0/LLN0.brcbDeviceState  rptID=LD0/LLN0$BR$brcbDeviceState rptEna=false datSet=dsDeviceState confRev=1 bufTm=0 sqNum=0 intgPd=0
cms> # ======== BRCB: 改 datSet 和 bufTm ========
cms> set-brcb-vals --ref LD0/LLN0.brcbDeviceState --dat-set "dsDeviceState" --buf-tm 2000
  Setting BRCB values: ref=LD0/LLN0.brcbDeviceState
  OK  BRCB values set for LD0/LLN0.brcbDeviceState
cms> get-brcb-vals --refs "LD0/LLN0.brcbDeviceState"
  Fetching BRCB values for 1 reference(s)
  BRCB values (1 items):
    [0] LD0/LLN0.brcbDeviceState  rptID=LD0/LLN0$BR$brcbDeviceState rptEna=false datSet=dsDeviceState confRev=1 bufTm=2000 sqNum=0 intgPd=0
```

```bash
cms> # ======== 5. BRCB: get start ========
cms> get-brcb-vals --refs "LD0/LLN0.brcbDeviceState"
  Fetching BRCB values for 1 reference(s)
  BRCB values (1 items):
    [0] LD0/LLN0.brcbDeviceState  rptID=LD0/LLN0$BR$brcbDeviceState rptEna=false datSet=dsDeviceState confRev=1 bufTm=0 sqNum=0 intgPd=0
cms>
```
```bash
cms> # ======== 6. BRCB: 改 datSet 和 bufTm ========
cms> set-brcb-vals --ref LD0/LLN0.brcbDeviceState --dat-set "dsDeviceState" --buf-tm 2000
  Setting BRCB values: ref=LD0/LLN0.brcbDeviceState
  OK  BRCB values set for LD0/LLN0.brcbDeviceState
cms> get-brcb-vals --refs "LD0/LLN0.brcbDeviceState"
  Fetching BRCB values for 1 reference(s)
  BRCB values (1 items):
    [0] LD0/LLN0.brcbDeviceState  rptID=LD0/LLN0$BR$brcbDeviceState rptEna=false datSet=dsDeviceState confRev=1 bufTm=2000 sqNum=0 intgPd=0
cms>
cms> # ======== BRCB: 清空缓存 ========
cms> set-brcb-vals --ref LD0/LLN0.brcbDeviceState --purge-buf true
  Setting BRCB values: ref=LD0/LLN0.brcbDeviceState
  OK  BRCB values set for LD0/LLN0.brcbDeviceState
cms> get-brcb-vals --refs "LD0/LLN0.brcbDeviceState"
  Fetching BRCB values for 1 reference(s)
  BRCB values (1 items):
    [0] LD0/LLN0.brcbDeviceState  rptID=LD0/LLN0$BR$brcbDeviceState rptEna=false datSet=dsDeviceState confRev=1 bufTm=2000 sqNum=0 intgPd=0
```
```bash
cms> # ======== 7. BRCB: 清空缓存 ========
cms> set-brcb-vals --ref LD0/LLN0.brcbDeviceState --purge-buf true
  Setting BRCB values: ref=LD0/LLN0.brcbDeviceState
  OK  BRCB values set for LD0/LLN0.brcbDeviceState
cms> get-brcb-vals --refs "LD0/LLN0.brcbDeviceState"
  Fetching BRCB values for 1 reference(s)
  BRCB values (1 items):
    [0] LD0/LLN0.brcbDeviceState  rptID=LD0/LLN0$BR$brcbDeviceState rptEna=false datSet=dsDeviceState confRev=1 bufTm=2000 sqNum=0 intgPd=0
cms>
```
```bash
cms> # ======== 8. BRCB: 总召唤 ========
cms> set-brcb-vals --ref LD0/LLN0.brcbDeviceState --gi true
  Setting BRCB values: ref=LD0/LLN0.brcbDeviceState
  OK  BRCB values set for LD0/LLN0.brcbDeviceState
cms> get-brcb-vals --refs "LD0/LLN0.brcbDeviceState"
  Fetching BRCB values for 1 reference(s)
  BRCB values (1 items):
    [0] LD0/LLN0.brcbDeviceState  rptID=LD0/LLN0$BR$brcbDeviceState rptEna=false datSet=dsDeviceState confRev=1 bufTm=2000 sqNum=0 intgPd=0
cms>
```
```bash
cms> # ======== 9. BRCB: 关闭报告 + 保留时间 ========
cms> set-brcb-vals --ref LD0/LLN0.brcbDeviceState --rpt-ena false --resv-tms 30
  Setting BRCB values: ref=LD0/LLN0.brcbDeviceState
  OK  BRCB values set for LD0/LLN0.brcbDeviceState
cms> get-brcb-vals --refs "LD0/LLN0.brcbDeviceState"
  Fetching BRCB values for 1 reference(s)
  BRCB values (1 items):
    [0] LD0/LLN0.brcbDeviceState  rptID=LD0/LLN0$BR$brcbDeviceState rptEna=false datSet=dsDeviceState confRev=1 bufTm=2000 sqNum=0 intgPd=0
cms>
```
```bash
cms> # ======== 10. BRCB: 再启用报告（看 rptEna 顺序规则） ========
cms> set-brcb-vals --ref LD0/LLN0.brcbDeviceState --intg-pd 60000 --rpt-ena true
  Setting BRCB values: ref=LD0/LLN0.brcbDeviceState
  OK  BRCB values set for LD0/LLN0.brcbDeviceState
cms> get-brcb-vals --refs "LD0/LLN0.brcbDeviceState"
  Fetching BRCB values for 1 reference(s)
  BRCB values (1 items):
    [0] LD0/LLN0.brcbDeviceState  rptID=LD0/LLN0$BR$brcbDeviceState rptEna=true datSet=dsDeviceState confRev=1 bufTm=2000 sqNum=0 intgPd=60000
```

#### 8.7.4 读非缓存报告控制块值服务(GetURCBValues)

```bash
get-urcb-vals --refs "LD0/LLN0.urcbAin"
```

```bash
见8.7.5
```

### 8.7.5 设置非缓存报告控制块值服务(SetURCBValues)

```bash
set-urcb-vals --ref LD0/LLN0.urcbAin --rpt-id "MyCustomRptID
```

```bash
cms> # ======== 连接 ========
cms> connect --ap P_B5041A/S1
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with P_B5041A/S1 ...
  OK  Associated: P_B5041A/S1
cms>
```
```bash
cms> # ======== 1. URCB: 改 rptID ========
cms> get-urcb-vals --refs "LD0/LLN0.urcbAin"
  Fetching URCB values for 1 reference(s)
  URCB values (1 items):
    [0] LD0/LLN0.urcbAin  rptID=LD0/LLN0$RP$urcbAin rptEna=false datSet=dsAin confRev=1 bufTm=0 sqNum=0 intgPd=0
cms> set-urcb-vals --ref LD0/LLN0.urcbAin --rpt-id "MyCustomRptID
  Setting URCB values: ref=LD0/LLN0.urcbAin
  OK  URCB values set for LD0/LLN0.urcbAin
cms> get-urcb-vals --refs "LD0/LLN0.urcbAin"
  Fetching URCB values for 1 reference(s)
  URCB values (1 items):
    [0] LD0/LLN0.urcbAin  rptID=MyCustomRptID rptEna=false datSet=dsAin confRev=1 bufTm=5000 sqNum=0 intgPd=30000
cms>
```
```bash
cms> # ======== 2. URCB: 改 bufTm 和 intgPd ========
cms> set-urcb-vals --ref LD0/LLN0.urcbAin --buf-tm 5000 --intg-pd 30000
  Setting URCB values: ref=LD0/LLN0.urcbAin
  OK  URCB values set for LD0/LLN0.urcbAin
cms> get-urcb-vals --refs "LD0/LLN0.urcbAin"
  Fetching URCB values for 1 reference(s)
  URCB values (1 items):
    [0] LD0/LLN0.urcbAin  rptID=MyCustomRptID rptEna=false datSet=dsAin confRev=1 bufTm=5000 sqNum=0 intgPd=30000
cms>
```
```bash
cms> # ======== 3. URCB: 启用报告 ========
cms> set-urcb-vals --ref LD0/LLN0.urcbAin --rpt-ena true
  Setting URCB values: ref=LD0/LLN0.urcbAin
  OK  URCB values set for LD0/LLN0.urcbAin
cms> get-urcb-vals --refs "LD0/LLN0.urcbAin"
  Fetching URCB values for 1 reference(s)
  URCB values (1 items):
    [0] LD0/LLN0.urcbAin  rptID=MyCustomRptID rptEna=true datSet=dsAin confRev=1 bufTm=5000 sqNum=0 intgPd=30000
cms>
```
```bash
cms> # ======== 4. URCB: 触发总召唤 ========
cms> set-urcb-vals --ref LD0/LLN0.urcbAin --gi true
  Setting URCB values: ref=LD0/LLN0.urcbAin
  OK  URCB values set for LD0/LLN0.urcbAin
cms> get-urcb-vals --refs "LD0/LLN0.urcbAin"
  Fetching URCB values for 1 reference(s)
  URCB values (1 items):
    [0] LD0/LLN0.urcbAin  rptID=MyCustomRptID rptEna=true datSet=dsAin confRev=1 bufTm=5000 sqNum=0 intgPd=30000
cms>
```

### 8.8 Log

```bash
# 获取 LCB 值
get-lcb-vals --refs "<ref1> <ref2>..."
# 设置 LCB 值
set-lcb-vals --ref <logRef> [--log-ena true|false] [--intg-pd <ms>] [--datSet <name>]
# 按时间查询日志
query-log-by-time --ref <logRef> [--start <ms>] [--stop <ms>]
# 查询指定条目之后的日志
query-log-after --ref <logRef> --entry <entryId> [--start <ms>]
# 获取日志状态值
get-log-status --refs "<ref1> <ref2>..."
```

```bash
cms> get-lcb-vals --refs "LD0/LLN0.lcblog"
  Fetching LCB values for 1 reference(s)
    [LD0/LLN0.lcblog] logEna=true datSet=dsLog intgPd=0 logRef=LD0 trgOps=dc:false,qc:false,du:false,integrity:false,gi:false

cms> set-lcb-vals --ref LD0/LLN0.lcblog --log-ena true --intg-pd 5000
  Setting LCB values: ref=LD0/LLN0.lcblog
  OK  LCB values set for LD0/LLN0.lcblog

cms> get-lcb-vals --refs "LD0/LLN0.lcblog"
  Fetching LCB values for 1 reference(s)
    [LD0/LLN0.lcblog] logEna=true datSet=dsLog intgPd=5000 logRef=LD0 trgOps=dc:false,qc:false,du:false,integrity:false,gi:false

cms> query-log-by-time --ref LD0/LLN0.lcblog
  Querying log by time: ref=LD0/LLN0.lcblog
  OK  QueryLogByTime completed

cms> query-log-after --ref LD0/LLN0.lcblog --entry "000001"
  Querying log after entry: ref=LD0/LLN0.lcblog entry=000001
  OK  QueryLogAfter completed

cms> get-log-status --refs "LD0/LLN0.lcblog"
  Fetching log status for 1 reference(s)
    [LD0/LLN0.lcblog] oldEntrTm=0/0 newEntrTm=0/0
```

#### 8.8.1 日志条目(LogEntry)

数据结构的定义，并不是服务

#### 8.8.2 读日志控制块值服务(GetLCBValues)

获取日志控制块(LCB)的所有属性。参数见标准 **表52**。

```bash
get-lcb-vals --refs "<ref1> <ref2>..."
```

```bash
cms> connect --ap P_B5041A/S1
  Connecting to 127.0.0.1:8102 ...
  Connected, negotiating parameters ...
  Negotiated, associating with P_B5041A/S1 ...
  OK  Associated: P_B5041A/S1
cms> get-lcb-vals --refs "LD0/LLN0.lcblog"
  Fetching LCB values for 1 reference(s)
    [LD0/LLN0.lcblog] logEna=true datSet=dsLog intgPd=0 logRef=LD0 trgOps=dc:false,qc:false,du:false,integrity:false,gi:false
```

#### 8.8.3 设置日志控制块值服务(SetLCBValues)

修改日志控制块(LCB)内的一个或多个属性。属性设置顺序符合标准要求：`logEna` 为 `true` 时先设其他属性再设 `logEna`；为 `false` 时先设 `logEna` 再设其他属性。参数见标准 **表53**。

```bash
set-lcb-vals --ref <logRef> [--log-ena true|false] [--intg-pd <ms>] [--datSet <name>] [--opt-flds <flags>] [--log-ref <ref>] [--trg-ops <flags>] [--buf-tm <ms>]
```

```bash
cms> set-lcb-vals --ref LD0/LLN0.lcblog --log-ena true --intg-pd 5000
  Setting LCB values: ref=LD0/LLN0.lcblog
  OK  LCB values set for LD0/LLN0.lcblog
cms> get-lcb-vals --refs "LD0/LLN0.lcblog"
  Fetching LCB values for 1 reference(s)
    [LD0/LLN0.lcblog] logEna=true datSet=dsLog intgPd=5000 logRef=LD0 trgOps=dc:false,qc:false,du:false,integrity:false,gi:false
```

> **注意**：仅修改 `logEna`、`intgPd`、`datSet`、`optFlds`、`logRef`、`trgOps`、`bufTm` 属性，**不包含** `query-log-by-time` 和 `query-log-after` 服务参数。

### 8.8.4 按时间查询日志服务(QueryLogByTime)

```bash
query-log-by-time --ref <logRef> [--start <ms>] [--stop <ms>]
```

下边是用脚本生成了10条模拟的本地log，然后用命令请求
```bash
cms> query-log-by-time --ref LD0/LLN0.lcblog --start 1700000000000 --stop 1700000100000
  Querying log by time: ref=LD0/LLN0.lcblog
  Log entries (1 entries):
    [0] 1970-01-01 08:00  id=
           [0]   value=0
  OK  QueryLogByTime completed
cms> query-log-by-time --ref LD0/LLN0.lcblog
  Querying log by time: ref=LD0/LLN0.lcblog
  Log entries (10 entries):
    [0] 2024-02-29 08:01  id=000001
           [0] LD0/LLN0.Mod.stVal  value=11
           [1] LD0/LLN0.Beh.stVal  value=12
           [2] LD0/LLN0.Health.stVal  value=13
    [1] 2024-02-29 08:02  id=000002
           [0] LD0/LLN0.Mod.stVal  value=21
           [1] LD0/LLN0.Beh.stVal  value=22
           [2] LD0/LLN0.Health.stVal  value=23
    [2] 2024-02-29 08:03  id=000003
           [0] LD0/LLN0.Mod.stVal  value=31
           [1] LD0/LLN0.Beh.stVal  value=32
           [2] LD0/LLN0.Health.stVal  value=33
    [3] 2024-02-29 08:04  id=000004
           [0] LD0/LLN0.Mod.stVal  value=41
           [1] LD0/LLN0.Beh.stVal  value=42
           [2] LD0/LLN0.Health.stVal  value=43
    [4] 2024-02-29 08:05  id=000005
           [0] LD0/LLN0.Mod.stVal  value=51
           [1] LD0/LLN0.Beh.stVal  value=52
           [2] LD0/LLN0.Health.stVal  value=53
    [5] 2024-02-29 08:06  id=000006
           [0] LD0/LLN0.Mod.stVal  value=61
           [1] LD0/LLN0.Beh.stVal  value=62
           [2] LD0/LLN0.Health.stVal  value=63
    [6] 2024-02-29 08:07  id=000007
           [0] LD0/LLN0.Mod.stVal  value=71
           [1] LD0/LLN0.Beh.stVal  value=72
           [2] LD0/LLN0.Health.stVal  value=73
    [7] 2024-02-29 08:08  id=000008
           [0] LD0/LLN0.Mod.stVal  value=81
           [1] LD0/LLN0.Beh.stVal  value=82
           [2] LD0/LLN0.Health.stVal  value=83
    [8] 2024-02-29 08:09  id=000009
           [0] LD0/LLN0.Mod.stVal  value=91
           [1] LD0/LLN0.Beh.stVal  value=92
           [2] LD0/LLN0.Health.stVal  value=93
    [9] 2024-02-29 08:10  id=000010
           [0] LD0/LLN0.Mod.stVal  value=101
           [1] LD0/LLN0.Beh.stVal  value=102
           [2] LD0/LLN0.Health.stVal  value=103
  OK  QueryLogByTime completed
```

#### 8.8.5 查询指定条目之后的日志服务(QueryLogAfter)

```bash
cms> query-log-after --ref LD0/LLN0.lcblog --entry "000001"
  Querying log after entry: ref=LD0/LLN0.lcblog entry=000001
  OK  QueryLogAfter completed
```

#### 8.8.6 读日志状态值服务(GetLogStatusValues)

```bash
cms> get-log-status --refs "LD0/LLN0.lcblog"
  Fetching log status for 1 reference(s)
    [LD0/LLN0.lcblog] oldEntrTm=60000/19782 newEntrTm=600000/19782
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

### 8.12.1 下载文件

```bash
cms> get-file --file /test.txt --output D:/downloaded.txt
```

```bash
cms> get-file --file /test.txt --output D:/downloaded.txt
  Downloading /test.txt ...
  OK  Saved /test.txt to D:/downloaded.txt (size: 26 bytes)
```

### 8.12.2 上传文件

```bash
cms> set-file --local D:/test.txt --remote /test.txt
```

```bash
cms> set-file --local D:/test.txt --remote /test.txt
  Uploading D:/test.txt -> /test.txt ...        
  OK  Uploaded D:/test.txt to /test.txt successfully
```

### 8.12.3 删除文件

```bash
cms> delete-file --file /test.txt
```

```bash
cms> delete-file --file /test.txt
  Deleting file /test.txt
  OK  Deleted file /test.txt successfully 
```

### 8.12.4 读文件属性值服务

```bash
get-file-attrs --file /test.txt
```

```bash
cms> set-file --local D:/test.txt --remote /test.txt
  Uploading D:/test.txt -> /test.txt ...        
  OK  Uploaded D:/test.txt to /test.txt successfully
cms> get-file-attrs --file /test.txt
  Fetching file attributes for /test.txt
    fileName=/test.txt
    fileSize=9
    lastModified=1783412991932
    checkSum=871209221
cms>
```

### 8.12.5 列文件目录服务(GetFileDirectory)

```bash
cms> get-file-dir --path /
```

```bash
cms> get-file-dir --path /
  Fetching file directory...
    /test.txt  size=9  modified=1783412991932  crc32=871209221
```

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


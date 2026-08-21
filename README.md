# Communication Message Specification（CMS）

***

## CMS概述

**CMS（通信报文规范）就是国家电网为了摆脱对国外技术的依赖，专门搞出来的**“MMS协议国产替代版”。它的核心使命是实现电力通信的**自主可控**，同时解决老协议笨重、低效的问题。

- **瘦身提速（PER编码）**：把原来MMS那种臃肿的BER编码换成了紧凑的**PER编码**。
- **砍掉中间商（直接映射）**：以前MMS需要把电力指令层层转译（七层模型），现在CMS**直接映射TCP/IP**。
- **穿上防弹衣（国密加密）**：最大的亮点在于**安全性**。CMS原生支持国密算法加密。

本项目就是制作CMS标准的代码实现。PER 编解码由 Rust 侧负责：基于 [rasn](https://github.com/rustasn/rasn) 开源库，通过 `csasn1` 生成器从 `assets/cms.asn1` 生成 Java 数据类，并借助 Rust FFI 完成 APER 编解码。Java 侧在此基础上手写封装。项目目录包含下边内容：

- **encoding and decoding**:
  - [rasn](docs/impl/01-rasn.md):rasn 开源库，完成 PER 编解码。本项目参与了rasn的开源贡献。
  - [csasn1](docs/impl/02-csasn1.md): 基于 csasn1 生成器，从 `assets/cms.asn1` 生成 jcms-data 的 Java 代码，同时提供 Rust FFI 编解码入口。（除了java，csasn1也提供生成rust与python模板代码）
- **jcms-data**（inner-data）
  - [data](docs/impl/03-jcms-data.md): 由 csasn1 自动生成的 `Inner*` POJO 类，是数据结构的单一真相源，以 JSON（JER）作为与上层交换的载体。
- **jcms-core**
  - [core](docs/impl/04-jcms-core.md): 手写封装，将 jcms-data 打包为 `CmsType` 等 Cms* 基类体系（含 §8 服务报文段）。
  - [info](docs/impl/04a-jcms-info.md): 一些文档性质的枚举和说明。(core/info子模块进一步介绍，与代码规则)
- **jcms-utils**
  - [config](docs/impl/jcms-config.md): 配置模块。
  - [scl](docs/impl/jcms-scl.md):  scd文件解析模块。
  - [security](docs/impl/jcms-security.md): 安全协议。
  - [transport](docs/impl/jcms-transport.md): 传输层基础构建。
- **jcms-app**
  - [node](docs/impl/jcms-node.md): dlt2811 §6传输层封装。
  - [handler](docs/impl/jcms-handler.md): 各种处理器，比如客户端，服务器，命令行交互界面。
  - [console](docs/impl/jcms-console.md): 命令行交互界面。
- [国产自主可控新一代通信标准CMS之总览篇](https://zhuanlan.zhihu.com/p/520653213)

***

## 使用方法

- 自定义方法
  - json格式输出
  - 清空命令行
  - 显示报文信息
- 8.1 [通信服务基础](docs/usage/8-1.md)
- 8.2 连接
  - 8.2.1 [associate](docs/usage/8-2-1.md)
  - 8.2.2 [release](docs/usage/8-2-2.md)
  - 8.2.3 [abort](docs/usage/8-2-3.md)
- 8.3 目录
  - 8.3.1 [server-dir](docs/usage/8-3-1.md)
  - 8.3.2 [ld-dir](docs/usage/8-3-2.md)
  - 8.3.3 [ln-dir](docs/usage/8-3-3.md)
  - 8.3.4 [all-data](docs/usage/8-3-4.md)
  - 8.3.5 [all-def](docs/usage/8-3-5.md)
  - 8.3.6 [all-cb](docs/usage/8-3-6.md)
- 8.4 数据
  - 8.4.1 [get-data-values](docs/usage/8-4-1.md)
  - 8.4.2 [set-data-values](docs/usage/8-4-2.md)
  - 8.4.3 [data-dir](docs/usage/8-4-3.md)
  - 8.4.4 [get-data-def](docs/usage/8-4-4.md)
- 8.5 数据库
  - 8.5.1 [get-dataset-values](docs/usage/8-5-1.md)
  - 8.5.2 [set-dataset-values](docs/usage/8-5-2.md)
  - 8.5.3 [create-dataset](docs/usage/8-5-3.md)
  - 8.5.4 [delete-dataset](docs/usage/8-5-4.md)
  - 8.5.5 [get-dataset-dir](docs/usage/8-5-5.md)
- 8.6 定值（SG Block）
  - 8.6.1 [select-active-sg](docs/usage/8-6-1.md)
  - 8.6.2 [select-edit-sg](docs/usage/8-6-2.md)
  - 8.6.3 [set-edit-sg](docs/usage/8-6-3.md)
  - 8.6.4 [confirm-edit-sg](docs/usage/8-6-4.md)
  - 8.6.5 [get-edit-sg](docs/usage/8-6-5.md)
  - 8.6.6 [sgcb-vals](docs/usage/8-6-6.md)
- 8.7 报告（Report）
  - 8.7.1 [report](docs/usage/8-7-1.md)（服务器推送）
  - 8.7.2 [get-brcb-vals](docs/usage/8-7-2.md)
  - 8.7.3 [set-brcb-vals](docs/usage/8-7-3.md)
  - 8.7.4 [get-urcb-vals](docs/usage/8-7-4.md)
  - 8.7.5 [set-urcb-vals](docs/usage/8-7-5.md)
- 8.8 日志（Log）
  - 8.8.2 [get-lcb-vals](docs/usage/8-8-2.md)
  - 8.8.3 [set-lcb-vals](docs/usage/8-8-3.md)
  - 8.8.4 [query-log-by-time](docs/usage/8-8-4.md)
  - 8.8.5 [query-log-after](docs/usage/8-8-5.md)
  - 8.8.6 [get-log-status](docs/usage/8-8-6.md)
- 8.9 GOOSE
  - 8.9.1 [send-goose](docs/usage/8-9-1.md)（服务器推送）
  - 8.9.2 [get-go-ref](docs/usage/8-9-2.md)
  - 8.9.3 [get-goose-elem](docs/usage/8-9-3.md)
  - 8.9.4 [get-gocb-vals](docs/usage/8-9-4.md)
  - 8.9.5 [set-gocb-vals](docs/usage/8-9-5.md)
- 8.10 多播采样值（MSV）
  - 8.10.1 [send-msv](docs/usage/8-10-1.md)（服务器推送）
  - 8.10.2 [get-msvcb-vals](docs/usage/8-10-2.md)
  - 8.10.3 [set-msvcb-vals](docs/usage/8-10-3.md)
- 8.13 RPC
  - 8.13.2 [rpc-iface-dir](docs/usage/8-13-2.md)
  - 8.13.3 [rpc-method-dir](docs/usage/8-13-3.md)
  - 8.13.4 [rpc-iface-def](docs/usage/8-13-4.md)
  - 8.13.5 [rpc-method-def](docs/usage/8-13-5.md)
  - 8.13.6 [rpc-call](docs/usage/8-13-6.md)
- 8.11 控制（Control）
  - 8.11.1 [select](docs/usage/8-11-1.md)
  - 8.11.2 [select-with-value](docs/usage/8-11-2.md)
  - 8.11.3 [operate](docs/usage/8-11-3.md)
  - 8.11.4 [cancel](docs/usage/8-11-4.md)
  - 8.11.5 [command-termination](docs/usage/8-11-5.md)（服务器推送）
  - 8.11.6 [time-act-ope](docs/usage/8-11-6.md)
  - 8.11.7 [time-act-ope-term](docs/usage/8-11-7.md)（服务器推送）
- 8.12 文件（File）
  - 8.12.1 [get-file](docs/usage/8-12-1.md)
  - 8.12.2 [set-file](docs/usage/8-12-2.md)
  - 8.12.3 [delete-file](docs/usage/8-12-3.md)
  - 8.12.4 [get-file-attrs](docs/usage/8-12-4.md)
  - 8.12.5 [get-file-dir](docs/usage/8-12-5.md)
- 8.13 RPC
  - 8.13.2 [rpc-iface-dir](docs/usage/8-13-2.md)
  - 8.13.3 [rpc-method-dir](docs/usage/8-13-3.md)
  - 8.13.4 [rpc-iface-def](docs/usage/8-13-4.md)
  - 8.13.5 [rpc-method-def](docs/usage/8-13-5.md)
  - 8.13.6 [rpc-call](docs/usage/8-13-6.md)
- 8.14 测试：[test](docs/usage/8-14.md)
- 8.15 协商：[negotiate](docs/usage/8-15.md)

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
# 设置服务端每页最大返回条数（用于测试自动续拉分页）
# 服务端开启后，客户端搭配 --auto-pull true 即可测试分页续拉逻辑
max-entries 5
# 恢复默认
max-entries 0
# 查看当前值
max-entries
```

### 自动续拉分页（`--auto-pull`）

部分支持分页的命令（`server-dir`、`ld-dir`、`ln-dir`、`all-data`、`all-def`、`all-cb`、`data-dir`、`get-dataset-dir`、`get-dataset-values`）支持 `--auto-pull` 参数。启用后客户端会自动跟随 `moreFollows` 标志拉取所有分页数据，无需手动传 `--after`：

```bash
# 自动拉取所有逻辑设备
cms server-dir --auto-pull true
# 自动拉取所有数据值（配合服务端 max-entries 可以测试分页）
cms all-data --ln LD0 --auto-pull true
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

## csasn1（Rust 生成器）

### 说明

本部分基于 [rasn](https://github.com/rustasn/rasn) 开源库，从 `assets/cms.asn1` 生成 jcms-data 的 `Inner*` POJO 类，并提供 Rust FFI 完成 APER 编解码。生成的 Java 类由 jcms-core 手写封装为 `Cms*` 类型。

### per 编码规则

- **INTEGER**
  - 没有前导码、没有 Tag、没有自带的 Length；是否对齐，只看它的取值范围（`lb..ub`）
  - 对于 2811，小的不需要对齐（比如 `Boolean`、`Quality`），其他的都需要对齐（因为 **range > 255**）
  - 不需要对齐的举例：`Boolean ::= INTEGER (0..1)`、`SmpMod ::= INTEGER (0..2)`
  - 需要对齐的举例：`Int16U ::= INTEGER (0..65535)`
- **OCTET STRING**
  - 永远都是对齐，长度字段视情况而定。
  - `OCTET STRING (SIZE(n))`：固定长度。不需要编码长度，因为长度已在 ASN.1 文件中规定。
  - `OCTET STRING (SIZE(lb..ub)), ub < 64K`：变长。长度为字符串实际长度。若长度超过 64K，长度编码会越界，必须改用不受约束长度决定因子。
  - 无约束（只在 Data 中有一个）：需要对齐（实际与带约束的编码一样，只是不检查长度）。
- **BIT STRING**
  - `BIT STRING (SIZE(n))`：固定 BIT STRING，n ≤ 16 → 不对齐；n > 16 或变长 → 对齐。
  - `BIT STRING (SIZE(lb..ub)), ub < 64K`：需要对齐，需要编码长度，长度是 **bit 数**。
  - 无约束（只在 Data 中有一个）：需要对齐（实际与带约束的编码一样，只是不检查长度）。
- **VisibleString**
  - 已知倍数字符串，1 字符 = 1 字节。
  - 约束不编码；只有实际长度（字符数 = 字节数）可能编码。
  - 固定 `SIZE(n)`：`n × 8 < 16` → 不对齐；`n × 8 > 16` → 对齐；无长度字段。
  - 变长 / 无约束：先编码长度决定因子，内容对齐（`> 16 bit` 时）。
- **UTF8String**
  - 标准实际上并没有定义，这里按 VisibleString 实现，长度也为字节数。仅在 Data 中使用。
- **OPTIONAL**
  - 若字段为可选，标准要求在前面加一个 bit 表示该字段是否存在。
- **SEQUENCE OF**
  - 此类数组需要编码元素个数。

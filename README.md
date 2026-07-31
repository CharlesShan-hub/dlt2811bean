# Communication Message Specification（CMS）

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
* 8.1 [通信服务基础](docs/usage/8-1.md)
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
* 8.9 GOOSE
  * 8.9.1 [send-goose](docs/usage/8-9-1.md)（服务器推送）
  * 8.9.2 [get-go-ref](docs/usage/8-9-2.md)
  * 8.9.3 [get-goose-elem](docs/usage/8-9-3.md)
  * 8.9.4 [get-gocb-vals](docs/usage/8-9-4.md)
  * 8.9.5 [set-gocb-vals](docs/usage/8-9-5.md)
* 8.10 多播采样值（MSV）
  * 8.10.1 [send-msv](docs/usage/8-10-1.md)（服务器推送）
  * 8.10.2 [get-msvcb-vals](docs/usage/8-10-2.md)
  * 8.10.3 [set-msvcb-vals](docs/usage/8-10-3.md)
* 8.13 RPC
  * 8.13.2 [rpc-iface-dir](docs/usage/8-13-2.md)
  * 8.13.3 [rpc-method-dir](docs/usage/8-13-3.md)
  * 8.13.4 [rpc-iface-def](docs/usage/8-13-4.md)
  * 8.13.5 [rpc-method-def](docs/usage/8-13-5.md)
  * 8.13.6 [rpc-call](docs/usage/8-13-6.md)
* 8.11 控制（Control）
  * 8.11.1 [select](docs/usage/8-11-1.md)
  * 8.11.2 [select-with-value](docs/usage/8-11-2.md)
  * 8.11.3 [operate](docs/usage/8-11-3.md)
  * 8.11.4 [cancel](docs/usage/8-11-4.md)
  * 8.11.5 [command-termination](docs/usage/8-11-5.md)（服务器推送）
  * 8.11.6 [time-act-ope](docs/usage/8-11-6.md)
  * 8.11.7 [time-act-ope-term](docs/usage/8-11-7.md)（服务器推送）
* 8.12 文件（File）
  * 8.12.1 [get-file](docs/usage/8-12-1.md)
  * 8.12.2 [set-file](docs/usage/8-12-2.md)
  * 8.12.3 [delete-file](docs/usage/8-12-3.md)
  * 8.12.4 [get-file-attrs](docs/usage/8-12-4.md)
  * 8.12.5 [get-file-dir](docs/usage/8-12-5.md)
* 8.13 RPC
  * 8.13.2 [rpc-iface-dir](docs/usage/8-13-2.md)
  * 8.13.3 [rpc-method-dir](docs/usage/8-13-3.md)
  * 8.13.4 [rpc-iface-def](docs/usage/8-13-4.md)
  * 8.13.5 [rpc-method-def](docs/usage/8-13-5.md)
  * 8.13.6 [rpc-call](docs/usage/8-13-6.md)
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
























toolName: CompactFake
            
status: success
          
          

        
            
toolName: view_folder
            
status: success
          
d:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\main\java\com\ysh\jcms\data
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\main\java\com\ysh\jcms\data\core\CmsChoice.java
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\main\java\com\ysh\jcms\data\core\CmsChoice.java
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\main\java\com\ysh\jcms\data\core\CmsScalar.java
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\main\java\com\ysh\jcms\data\core\CmsBits.java
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\main\java\com\ysh\jcms\data\core\CmsSequence.java
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\main\java\com\ysh\jcms\data\core\CmsType.java
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\main\java\com\ysh\jcms\data\choice\CmsData.java
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-data\src\main\java\com\ysh\jcms\data\InnerBase.java
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\main\java\com\ysh\jcms\data\core\CmsEnum.java
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-core\src\main\java\com\ysh\jcms\data\core\CmsChoice.java
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-data\src\main\java\com\ysh\jcms\data\DefaultInnerVisibleString.java
          

        
            
toolName: search_by_regex
            
status: failed
          
query: catch \(Exception (ignored|e\) \{\}\)
          

        
            
toolName: search_by_regex
            
status: success
          
query: toJsonValue|_v
          

        
            
toolName: search_by_regex
            
status: success
          
query: catch \(Exception \w+\) \{\s*\}
          

        
            
toolName: search_by_regex
            
status: success
          
query: String json = .*_v|writeValueAsString\(.*_v|toJsonValue
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-data\src\main\java\com\ysh\jcms\data\InnerBase.java
          

        
            
toolName: view_files
            
status: failed
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-data\src\main\java\com\ysh\jcms\data\InnerUrgb.java
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-data\src\main\java\com\ysh\jcms\data\InnerURCB.java
          

        
            
toolName: search_by_regex
            
status: success
          
query: lombok|@Data|equals|hashCode|class Inner|@JsonValue|toJsonValue
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\work\standard\dlt2811bean\cms\jcms\jcms-data\src\main\java\com\ysh\jcms\data\InnerUtcTime.java
          

        
            
toolName: view_files
            
status: success
          
            
filePath: d:\project\cs\csasn1\src\generator\java\gen_newtype.rs
          
整体看完了核心链路（`CmsType` / `CmsScalar` / `CmsBits` / `CmsSequence` / `CmsChoice` / `InnerBase` + 生成器模板）。这轮 `_v` 统一做得相当干净，数据流基本打通。下面按优先级给出评审结论。

## 🔴 正确性隐患（建议优先处理）

**1. 生成器对 SEQUENCE / NEWTYPE 还在用 Lombok `@Data`，但类里已经没有任何实例字段了 → equals/hashCode 是"恒真"的**

看生成的 [InnerUtcTime.java](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-data/src/main/java/com/ysh/jcms/data/InnerUtcTime.java#L18-L19) 和 [InnerURCB.java](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-data/src/main/java/com/ysh/jcms/data/InnerURCB.java#L24-L27)：`@Data` 还在，但类里只剩静态 `MAPPER` 和方法，数据全在父类 `_v` 里。Lombok 的 equals 只比较**本类声明的字段**——零字段时生成的 `equals()` 对同类实例**无条件返回 true**、`hashCode()` 是常量。而 [CmsType.equals](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsType.java#L70-L76) 委托给 `inner.equals(...)`。

这意味着：**所有 SEQUENCE/NEWTYPE 的相等性断言实际从未真正比较过数据**，"全对"有一部分是"恒真"假象。CHOICE 的 [gen_choice.rs](file:///d:/project/cs/csasn1/src/generator/java/gen_choice.rs#L86-L103) 已经手写了 `_v` 版 equals，建议 struct/newtype 也照抄同一模式（比较 `_v` 的键 + 各键值），同时把 `@Data` 从生成器里彻底删掉。

**2. `syncInnerToInner` 破坏了"共享 `_v`"不变式**（[CmsChoice.java:L370-380](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsChoice.java#L370-L380)）

INNER 变体（DefaultInner*）每次 encode 都新建一个 sub-map 塞进父 `_v`，把 `val._v` 变成了孤儿引用，还把 octet-string 的 byte[] 就地换成了 hex String。注释还是老的（"DefaultInner* types store data in a direct `value` field"——已过时）。根因在 [choice(int)](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsChoice.java#L150-L166)：只有 `CmsType` 字段会被 `inner._v.put(vi.name, w.inner._v)` 播种，`InnerBase` 字段没有。

修法很顺：`choice(int)` 对 isInner 字段同样把 `val._v` 播种进父 `_v`，然后 `syncInnerToInner` 退化为纯共享刷新（甚至直接删掉）——byte[] 留在共享 map 里，JER 的 hex 由 [InnerBase 的 byte[] 序列化器](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-data/src/main/java/com/ysh/jcms/data/InnerBase.java#L96-L109) 在 JSON 边界完成，行为等价且别名不再断裂。

**3. decode 后残留未选中的默认变体**

早前 DBG 显示 `{_choice=visible-string, error={_=1}, ...}`——`error={_=1}` 是构造默认值。encode 路径会清理，但 decode 后 `_v` 不干净，`toJsonValue()`/日志会带出脏数据。建议在 `syncFromInner` 里加一个与 encode 对称的清理循环。

## 🟡 代码结构

- **LIST 和 ARRAY 两个 sync 模式实现逐字节相同**（[syncListToInner](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsChoice.java#L397-L406) vs `syncArrayToInner`、`syncListFromInner` vs `syncArrayFromInner`），可以合并成一个。
- **`CmsData.alt_sequence` / `alt_bit_string` 游离在 @Choice 体系外**（手工 List 初始化 + RAW 分支），建议给 ARRAY 变体也走注解注入，统一两条路径。
- `syncScalarToInner` 里 `wrapper.syncToInner()` + `toJsonValue()` 的组合，和 [CmsSequence.java:L250](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsSequence.java#L250) 的写法重复，可抽一个"wrapper → JSON 值"工具。

## 🟡 性能

- **反射元数据每实例重建**：`injectFields`/`injectChoices`/`rebind*`/`CmsBits.bitCount()` 每次都 `getClass().getFields()` + 注解扫描。decode 每来一包就 new 一批实例 → 每包都在反射。建议用 `ClassValue` 或静态 `ConcurrentHashMap` 缓存每类的字段元数据（这些元数据类加载后就不变）。这是 PDU 高频收发场景最大的收益点。
- **`InnerBase.toString()` 每次 `new ObjectMapper()`**（[InnerBase.java:L114-118](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-data/src/main/java/com/ysh/jcms/data/InnerBase.java#L114-L118)）——日志热路径会爆炸。另外**每个生成的 Inner 类各持一个私有 `MAPPER`**（N 个类 = N 个 mapper + 各注册序列化器），建议收敛为 `InnerBase` 单一静态共享实例（配置完成后线程安全）。
- **`hex()`/`bitStringHex()` 逐字节 `String.format("%02x")`**——编解码热路径，换成静态查表法。
- **`CmsType.decode()` 每次 `getMethod("decode", ...)` + `Method.invoke`**（[CmsType.java:L57-67](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsType.java#L57-L67)），方法引用可按 Inner 类缓存。

## 🟠 安全与健壮性

- [CmsChoice.java:L112](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsChoice.java#L112) 和 [:L205](file:///d:/project/work/standard/dlt2811bean/cms/jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsChoice.java#L205) 的 `catch (Exception ignored) {}` 会吞掉 rebind/choice 的失败——这类静默失败正是别名断裂 bug 的温床，至少 warn 日志。
- **`unhex()` 无输入校验**：奇数长度会越界，非法字符产出垃圾字节；`parseBitStringHex` 的 `Integer.parseInt` 对坏输入抛 `NumberFormatException`。协议库的 decode 面向不可信数据，建议统一校验入口。
- 建议补一个**随机字节模糊回环测试**（随机 APER 字节 → decode → 不崩溃），顺带验证 Rust FFI 的健壮性。
- `_v` 共享 map 是可变共享状态，建议在 `CmsType` 文档里注明"实例非线程安全、单线程使用"（服务端并发响应时容易踩）。

## 🟢 功能/工程化

- **生成器漂移防护**：加一个 `gen-check` 目标（重新生成到临时目录 → diff 已提交文件），防止"模板改了、生成物没跟"再次发生——这轮已经因此踩过坑。
- **`encodeTest()`** 每个生成类都有，纯调试用、生产无效载荷，建议挪到测试辅助或条件编译。
- **库级 encode/decode 钩子**（可插拔监听器）：CLI 已有 trace-pdu，库层提供一个监听器接口能方便服务端线上排障。
- `CmsEnum` 的位宽推导已经很优雅，可选：给 `value(int)` 增加负值/越界统一校验策略的开关。

---

其中 **#1（Lombok 恒真 equals）** 和 **#2（INNER 别名断裂）** 是真正值得动手的两处，我可以分别给出修改方案。要我先把这两个的改动预览做出来吗？还是按你的节奏，你自己先验证当前状态？
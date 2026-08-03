# ap-dir — 列出可用的 AccessPoint（本地配置命令）

## 概述

列出 SCD 文件中所有可用的 **AccessPoint**（访问点），供 `connect --ap IED/AP` 使用。

这是**本地配置命令**，不需要连接服务器、也不发任何协议报文。因为 DL/T 2811（与 IEC 61850 一致）**没有"枚举 AP"的服务**——AP 属于 SCL 配置范畴，只有逻辑设备（LD）才是运行时可枚举的对象（`server-dir`）。

对应 CLI 命令：`ap-dir`

## 命令参数

| 参数 | 说明 | 默认值 |
|------|------|--------|
| `--scd` | SCD 文件路径（绝对路径或 classpath 资源） | 配置 `server.sclFiles[0]` |
| `--ied` | 只列出指定 IED 的 AP（如 `C_B5041X`） | 全部 IED |
| `--json` | JSON 格式输出 | — |

## 使用场景

```bash
cms> ap-dir --scd config/sample-scd-full.scd;
  SCL: config/sample-scd-full.scd
  IED C_B5041X:
    [0] S1
    [1] S2
  IED C_B5042X:
    [0] M1
```

# 只看某个 IED
cms> ap-dir --ied C_B5041X;
  SCL: config/sample-scd-full.scd
  IED C_B5041X:
    [0] S1
    [1] S2

# JSON 模式（脚本调用）
cms> ap-dir --json
{"success":true,"data":[{"ied":"C_B5041X","aps":["S1","S2"]},{"ied":"C_B5042X","aps":["M1"]}]}
```

拿到 AP 名后，即可连接：

```bash
cms> connect --ap C_B5041X/S1;
```

## 与协议的关系

| 层面 | 机制 | 说明 |
|------|------|------|
| **AP 枚举** | SCD 配置（`ap-dir` 读取） | 协议无此服务，AP 是静态配置概念 |
| **LD 枚举** | 协议服务 `server-dir`（§8.3.1） | 连接后在线枚举逻辑设备 |
| **AP 引用** | 协议参数 `serverAccessPointReference` | 关联服务（Associate）中显式指定 |

先 `ap-dir`（读配置知道有哪些 AP），再 `connect --ap`（指定 AP 关联），之后才能 `server-dir` 等服务。

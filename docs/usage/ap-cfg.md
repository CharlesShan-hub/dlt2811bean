# ap-cfg — 查看/修改 AP 来源配置（本地配置命令）

## 概述

**运行时**查看和修改 AccessPoint 的来源配置（对齐 `trace-pdu` 的内存开关模式）。修改后立即生效，**无需改 yaml 重启进程**，影响 `ap-dir` 的读取来源。

对应 CLI 命令：`ap-cfg`

## 命令参数

| 参数 | 说明 | 默认值 |
|------|------|--------|
| `--source` | AP 来源：`scd`=从 SCD 文件读，`list`=从 defaultAps 列表读 | — |
| `--json` | JSON 格式输出 | — |

无参数时只显示当前配置。

## 使用场景

```bash
# 设置来源（只设置，简洁确认）
cms> ap-cfg --source scd;
  OK  AP 来源已设为: scd
cms> ap-cfg --source list;
  OK  AP 来源已设为: list

# 查看当前来源（显示上一次设置的结果，AP 统一为 IED/AP 引用格式）
cms> ap-cfg;
  AP 来源: list（从 defaultAps 列表读）
    [0] C_B5041X/S1
    [1] C_B5042X/M1

# scd 模式下查看：显示 SCD 路径并解析出其中的 AP（同样为 IED/AP 格式）
cms> ap-cfg;
  AP 来源: scd（从 SCD 文件读）
  SCL: C:\Users\17428\Downloads\longyang\big-file.scd
    [0] C_B5041X/S1
    [1] C_B5041X/S2
    [2] C_B5042X/M1

# 设置后 ap-dir 立即按新来源读取
cms> ap-cfg --source list;
cms> ap-dir;
  AP 列表（client.accessPoint.defaultAps）
  AccessPoint:
    [0] C_B5041X/S1
    [1] C_B5042X/M1

# JSON 模式（查看）
cms> ap-cfg --json
{"success":true,"data":{"fromScd":false,"defaultAps":["C_B5041X/S1","C_B5042X/M1"]}}
```

## 与 ap-dir 的关系

`ap-cfg` 与 `ap-dir` 共享同一份内存配置（`client.accessPoint`），因此：

```
ap-cfg --source list   →  ap-dir 改为输出 defaultAps
ap-cfg --source scd    →  ap-dir 改为读 SCD 文件
```

**注意**：`ap-cfg` 的修改只对当前进程生效，不写回 yaml。要持久化需同步修改配置：

```yaml
client:
  accessPoint:
    fromScd: true        # true=从 SCD 读，false=从 defaultAps 列表读
    defaultAps:
      - C_B5041X/S1
```

## 与协议的关系

| 层面 | 机制 | 说明 |
|------|------|------|
| **AP 来源** | `client.accessPoint.fromScd` | `scd`=读 SCD 文件，`list`=读静态列表 |
| **静态列表** | `client.accessPoint.defaultAps` | 无 SCD 场景下的 AP 引用清单 |
| **运行时切换** | `ap-cfg` | 内存开关，立即生效 |

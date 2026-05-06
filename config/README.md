# SCD 示例文件

本目录包含从 GitHub 下载的 IEC 61850 SCD 示例文件，用于 DL/T 2811 协议实现的测试和开发。

## 文件列表

### 1. sample-scd-full.scd

- **来源**: https://github.com/gillerman/IEC_61850_SCL_2_Nodeset
- **原始链接**: https://raw.githubusercontent.com/gillerman/IEC_61850_SCL_2_Nodeset/master/Example61850TestSCD.xml
- **说明**: 完整变电站 SCD 示例，包含 Substation 拓扑（变压器、母线、间隔）、Communication 配置（IP、GOOSE、SMV）、12 个 IED 以及完整的 DataTypeTemplates。

### 2. sample-scd-relay.scd

- **来源**: https://github.com/robidev/iec61850_open_server
- **原始链接**: https://raw.githubusercontent.com/robidev/iec61850_open_server/master/scd/protection_relay.scd
- **说明**: 保护继电器 SCD 示例，包含完整的保护逻辑功能（PTOC、PIOC、PDIS、PDIF、XCBR 等），以及 Inputs 信号映射链。

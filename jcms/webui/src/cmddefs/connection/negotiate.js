import { P_APDU, P_ASDU, P_VERSION } from '../common.js'
import { asn1Negotiate } from '../asn1.js'

export default {
  title: '协商 negotiate (8.15)',
  desc: '单独执行参数协商',
  asn1: asn1Negotiate,
  doc: `## 协议原文

### 服务参数

关联协商服务用于客户和服务器之间协商双方的服务参数，服务的参数见表82。

**表82 关联协商服务参数**

| 服务/参数           | 所属     | 数据类型          |
| --------------- | ------ | ------------- |
| **Request**     | <br /> | <br />        |
| \`apduSize\`        | <br /> | \`INT16U\`        |
| \`asduSize\`        | <br /> | \`INT32U\`        |
| \`protocolVersion\` | <br /> | \`INT32U\`        |
| **Response+**   | <br /> | <br />        |
| \`apduSize\`        | <br /> | \`INT16U\`        |
| \`asduSize\`        | <br /> | \`INT32U\`        |
| \`protocolVersion\` | <br /> | \`INT32U\`        |
| \`modelVersion\`    | <br /> | \`VisibleString\` |
| **Response-**   | <br /> | <br />        |
| \`serviceError\`    | <br /> | \`ServiceError\`  |

\`apduSize\`参数用于协商客户和服务器的APDU帧大小。\`asduSize\`参数用于声明客户和服务器所支持的最大ASDU大小。\`protocolVersion\`用于声明客户和服务器所使用的协议版本号。\`modelVersion\`参数用于声明服务器的模型版本。

### 服务要求

建立TCP连接后，关联服务之前，应首先使用该服务协商通信双方服务参数。后续通信服务应按照协商后的服务参数执行。符合以下要求：

1. 服务器应根据客户该参数，结合自身APDU帧大小，返回可支持的APDU帧大小，作为协商结果。通信双方后续通信服务中APDU帧大小应采用服务器响应的该参数。
2. \`apduSize\`大于\`asduSize\`表示支持分帧传输，小于\`apduSize\`表示不能实现分帧传输数据。通信双方应记录对侧所能支持的ASDU大小，并据此组织ASDU数据帧，确保不超出对侧的能力。
3. 接收方应检查自身是否能够支持对端协议的版本，并采用对应版本的协议报文进行通信。无法支持该版本时，应返回协商失败。

## 设计要点

### 基本定义

1. **\`apduSize\`**：代表一个报文（包含头和服务数据）的整体大小。要注意的是服务数据部分可能是完整的也可能是不完整的。如果是不完整的就是多个 APDU 中的 ASDU 拼起来代表一个完整 ASDU。
2. **\`asduSize\`**：代表一个完整的服务数据大小。APDU 可能比较小，ASDU 比较大，那就把 ASDU 拆成很多份，放到多个 APDU 里边。

### 两种设备的兼容

这里体现了 DL/T 2811 协议对两种客户端的兼容性：

1. **小内存设备**：\`apduSize ≤ asduSize\`，代表不支持分帧。因为 PDU 包含的很多 SDU 是要一起作为一个整体处理的，小内存设备不能处理很大的 SDU。他们走的是另一套结构——对于数组的返回值，返回一小部分，然后设置 \`moreFollow=true\`，客户端进行第二次请求并携带 \`referenceAfter\`。
2. **大内存设备**：\`apduSize > asduSize\`，代表支持分帧。因为内存很大，可以处理很大块的数据，但是网络并不一定支持这么大的数据，所以要分帧，多次发送，接收端拼起来统一处理。

### 其他

- **时序：TCP → Negotiate → Associate**。negotiate 必须在 associate 之前完成，否则关联会失败。
- **自动模式**：\`connect --ap\` 三步一体，最常用。只有需要自定义参数时才需手动拆开。
- **协商优先级**：手动传参 > 配置文件 > 默认值（65535/65531/1）。`,
  params: [
    { ...P_APDU, default: 65535 },
    { ...P_ASDU, default: 65531 },
    { ...P_VERSION, default: 1, readonly: true },
  ],
}

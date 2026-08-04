import { asn1Associate } from './asn1.js'
import { P_AP } from './common.js'

export default {
  title: '关联 associate (8.2.1)',
  desc: '手动建立应用层关联（connect 已自动包含）',
  asn1: asn1Associate,
  doc: `## 协议原文

### 服务参数

关联服务用于客户与服务器之间进行连接认证，服务的参数见表 19。

**表 19 关联服务参数**

| 服务/参数 | 数据类型 |
|-----------|----------|
| **Request** | |
| \`serverAccessPointReference\` [0..1] | \`VisibleString129\` |
| \`authenticationParameter\` [0..1] | \`OCTETSTRING\` |
| **Response+** | |
| \`associationId\` | \`OCTETSTRING64\` |
| \`result\` | \`ServiceError\` = no-error |
| \`authenticationParameter\` | \`OCTETSTRING\` |
| **Response-** | |
| \`serviceError\` | \`ServiceError\` |

\`serverAccessPointReference\` 为访问点的引用，格式为：

\`\`\`
IEDName.AccessPoint
\`\`\`

\`authenticationParameter\` 表示应用层关联过程的安全认证。\`associationId\` 表示应用层关联的标识，具体格式由服务器定义。\`signatureCertificate\` 表示签名证书，是 \`OCTET STRING\` 类型数据。\`signedTime\` 表示签名时间。\`signedValue\` 表示签名值。

### 关联的访问点

建立应用层关联时，客户通过 \`serverAccessPointReference\` 指定所关联的访问点，服务器后续的所有服务均针对此访问点下的模型。未指定 \`serverAccessPointReference\` 时，服务器应使用缺省的访问点，或根据客户地址选择一个访问点。

通常一个访问点对应于一个子网，不同子网的地址不同。但服务器在一个子网上需要同时为多个客户服务，且不同客户的访问模型不同时，通过指定访问点来区分所使用的模型。

### 服务要求

1. 安全认证参数 \`authenticationParameter\` 是可选参数。需要安全通信时 \`authenticationParameter\` 中应携带数字证书相关信息。
2. 关联建立过程中，关联请求者应将自己的签名证书赋值到 \`signatureCertificate\` 中发送。关联响应者确认了关联请求者的身份合法后，应将自己的签名证书内容赋到 \`signatureCertificate\` 中回传给关联请求者。至少应支持 8192 字节的证书传输。
3. 签名时间 \`signedTime\` 是 \`authenticationParameter\` 生成的 UTC 时间，用 \`UtcTime\` 类型表示，时间精度应小于 1s。
4. 签名值 \`signedValue\` 由发起方计算（客户和服务器都可以为发起方），由关联接收方对签名值进行验证，计算时只对 \`time\` 自身数据进行签名，不包含编码附加的标签、长度等额外信息。

## 设计要点

### 安全的两重体现

DL/T 2811 的安全分两层实现，各司其职：

1. **应用层**：关联（Associate）时的证书机制。通过 \`authenticationParameter\` 互传签名证书、验证签名值来确认双方身份，防止伪造身份接入。
2. **传输层**：TCP 之上的加密机制（TLS）。保证通信数据在传输过程中的机密性与完整性，防止窃听和篡改。

应用层解决"你是谁"（身份认证），传输层解决"路上安全"（加密传输）。

### SCD 目录层级科普

SCD（变电站配置描述）文件描述的是分层模型，自上而下：

| 层级 | 名称 | 说明 |
|------|------|------|
| 1 | IED | 装置，如 \`C_B5041X\`、\`P_B5041A\` |
| 2 | AccessPoint | 访问点，装置的通信入口，一个 IED 可有多个 AP |
| 3 | LDevice（LD） | 逻辑设备，按功能划分（如 \`LD0\`、\`PROT\`、\`MEAS\`） |
| 4 | LN | 逻辑节点，最小功能单元（如 \`LLN0\`、\`PTOC1\`） |
| 5 | DO | 数据对象（如 \`Mod\`、\`StrVal\`） |
| 6 | DA | 数据属性，字段级（如 \`stVal\`、\`q\`、\`t\`） |

连接建立只涉及前两级：**IED → AccessPoint**，\`associate --ap IED/AP\` 正是落在这个层级。关联成功后，后续所有服务都在该 AP 下的模型树中展开，数据引用格式为 \`LD/LN.DO.DA\`（再往下还有子属性/字段）。`,
  params: [P_AP, { key: 'secure', label: '应用层安全认证 secure', type: 'switch' }],
}

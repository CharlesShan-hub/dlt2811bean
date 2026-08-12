import { asn1Abort } from '../asn1.js'

const abortReasons = [
  { value: 0, label: 'other', cn: '其他' },
  { value: 1, label: 'unrecognized-service', cn: '无法识别的服务' },
  { value: 2, label: 'invalid-reqID', cn: '无效请求ID' },
  { value: 3, label: 'invalid-argument', cn: '无效参数' },
  { value: 4, label: 'invalid-result', cn: '无效结果' },
  { value: 5, label: 'max-serv-outstanding-exceeded', cn: '超出最大未完成服务数' },
]

export default {
  title: '中止 abort (8.2.3)',
  desc: '中止当前关联，服务器直接关闭（无需响应）',
  asn1: asn1Abort,
  doc: `## 协议原文

### 服务参数

异常中止服务用于强行断开已关联的服务，服务的参数见表 21。

**表 21 异常中止服务参数**

| 服务/参数 | 数据类型 |
|-----------|----------|
| **Request** | |
| \`associationId\` | \`OCTETSTRING\` |
| \`reason\` | \`CODEDENUM\` |
| **Indication** | |
| \`associationId\` | \`OCTETSTRING\` |
| \`reason\` | \`CODEDENUM\` |

\`reason\` 的取值范围（8.2.3 异常中止原因）：

| reason | 值 | 含义 |
|--------|-----|------|
| \`other\` | 0 | 其他 |
| \`unrecognized-service\` | 1 | 无法识别的服务 |
| \`invalid-reqID\` | 2 | 无效请求ID |
| \`invalid-argument\` | 3 | 无效参数 |
| \`invalid-result\` | 4 | 无效结果 |
| \`max-serv-outstanding-exceeded\` | 5 | 超出最大未完成服务数 |

## 设计要点

abort 与 release 一"负"一"正"，都是结束关联，但语义完全不同：

| 维度 | abort（异常中止） | release（正常释放） |
|------|--------------------|------------------|
| 适用场景 | 错误、异常时强行断开 | 切换 AP 前、主动断开连接 |
| 交互方式 | 单向通知，只管发送和原因，无需回复 | 请求-响应，等待服务器回复确认 |
| 服务器行为 | 收到即关闭，立即生效 | 正常关闭关联，TCP 连接保留 |

1. **abort 用于异常兜底**：更像一条错误通知——携带 \`reason\` 原因发出即算完事，不等回复、不协商，服务器收到即关闭，适合错误、超时等异常场景的强断。
2. **release 用于正常收尾**：切换访问点（AP）前先释放旧关联，主动断开连接也走 release。走完整请求-响应流程，服务器回复确认，双方状态干净，释放的是应用层关联，TCP 连接保留。`,
  params: [
    { key: 'reason', label: '中止原因 reason', type: 'select', options: abortReasons.map((r) => `${r.value}: ${r.cn}（${r.label}）`) },
  ],
}

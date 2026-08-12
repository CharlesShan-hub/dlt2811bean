import { asn1Release } from '../asn1.js'

export default {
  title: '释放 release (8.2.2)',
  desc: '释放当前应用层关联，保持 TCP 连接',
  asn1: asn1Release,
  doc: `## 协议原文

### 服务参数

释放关联服务用于关闭已建立的关联，服务的参数见表 20。

**表 20 释放关联服务参数**

| 服务/参数 | 数据类型 |
|-----------|----------|
| **Request** | |
| \`associationId\` | \`OCTETSTRING\` |
| **Response+** | |
| \`associationId\` | \`OCTETSTRING\` |
| \`result\` | \`ServiceError\` = no-error |
| **Response-** | |
| \`serviceError\` | \`ServiceError\` |

## 设计要点

release 与 abort 一"正"一"负"，都是结束关联，但语义完全不同：

| 维度 | release（正常释放） | abort（异常中止） |
|------|--------------------|------------------|
| 适用场景 | 切换 AP 前、主动断开连接 | 错误、异常时强行断开 |
| 交互方式 | 请求-响应，等待服务器回复确认 | 单向通知，只管发送和原因，无需回复 |
| 服务器行为 | 正常关闭关联，TCP 连接保留 | 收到即关闭，立即生效 |

1. **release 用于正常收尾**：切换访问点（AP）前先释放旧关联，主动断开连接也走 release。走完整请求-响应流程，服务器回复确认，双方状态干净，释放的是应用层关联，TCP 连接保留。
2. **abort 用于异常兜底**：更像一条错误通知——携带 \`reason\` 原因发出即算完事，不等回复、不协商，服务器收到即关闭，适合错误、超时等异常场景的强断。`,
  params: [],
}

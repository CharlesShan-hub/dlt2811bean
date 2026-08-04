export default {
  title: '逻辑设备目录 ld-dir (8.3.2)',
  desc: '获取指定逻辑设备下的逻辑节点目录（GetLogicalDeviceDirectory）',
  asn1: `GetLogicalDeviceDirectory-RequestPDU ::= SEQUENCE {
    ldName            [0] IMPLICIT ObjectName OPTIONAL,
    referenceAfter    [1] IMPLICIT ObjectReference OPTIONAL
}

GetLogicalDeviceDirectory-ResponsePDU ::= SEQUENCE {
    lnReference       [0] IMPLICIT SEQUENCE OF SubReference,
    moreFollows       [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetLogicalDeviceDirectory-ErrorPDU ::= ServiceError — 8.3.2`,
  doc: `## 协议原文

### 服务参数

读逻辑设备目录服务用于获取指定逻辑设备的逻辑节点，服务的参数见表 24。其中 \`referenceAfter\` 用于请求指定 \`reference\` 之后的信息。

**表 24 读逻辑设备目录服务参数**

| 服务/参数 | 数据类型 |
|-----------|----------|
| **Request** | |
| \`ldName\` [0..1] | \`ObjectName\` |
| \`referenceAfter\` [0..1] | \`ObjectReference\` |
| **Response+** | |
| \`lnReference\` [0..n] | \`SubReference\` |
| \`moreFollows\` [0..1] | \`BOOLEAN\` |
| **Response-** | |
| \`serviceError\` | \`ServiceError\` |

### 服务要求

1. 请求时指定了 \`ldName\` 的情况下，响应的 \`lnReference\` 应为逻辑节点的名称。未指定 \`ldName\` 的情况下，应读取所有逻辑设备的逻辑节点，响应的 \`lnReference\` 应为逻辑节点的引用。
2. \`referenceAfter\` 用于连续多次请求时，宜设为上一次响应的最后一个 \`lnReference\`。
3. \`referenceAfter\` 用于单次请求时，应直接从指定的 \`referenceAfter\` 之后返回结果。
4. \`lnReference\` 是 \`SubReference\` 类型，应补齐 \`reference\` 的内容。

## 设计要点

### 两种请求模式

不指定 \`ldName\` 时是全量模式：返回服务器所有逻辑设备的逻辑节点，\`lnReference\` 是带 \`LD/\` 前缀的完整引用；指定 \`ldName\` 时是单设备模式：只返回该 LD 下的逻辑节点，\`lnReference\` 只是节点名（不带前缀）。\`referenceAfter\` 自动适配这两种结构——全量模式续读值带前缀，单设备模式不带。

### 全量拉取的经典分页

不指定 LD 的全量请求可能返回上百个 LN，一条响应装不下：\`moreFollows\` 置真表示"还有"，客户端带 \`referenceAfter\`（上一次返回的最后一个 \`lnReference\`）循环续拉直到拉完——这是游标分页的经典示例。`,
  params: [
    { key: 'ld', label: '逻辑设备 ld', type: 'ld-select', placeholder: '选择逻辑设备（可选，省略则返回所有 LD 的完整引用）' },
    { key: 'after', label: '起始引用 after', type: 'ln-cascade', placeholder: 'LD → LN 逐级选择（上面选 ld 后自动跟随，只选 LN）' },
  ],
}

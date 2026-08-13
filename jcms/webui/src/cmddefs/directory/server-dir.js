export default {
  title: '服务器目录 server-dir (8.3.1)',
  desc: '获取服务器全部逻辑设备目录（GetServerDirectory）',
  asn1: `GetServerDirectory-RequestPDU ::= SEQUENCE {
    objectClass      [0] IMPLICIT INTEGER {
        reserved        (0),
        logical-device  (1),
        file-system     (2)
    } (0..2),
    referenceAfter   [1] IMPLICIT ObjectReference OPTIONAL
}

GetServerDirectory-ResponsePDU ::= SEQUENCE {
    reference        [0] IMPLICIT SEQUENCE OF ObjectReference,
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetServerDirectory-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读服务器目录服务用于获取所有逻辑设备名称，服务的参数见表 22。

**表 22 读服务器目录服务参数**

| 服务/参数 | 数据类型 |
|-----------|----------|
| **Request** | |
| \`objectClass\` | \`ENUMERATED\` |
| \`referenceAfter\` [0..1] | \`ObjectReference\` |
| **Response+** | |
| \`reference\` [0..n] | \`ObjectReference\` |
| \`moreFollows\` [0..1] | \`BOOLEAN\` |
| **Response-** | |
| \`serviceError\` | \`ServiceError\` |

\`objectClass\` 的取值范围见表 23。

**表 23 objectClass 值**

| objectClass | 值 | 含义 |
|-------------|-----|------|
| \`reserved\` | 0 | 保留 |
| \`logical-device\` | 1 | 逻辑设备 |

### 服务要求

1. \`referenceAfter\` 是不正确的引用名时，应返回 Response-。
2. \`referenceAfter\` 正确但返回的 \`reference\` 数量为 0 时，应返回 Response+。
3. \`objectClass\` 应始终为 \`logical-device\`。

> 注：文件目录已改由新的 GetFileDirectory 服务（8.12.5）承担，file-system 类型不再使用。因此 \`objectClass\` 永远是 \`logical-device\`（值 1），而不是 \`reserved\`（值 0）。

## 设计要点

### 客户端缓存的"地基"

server-dir 是连接后自动巡检的第一步：关联建立后依次拉取 server-dir → ld-dir → ln-dir → all-def，把整棵模型树（LD → LN → DO/DA）缓存到本地，之后选下拉、校验引用都直接查缓存，不必再问服务器。它只读、幂等、带游标分页，天然适合做缓存。`,
  params: [
    // 协议固定为 logical-device，仅展示不可修改
    { key: 'objectClass', label: '对象类型 objectClass', type: 'select', options: ['1: 逻辑设备（logical-device）', '0: 保留（reserved）'], disabled: true },
    { key: 'after', label: '起始引用 after', type: 'ld-select', placeholder: '选择起始 LD（可选，不选则从头开始）' },
    { key: 'auto-pull', label: '自动续拉 auto-pull', type: 'auto-pull-switch' },
  ],
}

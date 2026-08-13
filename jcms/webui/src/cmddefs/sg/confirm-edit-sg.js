export default {
  title: '确认编辑定值组值 confirm-edit-sg (8.6.4)',
  desc: '将编辑缓冲区的定值提交到定值组',
  asn1: `ConfirmEditSGValues-RequestPDU ::= SEQUENCE {
    sgcbReference       [0] IMPLICIT ObjectReference
}

ConfirmEditSGValues-ResponsePDU ::= NULL

ConfirmEditSGValues-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

确认编辑定值组值服务用于确认编辑定值组的设置值生效，服务的参数见表 43。

**表 43 确认编辑定值组值服务参数**

| 服务/参数 | 数据类型 |
|-----------|----------|
| **Request** | |
| sgcbReference | ObjectReference |
| **Response+** | |
| **Response-** | |
| serviceError | ServiceError |
`,
  params: [
    { key: 'ln', label: '逻辑节点', type: 'ln-cascade', required: true },
    { key: 'sgcb', label: '定值组控制块 sgcb', type: 'sgcb-select', required: true, dependsOn: 'ln' },
  ],
}
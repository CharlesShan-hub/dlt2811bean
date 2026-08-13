export default {
  title: '选择激活定值组 select-active-sg (8.6.1)',
  desc: '选择一个定值组作为当前激活组',
  asn1: `SelectActiveSG-RequestPDU ::= SEQUENCE {
    sgcbReference       [0] IMPLICIT ObjectReference,
    settingGroupNumber  [1] IMPLICIT INT8U
}

SelectActiveSG-ResponsePDU ::= NULL

SelectActiveSG-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

选择激活定值组服务用于选择待启用的定值组，服务的参数见表 40。

**表 40 选择激活定值组服务参数**

| 服务/参数 | 数据类型 |
|-----------|----------|
| **Request** | |
| sgcbReference | ObjectReference |
| settingGroupNumber | INT8U |
| **Response+** | |
| **Response-** | |
| serviceError | ServiceError |
`,
  params: [
    { key: 'ln', label: '逻辑节点', type: 'ln-cascade', required: true },
    { key: 'sgcb', label: '定值组控制块 sgcb', type: 'sgcb-select', required: true, dependsOn: 'ln' },
    { key: 'num', label: '定值组号 num', type: 'number', required: true, min: 1, placeholder: '1~numOfSG' },
  ],
}
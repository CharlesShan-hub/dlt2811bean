export default {
  title: '选择编辑定值组 select-edit-sg (8.6.2)',
  desc: '选择一个定值组作为当前编辑组',
  asn1: `SelectEditSG-RequestPDU ::= SEQUENCE {
    sgcbReference       [0] IMPLICIT ObjectReference,
    settingGroupNumber  [1] IMPLICIT INT8U
}

SelectEditSG-ResponsePDU ::= NULL

SelectEditSG-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

选择编辑定值组服务用于选择待编辑的定值组，服务的参数见表 41。

**表 41 选择编辑定值组服务参数**

| 服务/参数 | 数据类型 |
|-----------|----------|
| **Request** | |
| sgcbReference | ObjectReference |
| settingGroupNumber | INT8U |
| **Response+** | |
| **Response-** | |
| serviceError | ServiceError |
`,
}
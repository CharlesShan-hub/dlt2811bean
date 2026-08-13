export default {
  title: '设置编辑定值组值 set-edit-sg (8.6.3)',
  desc: '设置编辑缓冲区中的定值',
  asn1: `SetEditSGValue-RequestPDU ::= SEQUENCE {
    data                [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference         [0] IMPLICIT ObjectReference,
        value             [1] IMPLICIT Data
    }
}

SetEditSGValue-ResponsePDU ::= NULL

SetEditSGValue-ErrorPDU ::= SEQUENCE {
    result              [0] IMPLICIT SEQUENCE OF ServiceError
}`,
  doc: `## 协议原文

### 服务参数

设置编辑定值组值用于修改一组定值数据，服务的参数见表 42。

**表 42 设置编辑定值组值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| data [1..n] | | |
| reference | data | ObjectReference |
| value | data | Data |
| **Response+** | | |
| **Response-** | | |
| result [1..n] | | ServiceError |

### 服务要求

设置编辑定值组值的功能约束自动识别为 SE。所有编辑定值组值设置成功时返回 Response+，部分或全部失败时返回 Response-。在 Response-中，依次返回每个编辑定值组值的设置结果。
`,
}
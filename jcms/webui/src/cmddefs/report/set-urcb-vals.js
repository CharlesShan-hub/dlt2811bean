export default {
  title: '设置非缓存报告控制块值 set-urcb-vals (8.7.5)',
  desc: '修改非缓存报告控制块的属性',
  asn1: `SetURCBValues-RequestPDU ::= SEQUENCE {
    urcb             [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        rptID         [1] IMPLICIT VisibleString129 OPTIONAL,
        rptEna        [2] IMPLICIT BOOLEAN OPTIONAL,
        resv          [3] IMPLICIT BOOLEAN OPTIONAL,
        datSet        [4] IMPLICIT ObjectReference OPTIONAL,
        optFlds       [5] IMPLICIT RCBOptFlds OPTIONAL,
        bufTm         [6] IMPLICIT INT32U OPTIONAL,
        trgOps        [7] IMPLICIT TriggerConditions OPTIONAL,
        intgPd        [8] IMPLICIT INT32U OPTIONAL,
        gi            [9] IMPLICIT BOOLEAN OPTIONAL
    }
}

SetURCBValues-ResponsePDU ::= NULL

SetURCBValues-ErrorPDU ::= SEQUENCE {
    result           [0] IMPLICIT SEQUENCE OF ServiceError
}`,
  doc: `## 协议原文

### 服务参数

设置非缓存报告控制块值服务用于修改非缓存报告控制块内的一个或多个属性，服务的参数见表 50。

**表 50 设置非缓存报告控制块值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| urcb [1..n] | | |
| reference | urcb | ObjectReference |
| rptID [0..1] | urcb | VisibleString129 |
| rptEna [0..1] | urcb | BOOLEAN |
| resv [0..1] | urcb | BOOLEAN |
| datSet [0..1] | urcb | ObjectReference |
| optFlds [0..1] | urcb | RCBOptFlds |
| bufTm [0..1] | urcb | INT32U |
| trgOps [0..1] | urcb | TriggerConditions |
| intgPd [0..1] | urcb | INT32U |
| gi [0..1] | urcb | BOOLEAN |
| **Response+** | | |
| **Response-** | | |
| result [1..n] | | ServiceError |

### 服务要求

设置非缓存报告控制块值的服务要求如下。

a) 除 rptEna 外，其他属性之间没有顺序要求，某一个属性设置失败应不影响其他属性设置。

b) 设置序列中含有 rptEna 且其值为 False 时，应先设置 rptEna 为 False 再设置其他属性。rptEna 值为 True 时，应先设置其他属性再设置 rptEna。属性设置未全部成功的情况下，应不继续设置 rptEna 值为 True。

c) 设置序列为空时，应返回 Response+，不对非缓存报告控制块做任何修改。

d) 所有控制块均设置成功时返回 Response+，部分或全部失败时返回 Response-。在 Response-中，无论设置成功或失败，应返回每个控制块的设置结果。

e) 某控制块的所有属性均设置成功的情况下，该控制块 result 的内容应为空。某控制块的部分属性设置失败的情况下，该控制块 result 中应包含设置失败的属性，设置成功的属性不需列入。
`,
}
export default {
  title: '设置日志控制块值 set-lcb-vals (8.8.3)',
  desc: '修改日志控制块内的一个或多个属性',
  asn1: `SetLCBValues-RequestPDU ::= SEQUENCE {
    lcb              [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        logEna        [1] IMPLICIT BOOLEAN OPTIONAL,
        dataSet       [2] IMPLICIT ObjectReference OPTIONAL,
        optFlds       [3] IMPLICIT LCBOptFlds OPTIONAL,
        intgPd        [4] IMPLICIT INT32U OPTIONAL,
        logRef        [5] IMPLICIT ObjectReference OPTIONAL,
        trgOps        [6] IMPLICIT TriggerConditions OPTIONAL,
        bufTm         [7] IMPLICIT INT32U OPTIONAL
    }
}

SetLCBValues-ResponsePDU ::= NULL

SetLCBValues-ErrorPDU ::= SEQUENCE {
    result           [0] IMPLICIT SEQUENCE OF ServiceError
}`,
  doc: `## 协议原文

### 服务参数

设置日志控制块值服务用于修改日志控制块内的一个或多个属性，服务的参数见表53。

**表53 设置日志控制块值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| lcb[1..n] | | |
| reference | lcb | ObjectReference |
| logEna[0..1] | lcb | BOOLEAN |
| dataSet[0..1] | lcb | ObjectReference |
| optFlds[0..1] | lcb | LCBOptFlds |
| intgPd[0..1] | lcb | INT32U |
| logRef[0..1] | lcb | ObjectReference |
| trgOps[0..1] | lcb | TriggerConditions |
| bufTm[0..1] | lcb | INT32U |
| **Response+** | | |
| **Response-** | | |
| result[1..n] | | ServiceError |

### 服务要求

设置日志控制块值的服务要求如下。

a) 除logEna外，其他属性之间没有顺序要求，某一个属性设置失败应不影响其他属性设置。

b) 设置序列中含有logEna且其值为False时，应先设置logEna为False再设置其他属性。logEna值为True时，应先设置其他属性再设置logEna。属性设置未全部成功的情况下，应不继续设置logEna值为True。

c) 设置序列为空时，应返回Response+，不对日志控制块做任何修改。
`,
  params: [
    { key: 'ln', label: '逻辑节点', type: 'ln-cascade', required: true },
    { key: 'ref', label: 'LCB 引用 ref', type: 'cb-select', cb: 'lcb', required: true, dependsOn: 'ln' },
    { key: 'dat-set', label: '数据集 dat-set', type: 'text', required: false, placeholder: 'LD/LN.dsName', inline: 'cfg' },
    { key: 'log-ref', label: '日志引用 log-ref', type: 'text', required: false, placeholder: 'ObjectReference', inline: 'cfg' },
    { key: 'trg-ops', label: '触发条件 trg-ops', type: 'number', required: false, placeholder: '位掩码 1/2/4/8/16', inline: 'mask' },
    { key: 'opt-flds', label: '选项字段 opt-flds', type: 'number', required: false, placeholder: 'LCBOptFlds 位掩码', inline: 'mask' },
    { key: 'intg-pd', label: '完整性周期 intg-pd', type: 'number', required: false, placeholder: '毫秒', inline: 'time' },
    { key: 'buf-tm', label: '缓存时间 buf-tm', type: 'number', required: false, placeholder: '毫秒', inline: 'time' },
    { key: 'log-ena', label: '日志使能 log-ena', type: 'switch', required: false },
  ],
}
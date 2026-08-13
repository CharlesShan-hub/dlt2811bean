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
} — 8.8.3`,
}
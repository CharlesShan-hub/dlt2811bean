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
} — 8.7.5`,
}
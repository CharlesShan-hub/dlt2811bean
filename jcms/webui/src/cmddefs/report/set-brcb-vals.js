export default {
  title: '设置缓存报告控制块值 set-brcb-vals (8.7.3)',
  desc: '修改缓存报告控制块的属性',
  asn1: `SetBRCBValues-RequestPDU ::= SEQUENCE {
    brcb             [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        rptID         [1] IMPLICIT VisibleString129 OPTIONAL,
        rptEna        [2] IMPLICIT BOOLEAN OPTIONAL,
        datSet        [3] IMPLICIT ObjectReference OPTIONAL,
        optFlds       [4] IMPLICIT RCBOptFlds OPTIONAL,
        bufTm         [5] IMPLICIT INT32U OPTIONAL,
        trgOps        [6] IMPLICIT TriggerConditions OPTIONAL,
        intgPd        [7] IMPLICIT INT32U OPTIONAL,
        gi            [8] IMPLICIT BOOLEAN OPTIONAL,
        purgeBuf      [9] IMPLICIT BOOLEAN OPTIONAL,
        entryID       [10] IMPLICIT EntryID OPTIONAL,
        resvTms       [11] IMPLICIT INT16 OPTIONAL
    }
}

SetBRCBValues-ResponsePDU ::= NULL

SetBRCBValues-ErrorPDU ::= SEQUENCE {
    result           [0] IMPLICIT SEQUENCE OF ServiceError
} — 8.7.3`,
}
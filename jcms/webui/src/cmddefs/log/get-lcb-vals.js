export default {
  title: '读日志控制块值 get-lcb-vals (8.8.2)',
  desc: '获取日志控制块的所有属性',
  asn1: `GetLCBValues-RequestPDU ::= SEQUENCE {
    lcbReference     [0] IMPLICIT SEQUENCE OF ObjectReference
}

GetLCBValues-ResponsePDU ::= SEQUENCE {
    error            [0] IMPLICIT SEQUENCE OF CHOICE {
        error         [0] IMPLICIT ServiceError,
        lcb           [1] IMPLICIT LCB
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetLCBValues-ErrorPDU ::= ServiceError — 8.8.2`,
}
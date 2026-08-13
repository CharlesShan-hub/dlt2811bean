export default {
  title: '读缓存报告控制块值 get-brcb-vals (8.7.2)',
  desc: '读取缓存报告控制块的属性值',
  asn1: `GetBRCBValues-RequestPDU ::= SEQUENCE {
    brcbReference    [0] IMPLICIT SEQUENCE OF ObjectReference
}

GetBRCBValues-ResponsePDU ::= SEQUENCE {
    error            [0] IMPLICIT SEQUENCE OF CHOICE {
        error         [0] IMPLICIT ServiceError,
        brcb          [1] IMPLICIT BRCB
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetBRCBValues-ErrorPDU ::= ServiceError — 8.7.2`,
}
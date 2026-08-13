export default {
  title: '读非缓存报告控制块值 get-urcb-vals (8.7.4)',
  desc: '读取非缓存报告控制块的属性值',
  asn1: `GetURCBValues-RequestPDU ::= SEQUENCE {
    urcbReference    [0] IMPLICIT SEQUENCE OF ObjectReference
}

GetURCBValues-ResponsePDU ::= SEQUENCE {
    error            [0] IMPLICIT SEQUENCE OF CHOICE {
        error         [0] IMPLICIT ServiceError,
        urcb          [1] IMPLICIT URCB
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetURCBValues-ErrorPDU ::= ServiceError — 8.7.4`,
}
export default {
  title: '读GOOSE控制块值 get-gocb-vals (8.9.4)',
  desc: '读取GOOSE控制块属性值',
  asn1: `GetGoCBValues-RequestPDU ::= SEQUENCE {
    gocbReference    [0] IMPLICIT SEQUENCE OF ObjectReference
}

GetGoCBValues-ResponsePDU ::= SEQUENCE {
    errorGocb        [0] IMPLICIT SEQUENCE OF CHOICE {
        error         [0] IMPLICIT ServiceError,
        gocb          [1] IMPLICIT GoCB
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetGoCBValues-ErrorPDU ::= ServiceError — 8.9.4`,
}
export default {
  title: '读RPC方法定义 rpc-method-def (8.13.5)',
  desc: '获取RPC方法的详细定义',
  asn1: `GetRpcMethodDefinition-RequestPDU ::= SEQUENCE {
    reference        [0] IMPLICIT SEQUENCE OF VisibleString
}

GetRpcMethodDefinition-ResponsePDU ::= SEQUENCE {
    method           [0] IMPLICIT SEQUENCE OF SEQUENCE {
        timeout       [0] IMPLICIT INT32U,
        version       [1] IMPLICIT INT32U,
        request       [2] IMPLICIT DataDefinition,
        response      [3] IMPLICIT DataDefinition
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetRpcMethodDefinition-ErrorPDU ::= ServiceError — 8.13.5`,
}
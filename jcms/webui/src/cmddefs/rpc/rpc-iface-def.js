export default {
  title: '读RPC接口定义 rpc-iface-def (8.13.4)',
  desc: '获取RPC接口的详细定义',
  asn1: `GetRpcInterfaceDefinition-RequestPDU ::= SEQUENCE {
    interface        [0] IMPLICIT VisibleString,
    referenceAfter   [1] IMPLICIT VisibleString OPTIONAL
}

GetRpcInterfaceDefinition-ResponsePDU ::= SEQUENCE {
    method           [0] IMPLICIT SEQUENCE OF SEQUENCE {
        name          [0] IMPLICIT VisibleString,
        version       [1] IMPLICIT INT32U,
        timeout       [2] IMPLICIT INT32U,
        request       [3] IMPLICIT DataDefinition,
        response      [4] IMPLICIT DataDefinition
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetRpcInterfaceDefinition-ErrorPDU ::= ServiceError — 8.13.4`,
}
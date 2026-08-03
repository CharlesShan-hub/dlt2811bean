// 各服务 ASN.1 报文结构原文（DL/T 2811-2024），供命令定义展示用。

export const asn1Associate = `Associate-RequestPDU ::= SEQUENCE {
    serverAccessPointReference [0] VisibleString129 OPTIONAL,
    authenticationParameter    [1] OCTETSTRING        OPTIONAL
}

Associate-ResponsePDU ::= SEQUENCE {
    associationId           OCTETSTRING64,
    result                  ServiceError DEFAULT no-error,
    authenticationParameter OCTETSTRING  OPTIONAL
} — 8.2.1`

export const asn1Negotiate = `Negotiate-RequestPDU ::= SEQUENCE {
    apduSize         INTEGER (0..65535),
    asduSize         INTEGER (0..65531),
    protocolVersion  INTEGER
}

Negotiate-ResponsePDU ::= SEQUENCE {
    apduSize         INTEGER (0..65535),
    asduSize         INTEGER (0..65531),
    protocolVersion  INTEGER,
    modelVersion     VisibleString
}`

export const asn1Release = `Release-RequestPDU ::= SEQUENCE {
    associationId OCTETSTRING64
}

Release-ResponsePDU ::= SEQUENCE {
    associationId OCTETSTRING64,
    result        ServiceError DEFAULT no-error
} — 8.2.2`

export const asn1Abort = `Abort-RequestPDU ::= SEQUENCE {
    associationId OCTETSTRING64,
    reason        [1] IMPLICIT INTEGER {
        other                         (0),
        unrecognized-service          (1),
        invalid-reqID                 (2),
        invalid-argument              (3),
        invalid-result                (4),
        max-serv-outstanding-exceeded (5)
    } (0..5)
} — 8.2.3`

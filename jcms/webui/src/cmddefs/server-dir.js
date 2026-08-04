export default {
  title: '服务器目录 server-dir (8.3.1)',
  desc: '获取服务器全部逻辑设备目录（GetServerDirectory）',
  asn1: `GetServerDirectory-RequestPDU ::= SEQUENCE {
    objectClass      [0] IMPLICIT INTEGER {
        reserved        (0),
        logical-device  (1),
        file-system     (2)
    } (0..2),
    referenceAfter   [1] IMPLICIT ObjectReference OPTIONAL
}

GetServerDirectory-ResponsePDU ::= SEQUENCE {
    reference        [0] IMPLICIT SEQUENCE OF ObjectReference,
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetServerDirectory-ErrorPDU ::= ServiceError — 8.3.1`,
  params: [
    { key: 'after', label: '起始引用 after', type: 'ld-select', placeholder: '选择起始 LD（可选，不选则从头开始）' },
  ],
}

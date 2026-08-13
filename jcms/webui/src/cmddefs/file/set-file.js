export default {
  title: '设置文件 set-file (8.12.2)',
  desc: '向服务器写入文件内容',
  asn1: `SetFile-RequestPDU ::= SEQUENCE {
    fileName         [0] IMPLICIT VisibleString255,
    startPosition    [1] IMPLICIT INT32U,
    fileData         [2] IMPLICIT SEQUENCE OF OCTETSTRING,
    endOfFile        [3] IMPLICIT BOOLEAN OPTIONAL
}

SetFile-ResponsePDU ::= NULL

SetFile-ErrorPDU ::= ServiceError — 8.12.2`,
}
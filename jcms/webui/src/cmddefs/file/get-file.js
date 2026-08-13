export default {
  title: '读取文件 get-file (8.12.1)',
  desc: '从服务器读取文件内容',
  asn1: `GetFile-RequestPDU ::= SEQUENCE {
    fileName         [0] IMPLICIT VisibleString255,
    startPosition    [1] IMPLICIT INT32U
}

GetFile-ResponsePDU ::= SEQUENCE {
    fileData         [0] IMPLICIT SEQUENCE OF OCTETSTRING,
    endOfFile        [1] IMPLICIT BOOLEAN OPTIONAL
}

GetFile-ErrorPDU ::= ServiceError — 8.12.1`,
}
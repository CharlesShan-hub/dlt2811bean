export default {
  title: '删除文件 delete-file (8.12.3)',
  desc: '删除服务器上的文件',
  asn1: `DeleteFile-RequestPDU ::= SEQUENCE {
    fileName         [0] IMPLICIT VisibleString255
}

DeleteFile-ResponsePDU ::= NULL

DeleteFile-ErrorPDU ::= ServiceError — 8.12.3`,
}
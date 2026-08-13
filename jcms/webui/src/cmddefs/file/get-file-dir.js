export default {
  title: '读文件目录 get-file-dir (8.12.5)',
  desc: '获取服务器文件目录列表',
  asn1: `GetFileDirectory-RequestPDU ::= SEQUENCE {
    pathName         [0] IMPLICIT VisibleString255 OPTIONAL,
    startTime        [1] IMPLICIT TimeStamp OPTIONAL,
    stopTime         [2] IMPLICIT TimeStamp OPTIONAL,
    fileAfter        [3] IMPLICIT VisibleString255 OPTIONAL
}

GetFileDirectory-ResponsePDU ::= SEQUENCE {
    fileEntry        [0] IMPLICIT SEQUENCE OF SEQUENCE {
        FileName      [0] IMPLICIT VisibleString129,
        FileSize      [1] IMPLICIT INT32U,
        LastModified  [2] IMPLICIT UtcTime,
        CheckSum      [3] IMPLICIT INT32U
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetFileDirectory-ErrorPDU ::= ServiceError — 8.12.5`,
}
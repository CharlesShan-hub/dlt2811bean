export default {
  title: '读日志状态值 get-log-status (8.8.6)',
  desc: '获取日志的状态信息',
  asn1: `GetLogStatusValues-RequestPDU ::= SEQUENCE {
    logReference     [0] IMPLICIT SEQUENCE OF ObjectReference
}

GetLogStatusValues-ResponsePDU ::= SEQUENCE {
    log              [0] IMPLICIT SEQUENCE OF SEQUENCE {
        errorOrValue  [0] IMPLICIT CHOICE {
            error       [0] IMPLICIT ServiceError,
            value       [1] IMPLICIT SEQUENCE {
                oldEntrTm   [0] IMPLICIT EntryTime,
                newEntrTm   [1] IMPLICIT EntryTime,
                oldEntr     [2] IMPLICIT EntryID,
                newEntr     [3] IMPLICIT EntryID
            }
        }
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetLogStatusValues-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读日志状态值服务的参数见表56。

**表56 读日志状态值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| logReference[1..n] | | ObjectReference |
| **Response+** | | |
| log[1..n] | | |
| error/value | log | ServiceError/log |
| oldEntrTm | log | EntryTime |
| newEntrTm | log | EntryTime |
| oldEntr | log | EntryID |
| newEntr | log | EntryID |
| moreFollows[0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |
`,
}
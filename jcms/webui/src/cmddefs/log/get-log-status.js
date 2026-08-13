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

GetLogStatusValues-ErrorPDU ::= ServiceError — 8.8.6`,
}
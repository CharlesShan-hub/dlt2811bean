export default {
  title: '按时间查询日志 query-log-by-time (8.8.4)',
  desc: '按时间段查询日志条目',
  asn1: `QueryLogByTime-RequestPDU ::= SEQUENCE {
    LogReference     [0] IMPLICIT ObjectReference,
    startTime        [1] IMPLICIT EntryTime OPTIONAL,
    stopTime         [2] IMPLICIT EntryTime OPTIONAL,
    entryAfter       [3] IMPLICIT EntryID OPTIONAL
}

QueryLogByTime-ResponsePDU ::= SEQUENCE {
    logEntry         [0] IMPLICIT SEQUENCE OF LogEntry,
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

QueryLogByTime-ErrorPDU ::= ServiceError — 8.8.4`,
}
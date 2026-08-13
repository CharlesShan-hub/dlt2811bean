export default {
  title: '查询指定条目之后的日志 query-log-after (8.8.5)',
  desc: '从指定日志条目之后开始查询日志',
  asn1: `QueryLogAfter-RequestPDU ::= SEQUENCE {
    logReference     [0] IMPLICIT ObjectReference,
    startTime        [1] IMPLICIT EntryTime OPTIONAL,
    entry            [2] IMPLICIT EntryID
}

QueryLogAfter-ResponsePDU ::= SEQUENCE {
    logEntry         [0] IMPLICIT SEQUENCE OF LogEntry,
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

QueryLogAfter-ErrorPDU ::= ServiceError — 8.8.5`,
}
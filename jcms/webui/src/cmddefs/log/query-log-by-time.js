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

QueryLogByTime-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

按时间查询日志服务的参数见表54。

**表54 按时间查询日志服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| LogReference | | ObjectReference |
| startTime[0..1] | | EntryTime |
| stopTime[0..1] | | EntryTime |
| entryAfter[0..1] | | EntryID |
| **Response+** | | |
| logEntry[0..n] | | LogEntry |
| moreFollows[0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |

startTime表示查询服务的起始时间，stopTime表示查询服务的截止时间。未指定startTime时，应从整个日志记录的第一条开始查询；未指定stopTime时，应一直查询到整个日志记录的最后一条。

### 服务要求

查询得到的日志条目过多且无法在一次响应中返回时，服务器应设置moreFollows为TRUE，以通知客户未能返回全部查询结果。客户可以用时间和ID再一次发起查询请求。
`,
  params: [
    { key: 'ref', label: '日志控制块引用 ref', type: 'text', required: true, placeholder: 'LD/LN.lcbName，如 LD0/LLN0.lcblog' },
    { key: 'start', label: '起始时间 start', type: 'number', required: false, placeholder: '毫秒时间戳（可选）' },
    { key: 'stop', label: '截止时间 stop', type: 'number', required: false, placeholder: '毫秒时间戳（可选）' },
    { key: 'entry-after', label: '起始条目 ID entry-after', type: 'text', required: false, placeholder: 'EntryID（可选）' },
  ],
}

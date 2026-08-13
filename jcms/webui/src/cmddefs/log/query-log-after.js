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

QueryLogAfter-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

查询指定条目之后的日志服务的参数见表55。

**表55 查询指定条目之后的日志服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| logReference | | ObjectReference |
| startTime[0..1] | | EntryTime |
| entry | | EntryID |
| **Response+** | | |
| logEntry[0..n] | | LogEntry |
| moreFollows[0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |
`,
  params: [
    { key: 'ln', label: '逻辑节点', type: 'ln-cascade', required: true },
    { key: 'ref', label: '日志控制块引用 ref', type: 'cb-select', cb: 'lcb', required: true, dependsOn: 'ln' },
    { key: 'entry', label: '起始条目 ID entry', type: 'text', required: true, placeholder: 'EntryID，8 字节' },
    { key: 'start', label: '起始时间 start', type: 'number', required: false, placeholder: '毫秒时间戳（可选）' },
  ],
}
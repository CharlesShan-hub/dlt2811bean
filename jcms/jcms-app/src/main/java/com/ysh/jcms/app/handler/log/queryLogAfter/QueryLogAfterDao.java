package com.ysh.jcms.app.handler.log.queryLogAfter;

public class QueryLogAfterDao {
    private String logRef;
    private String entryId;
    private Long startTime;

    public QueryLogAfterDao logRef(String v) { this.logRef = v; return this; }
    public QueryLogAfterDao entryId(String v) { this.entryId = v; return this; }
    public QueryLogAfterDao startTime(Long v) { this.startTime = v; return this; }

    public String logRef() { return logRef; }
    public String entryId() { return entryId; }
    public Long startTime() { return startTime; }
}

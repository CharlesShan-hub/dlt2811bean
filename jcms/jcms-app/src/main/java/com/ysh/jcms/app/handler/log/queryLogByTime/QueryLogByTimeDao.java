package com.ysh.jcms.app.handler.log.queryLogByTime;

public class QueryLogByTimeDao {
    private String logRef;
    private Long startTime;
    private Long stopTime;

    public QueryLogByTimeDao logRef(String v) { this.logRef = v; return this; }
    public QueryLogByTimeDao startTime(Long v) { this.startTime = v; return this; }
    public QueryLogByTimeDao stopTime(Long v) { this.stopTime = v; return this; }

    public String logRef() { return logRef; }
    public Long startTime() { return startTime; }
    public Long stopTime() { return stopTime; }
}

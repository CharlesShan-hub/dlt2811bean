package com.ysh.jcms.app.handler.log.queryLogByTime;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class QueryLogByTimeConsole extends CommandHandler<QueryLogByTimeDao, QueryLogByTimeClient> {

    public QueryLogByTimeConsole() {
        super(CommandInfo.QUERY_LOG_BY_TIME, true);
        Param p1 = Param.of("ref", null, "logRef", String.class, true);
        param(p1, "日志控制块引用 (ObjectReference)");
        Param p2 = Param.of("start", null, "startTime", Long.class, false);
        param(p2, "起始时间 (毫秒时间戳)");
        Param p3 = Param.of("stop", null, "stopTime", Long.class, false);
        param(p3, "截止时间 (毫秒时间戳)");
        Param p4 = Param.of("entry-after", null, "entryAfter", String.class, false);
        param(p4, "起始条目 ID (EntryID)");
    }
}
package com.ysh.jcms.app.handler.log.queryLogAfter;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class QueryLogAfterConsole extends CommandHandler<QueryLogAfterDao, QueryLogAfterClient> {

    public QueryLogAfterConsole() {
        super(CommandInfo.QUERY_LOG_AFTER, true);
        Param p1 = Param.of("ref", null, "logRef", String.class, true);
        param(p1, "日志控制块引用 (ObjectReference)");
        Param p2 = Param.of("entry", null, "entryId", String.class, true);
        param(p2, "起始条目 ID (EntryID)");
        Param p3 = Param.of("start", null, "startTime", Long.class, false);
        param(p3, "起始时间 (毫秒时间戳)");
    }
}
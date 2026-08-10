package com.ysh.jcms.app.handler.log.queryLogByTime;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class QueryLogByTimeConsole extends CommandHandler {

    public QueryLogByTimeConsole() {
        super(CommandInfo.QUERY_LOG_BY_TIME);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "日志控制块引用 (ObjectReference)", null), new Param("start", "起始时间 (毫秒时间戳)", null),
                new Param("stop", "截止时间 (毫秒时间戳)", null));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        if (!CmsConsole.requireParam(args, "ref", "Usage: query-log-by-time --ref <logRef> [--start <ms>] [--stop <ms>]"))
            return;

        String ref = args.get("ref");
        String startStr = args.get("start");
        String stopStr = args.get("stop");

        QueryLogByTimeDao dao = new QueryLogByTimeDao().logRef(ref.trim());
        if (startStr != null && !startStr.isEmpty())
            dao.startTime(Long.parseLong(startStr));
        if (stopStr != null && !stopStr.isEmpty())
            dao.stopTime(Long.parseLong(stopStr));

        console.getClient(QueryLogByTimeClient.class).execute(dao);
        ConsolePrinter.success("QueryLogByTime completed");
    }
}

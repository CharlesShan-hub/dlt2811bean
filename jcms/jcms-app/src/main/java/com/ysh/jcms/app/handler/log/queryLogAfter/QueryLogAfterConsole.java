package com.ysh.jcms.app.handler.log.queryLogAfter;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class QueryLogAfterConsole extends CommandHandler {

    public QueryLogAfterConsole() {
        super(CommandInfo.QUERY_LOG_AFTER);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "日志控制块引用 (ObjectReference)", null), new Param("entry", "起始条目 ID (EntryID)", null),
                new Param("start", "起始时间 (毫秒时间戳, OPTIONAL)", null));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        if (!CmsConsole.requireParam(args, "ref", "Usage: query-log-after --ref <logRef> --entry <entryId> [--start <ms>]"))
            return;
        if (!CmsConsole.requireParam(args, "entry", "Usage: query-log-after --ref <logRef> --entry <entryId> [--start <ms>]"))
            return;

        String ref = args.get("ref");
        String entryId = args.get("entry");
        String startStr = args.get("start");

        QueryLogAfterDao dao = new QueryLogAfterDao().logRef(ref.trim()).entryId(entryId.trim());
        if (startStr != null && !startStr.isEmpty())
            dao.startTime(Long.parseLong(startStr));

        console.getClient(QueryLogAfterClient.class).execute(dao);
        ConsolePrinter.success("QueryLogAfter completed");
    }
}

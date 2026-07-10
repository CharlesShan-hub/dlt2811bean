package com.ysh.jcms.app.handler.log.queryLogAfter;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class QueryLogAfterConsole implements CommandHandler {

    @Override
    public String name() {
        return "query-log-after";
    }

    @Override
    public String description() {
        return "查询指定条目之后的日志 (QueryLogAfter) [--json]。\n" + "  用法: query-log-after --ref <logRef> --entry <entryId> [--start <ms>]\n"
                + "  案例:\n" + "    query-log-after --ref LD0/LLN0.lcb1 --entry \"000001\"\n"
                + "    query-log-after --ref LD0/LLN0.lcb1 --entry \"000001\" --start 1700000000000";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "日志控制块引用 (ObjectReference)", null), new Param("entry", "起始条目 ID (EntryID)", null),
                new Param("start", "起始时间 (毫秒时间戳, OPTIONAL)", null), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
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

        if (!CmsConsole.isJsonMode(args)) {
            ConsolePrinter.info("Querying log after entry: ref=" + ref + " entry=" + entryId);
        }
        console.getClient(QueryLogAfterClient.class).execute(dao);
        CmsConsole.outputMessage("QueryLogAfter completed", args);
    }
}

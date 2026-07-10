package com.ysh.jcms.app.handler.log.queryLogByTime;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class QueryLogByTimeConsole implements CommandHandler {

    @Override
    public String name() {
        return "query-log-by-time";
    }

    @Override
    public String description() {
        return "按时间查询日志 (QueryLogByTime) [--json]。\n" + "  用法: query-log-by-time --ref <logRef> [--start <ms>] [--stop <ms>]\n" + "  案例:\n"
                + "    query-log-by-time --ref LD0/LLN0.lcb1\n"
                + "    query-log-by-time --ref LD0/LLN0.lcb1 --start 1700000000000 --stop 1700000100000";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "日志控制块引用 (ObjectReference)", null), new Param("start", "起始时间 (毫秒时间戳)", null),
                new Param("stop", "截止时间 (毫秒时间戳)", null), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = "true".equals(args.get("json"));
        if (!console.isConnected()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Not connected. Type 'connect' first.\"}");
            } else {
                ConsolePrinter.error("Not connected. Type 'connect' first.");
            }
            return;
        }

        String ref = args.get("ref");
        if (ref == null || ref.trim().isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Missing --ref.\"}");
            } else {
                ConsolePrinter.error("Missing --ref. Usage: query-log-by-time --ref <logRef> [--start <ms>] [--stop <ms>]");
            }
            return;
        }

        String startStr = args.get("start");
        String stopStr = args.get("stop");

        QueryLogByTimeDao dao = new QueryLogByTimeDao().logRef(ref.trim());
        if (startStr != null && !startStr.isEmpty())
            dao.startTime(Long.parseLong(startStr));
        if (stopStr != null && !stopStr.isEmpty())
            dao.stopTime(Long.parseLong(stopStr));

        if (!jsonMode) {
            ConsolePrinter.info("Querying log by time: ref=" + ref);
        }
        console.getClient(QueryLogByTimeClient.class).execute(dao);
        if (jsonMode) {
            ConsolePrinter.raw("{\"success\":true,\"message\":\"QueryLogByTime completed\"}");
        } else {
            ConsolePrinter.success("QueryLogByTime completed");
        }
    }
}

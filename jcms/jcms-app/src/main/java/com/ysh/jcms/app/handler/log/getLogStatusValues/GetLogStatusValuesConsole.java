package com.ysh.jcms.app.handler.log.getLogStatusValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetLogStatusValuesConsole implements CommandHandler {

    @Override
    public String name() {
        return "get-log-status";
    }

    @Override
    public String description() {
        return "获取日志状态值 (GetLogStatusValues)。用法: get-log-status --refs \"<ref1> <ref2>...\" [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("refs", "LCB 引用列表（空格分隔），如 \"LD0/LLN0.lcb1\"", null), new Param("json", "JSON 格式输出", ""));
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

        String refsStr = args.get("refs");
        if (refsStr == null || refsStr.trim().isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Missing --refs.\"}");
            } else {
                ConsolePrinter.error("Missing --refs. Usage: get-log-status --refs \"<ref1> <ref2>...\"");
            }
            return;
        }

        String[] refs = refsStr.trim().split("\\s+");
        GetLogStatusValuesDao dao = new GetLogStatusValuesDao();
        for (String ref : refs) {
            if (!ref.isEmpty())
                dao.addRef(ref.trim());
        }

        if (!jsonMode) {
            ConsolePrinter.info("Fetching log status for " + dao.refs().size() + " reference(s)");
        }

        console.getClient(GetLogStatusValuesClient.class).execute(dao);

        List<GetLogStatusValuesClient.LogStatusEntry> entries = console.getClient(GetLogStatusValuesClient.class).getLastEntries();

        if (entries.isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":true,\"data\":[]}");
            } else {
                ConsolePrinter.info("No log status values returned");
            }
            return;
        }

        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
            for (int i = 0; i < entries.size(); i++) {
                if (i > 0)
                    sb.append(',');
                String ref = i < refs.length ? refs[i] : "#" + i;
                sb.append("{\"ref\":\"").append(CmsFormatUtil.escapeJson(ref)).append("\",\"desc\":\"")
                        .append(CmsFormatUtil.escapeJson(entries.get(i).desc)).append("\"}");
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            for (int i = 0; i < entries.size(); i++) {
                String ref = i < refs.length ? refs[i] : "#" + i;
                ConsolePrinter.info("  [" + ref + "] " + entries.get(i).desc);
            }
        }
    }
}

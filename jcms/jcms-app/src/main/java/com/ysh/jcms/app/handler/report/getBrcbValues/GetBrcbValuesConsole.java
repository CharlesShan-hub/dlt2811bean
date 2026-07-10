package com.ysh.jcms.app.handler.report.getBrcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetBrcbValuesConsole implements CommandHandler {

    @Override
    public String name() {
        return "get-brcb-vals";
    }

    @Override
    public String description() {
        return "获取缓存报告控制块值 (GetBRCBValues)。用法: get-brcb-vals --refs \"<ref1> <ref2>...\" [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("refs", "BRCB 引用列表（空格分隔），如 \"LD0/LLN0.brcbAlarm\"", null), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = "true".equals(args.get("json"));
        if (!console.isConnected()) {
            String msg = "Not connected. Type 'connect' first.";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }

        String refsStr = args.get("refs");
        if (refsStr == null || refsStr.trim().isEmpty()) {
            String msg = "Missing --refs. Usage: get-brcb-vals --refs \"<ref1> <ref2>...\"";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }

        String[] refs = refsStr.trim().split("\\s+");
        GetBrcbValuesDao dao = new GetBrcbValuesDao();
        for (String ref : refs) {
            if (!ref.isEmpty())
                dao.addRef(ref.trim());
        }

        if (!jsonMode) {
            ConsolePrinter.info("Fetching BRCB values for " + dao.refs().size() + " reference(s)");
        }

        console.getClient(GetBrcbValuesClient.class).execute(dao);

        List<GetBrcbValuesClient.BrcbEntry> entries = console.getClient(GetBrcbValuesClient.class).getLastEntries();

        if (entries.isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":true,\"data\":[]}");
            } else {
                ConsolePrinter.info("No BRCB values returned");
            }
            return;
        }

        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
            for (int i = 0; i < entries.size(); i++) {
                String ref = i < refs.length ? refs[i] : "#" + i;
                if (i > 0)
                    sb.append(',');
                sb.append("{\"ref\":\"").append(CmsFormatUtil.escapeJson(ref)).append("\"");
                sb.append(",\"desc\":\"").append(CmsFormatUtil.escapeJson(entries.get(i).desc)).append("\"");
                sb.append("}");
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            List<Object> display = new java.util.ArrayList<>();
            for (int i = 0; i < entries.size(); i++) {
                String ref = i < refs.length ? refs[i] : "#" + i;
                display.add(ref + "  " + entries.get(i).desc);
            }
            ConsolePrinter.list("BRCB values (" + entries.size() + " items)", display, Object::toString);
        }
    }
}

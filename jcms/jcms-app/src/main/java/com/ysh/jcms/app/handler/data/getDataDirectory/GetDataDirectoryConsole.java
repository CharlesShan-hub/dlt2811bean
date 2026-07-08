package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetDataDirectoryConsole implements CommandHandler {

    @Override
    public String name() { return "data-dir"; }

    @Override
    public String description() { return "获取数据目录 (GetDataDirectory)。用法: data-dir --ref <ref> [--after REF] [--json]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ref", "数据引用，如 LD0/LLN0 或 LD0/LLN0.Mod", null),
            new Param("after", "起始引用（分页截取）", ""),
            new Param("json", "JSON 格式输出", "")
        );
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
                ConsolePrinter.error("Missing --ref. Usage: data-dir --ref <ref> [--after REF]");
            }
            return;
        }

        GetDataDirectoryDao dao = new GetDataDirectoryDao()
            .dataReference(ref.trim());

        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }

        if (!jsonMode) {
            ConsolePrinter.info("Fetching data directory for " + ref);
        }

        console.getClient(GetDataDirectoryClient.class).execute(dao);

        List<GetDataDirectoryClient.DirEntry> entries =
            console.getClient(GetDataDirectoryClient.class).getLastEntries();

        if (entries.isEmpty()) {
            ConsolePrinter.info("No data directory entries");
            return;
        }

        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
            for (int i = 0; i < entries.size(); i++) {
                if (i > 0) sb.append(',');
                GetDataDirectoryClient.DirEntry e = entries.get(i);
                String val = (e.fc != null ? "[" + e.fc + "]  " : "") + e.reference;
                sb.append('"').append(CmsFormatUtil.escapeJson(val)).append('"');
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            ConsolePrinter.list("Data directory (" + entries.size() + " items)",
                new java.util.ArrayList<>(entries),
                e -> {
                    if (e.fc != null) return "[" + e.fc + "]  " + e.reference;
                    return e.reference;
                });
        }
    }
}

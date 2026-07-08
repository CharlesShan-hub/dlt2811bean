package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetDataSetDirectoryConsole implements CommandHandler {

    @Override
    public String name() { return "get-dataset-dir"; }

    @Override
    public String description() { return "获取数据集目录 (GetDataSetDirectory)。用法: get-dataset-dir --ds <ref> [--after REF] [--json]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ds", "数据集引用，如 \"LD0/LLN0.dsAlarm\"", null),
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

        String dsRef = args.get("ds");
        if (dsRef == null || dsRef.trim().isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Missing --ds.\"}");
            } else {
                ConsolePrinter.error("Missing --ds. Usage: get-dataset-dir --ds <ref> [--after REF]");
            }
            return;
        }

        GetDataSetDirectoryDao dao = new GetDataSetDirectoryDao()
            .datasetReference(dsRef.trim());

        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }

        if (!jsonMode) {
            ConsolePrinter.info("Fetching dataset directory for " + dsRef);
        }

        console.getClient(GetDataSetDirectoryClient.class).execute(dao);

        List<GetDataSetDirectoryClient.DirEntry> entries =
            console.getClient(GetDataSetDirectoryClient.class).getLastEntries();

        if (entries.isEmpty()) {
            ConsolePrinter.info("No dataset directory entries");
            return;
        }

        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
            for (int i = 0; i < entries.size(); i++) {
                if (i > 0) sb.append(',');
                GetDataSetDirectoryClient.DirEntry e = entries.get(i);
                String val = "[" + e.fc + "]  " + e.reference;
                sb.append('"').append(CmsFormatUtil.escapeJson(val)).append('"');
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            ConsolePrinter.list("DataSet directory (" + entries.size() + " items)",
                new java.util.ArrayList<>(entries),
                e -> "[" + e.fc + "]  " + e.reference);
        }
    }
}

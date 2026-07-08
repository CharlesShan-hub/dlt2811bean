package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LdDirConsole implements CommandHandler {

    @Override
    public String name() { return "ld-dir"; }

    @Override
    public String description() { return "获取逻辑节点目录 (GetLogicalDeviceDirectory)。用法: ld-dir [--ld LD] [--after REF] [--json]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ld", "逻辑设备名（默认 C1）", "C1"),
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
        LdDirDao dao = new LdDirDao()
            .ldName(args.get("ld"));
        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }
        console.getClient(LdDirClient.class).execute(dao);
        List<String> items = new ArrayList<>(console.getContentManager().getLnNames());
        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
            for (int i = 0; i < items.size(); i++) {
                if (i > 0) sb.append(',');
                sb.append('"').append(CmsFormatUtil.escapeJson(items.get(i))).append('"');
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            ConsolePrinter.list("Logical Nodes", items, s -> s);
        }
    }
}

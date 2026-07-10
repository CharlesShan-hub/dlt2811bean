package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SvrDirConsole implements CommandHandler {

    @Override
    public String name() {
        return "server-dir";
    }

    @Override
    public String description() {
        return "获取逻辑设备目录 (GetServerDirectory)。用法: server-dir [--after REF] [--json]";
    }

    @Override
    public List<Param> params() {
        return java.util.Arrays.asList(new Param("after", "起始引用（分页截取，不传则从头开始）", ""), new Param("json", "JSON 格式输出（不传则输出人类可读文本）", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = "true".equals(args.get("json"));
        String msg = check(console);
        if (msg != null) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }
        SvrDirDao dao = new SvrDirDao();
        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }
        console.getClient(SvrDirClient.class).execute(dao);
        List<String> ldNames = new ArrayList<>(console.getContentManager().getLdNames());
        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
            for (int i = 0; i < ldNames.size(); i++) {
                if (i > 0)
                    sb.append(',');
                sb.append('"').append(CmsFormatUtil.escapeJson(ldNames.get(i))).append('"');
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            ConsolePrinter.list("Logical Devices", ldNames, s -> s);
        }
    }

    static String check(CmsConsole console) {
        if (!console.isConnected())
            return "Not connected. Type 'connect' first.";
        if (console.getClient(SvrDirClient.class) == null)
            return "SvrDirClient not registered.";
        return null;
    }
}

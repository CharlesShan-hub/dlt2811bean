package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AbortConsole implements CommandHandler {

    @Override
    public String name() {
        return "abort";
    }

    @Override
    public String description() {
        return "异常中止关联 (Abort) [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("reason", "中止原因码", "0"), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = "true".equals(args.get("json"));
        if (!console.isConnected()) {
            String msg = "Not connected.";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }
        int reason = Integer.parseInt(args.get("reason"));
        console.getClient(AbortClient.class).execute(new AbortClientDao().reason(reason));
        String msg = "Abort sent (reason=" + reason + ")";
        if (jsonMode) {
            ConsolePrinter.raw("{\"success\":true,\"message\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
        } else {
            ConsolePrinter.success(msg);
        }
    }
}

package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DisconnectHandler implements CommandHandler {

    @Override
    public String name() { return "disconnect"; }
    @Override
    public String description() { return "断开当前连接 [--json]"; }
    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("json", "JSON 格式输出", "")
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) {
        boolean jsonMode = "true".equals(args.get("json"));
        if (!console.isConnected()) { ConsolePrinter.info("Not connected."); return; }
        console.close();
        String msg = "Disconnected.";
        if (jsonMode) {
            ConsolePrinter.raw("{\"success\":true,\"message\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
        } else {
            ConsolePrinter.success(msg);
        }
    }
}

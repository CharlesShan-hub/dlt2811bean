package com.ysh.jcms.app.handler.console;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;
import com.ysh.jcms.utils.config.CmsConfigLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Handler for the `trace-pdu` command.
 *
 * Toggles PDU tracing on/off (print raw bytes for every sent/received frame).
 * Works on both server and client consoles.
 */
public class TracePduHandler implements CommandHandler {

    @Override
    public String name() { return "trace-pdu"; }

    @Override
    public String description() { return "开启/关闭 PDU 跟踪。用法: trace-pdu [--value true/false] [--json]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("value", "true 或 false", ""),
            new Param("json", "JSON 格式输出", "")
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = "true".equals(args.get("json"));
        String value = args.get("value");
        if (value == null || value.isEmpty()) {
            boolean current = CmsConfigLoader.load().getClient().getConsole().isTracePdu();
            ConsolePrinter.info("trace-pdu = " + current);
            return;
        }

        boolean v = Boolean.parseBoolean(value);
        CmsConfigLoader.load().getClient().getConsole().setTracePdu(v);
        ConsolePrinter.info("trace-pdu = " + v);
    }
}

package com.ysh.jcms.app.handler.console;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import java.util.Map;

/**
 * Handler for the `trace-pdu` command.
 *
 * Toggles PDU tracing on/off (print raw bytes for every sent/received frame).
 * Works on both server and client consoles.
 */
public class TracePduHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public TracePduHandler() {
        super(CommandInfo.TRACE_PDU);
        param("value", "true 或 false", "");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        String value = args.get("value");
        if (value == null || value.isEmpty()) {
            boolean current = CmsConfigLoader.load().client().console().tracePdu();
            ConsolePrinter.info("trace-pdu = " + current);
            return;
        }

        boolean v = Boolean.parseBoolean(value);
        CmsConfigLoader.load().client().console().tracePdu(v);
        ConsolePrinter.info("trace-pdu = " + v);
    }
}

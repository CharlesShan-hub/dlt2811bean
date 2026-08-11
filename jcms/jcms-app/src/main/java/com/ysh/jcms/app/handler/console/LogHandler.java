package com.ysh.jcms.app.handler.console;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import java.util.Map;

/**
 * Handler for the {@code log} command.
 *
 * <p>
 * Controls logging and tracing settings (e.g. PDU tracing). Supports future
 * flags like {@code --trace} for extensibility.
 *
 * <p>Usage:
 * <ul>
 *   <li>{@code log} — show current settings</li>
 *   <li>{@code log --trace} — toggle PDU tracing</li>
 *   <li>{@code log --trace false} — disable PDU tracing</li>
 * </ul>
 */
public class LogHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public LogHandler() {
        super(CommandInfo.LOG);
        Param p = Param.of("trace", null, null, String.class, false);
        param(p, "开启/关闭 PDU 跟踪（--trace 切换，--trace false 关闭，--trace true 切换）");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        String traceVal = args.get("trace");

        if (traceVal == null || traceVal.isEmpty()) {
            // No --trace → show current status
            showStatus();
            return;
        }

        boolean current = CmsConfigLoader.load().client().console().tracePdu();

        if ("false".equalsIgnoreCase(traceVal) || "off".equalsIgnoreCase(traceVal)) {
            // --trace false → disable
            CmsConfigLoader.load().client().console().tracePdu(false);
            ConsolePrinter.info("trace-pdu = false");
        } else {
            // --trace, --trace true, --trace on, etc. → toggle
            boolean newVal = !current;
            CmsConfigLoader.load().client().console().tracePdu(newVal);
            ConsolePrinter.info("trace-pdu = " + newVal);
        }
    }

    private void showStatus() {
        boolean current = CmsConfigLoader.load().client().console().tracePdu();
        ConsolePrinter.info("trace-pdu = " + current);
    }
}
package com.ysh.jcms.app.handler.console;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import java.util.Map;

/**
 * Handler for the {@code log} command.
 *
 * <p>
 * Controls logging and tracing settings (e.g. PDU tracing, session tracing).
 * Supports future flags for extensibility.
 *
 * <p>
 * Usage:
 * <ul>
 * <li>{@code log} — show current settings</li>
 * <li>{@code log --trace} — toggle PDU tracing</li>
 * <li>{@code log --trace false} — disable PDU tracing</li>
 * <li>{@code log --session} — toggle session tracing</li>
 * <li>{@code log --session false} — disable session tracing</li>
 * </ul>
 */
public class LogHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public LogHandler() {
        super(CommandInfo.LOG);
        Param p1 = Param.of("trace", null, null, String.class, false);
        param(p1, "开启/关闭 PDU 跟踪（--trace 切换，--trace false 关闭）");
        Param p2 = Param.of("session", null, null, String.class, false);
        param(p2, "开启/关闭会话追踪（--session 切换，--session false 关闭）");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        String traceVal = args.get("trace");
        String sessionVal = args.get("session");

        // 有参数 → 处理设置
        boolean handled = false;

        if (traceVal != null && !traceVal.isEmpty()) {
            handled = true;
            toggleTrace(traceVal);
        }

        if (sessionVal != null && !sessionVal.isEmpty()) {
            handled = true;
            toggleSession(sessionVal);
        }

        if (!handled) {
            showStatus();
        }
    }

    private void toggleTrace(String val) {
        CmsConfig.Client.Console cfg = CmsConfigLoader.load().client().console();
        boolean current = cfg.tracePdu();

        if ("false".equalsIgnoreCase(val) || "off".equalsIgnoreCase(val)) {
            cfg.tracePdu(false);
            CmsPrinter.info("trace-pdu = false");
        } else {
            boolean newVal = !current;
            cfg.tracePdu(newVal);
            CmsPrinter.info("trace-pdu = " + newVal);
        }
    }

    private void toggleSession(String val) {
        CmsConfig.Client.Console cfg = CmsConfigLoader.load().client().console();
        boolean current = cfg.sessionTrace();

        if ("false".equalsIgnoreCase(val) || "off".equalsIgnoreCase(val)) {
            cfg.sessionTrace(false);
            CmsPrinter.info("session-trace = false");
        } else {
            boolean newVal = !current;
            cfg.sessionTrace(newVal);
            CmsPrinter.info("session-trace = " + newVal);
        }
    }

    private void showStatus() {
        CmsConfig.Client.Console cfg = CmsConfigLoader.load().client().console();
        CmsPrinter.info("trace-pdu = " + cfg.tracePdu());
        CmsPrinter.info("session-trace = " + cfg.sessionTrace());
    }
}

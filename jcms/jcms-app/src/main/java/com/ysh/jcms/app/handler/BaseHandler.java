package com.ysh.jcms.app.handler;

import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Root base class for all service handlers (client and server).
 *
 * <p>
 * Provides the shared {@link Logger} instance and the static {@link #trace}
 * helper for PDU tracing.
 */
public abstract class BaseHandler {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Trace a PDU string. Output is controlled by the {@code trace-pdu} config
     * flag. When enabled, prints directly to the real console (bypassing the HTTP
     * API capture stream so that trace output is never included in API responses).
     */
    protected static void trace(String msg) {
        if (CmsConfigLoader.load().client().console().tracePdu()) {
            ConsolePrinter.consoleOnly(msg);
        }
    }

    /**
     * Trace a session lifecycle event. Output is controlled by the
     * {@code session-trace} config flag. When enabled, prints a gray
     * {@code [SESSION]} line to the real console (bypassing the HTTP API capture
     * stream).
     */
    public static void traceSession(String msg) {
        if (CmsConfigLoader.load().client().console().sessionTrace()) {
            ConsolePrinter.consoleOnly("[SESSION] " + msg);
        }
    }
}

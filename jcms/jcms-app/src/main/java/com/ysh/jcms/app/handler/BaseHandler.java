package com.ysh.jcms.app.handler;

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

    /** Trace a PDU string (logged at INFO level). Subclasses may override. */
    protected static void trace(String msg) {
        // using a static logger to avoid per-instance overhead
        LoggerFactory.getLogger(BaseHandler.class).info(msg);
    }
}

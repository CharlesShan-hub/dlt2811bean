package com.ysh.jcms.app.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Root base class for all service handlers (client and server).
 *
 * <p>
 * Provides the shared {@link Logger} instance.
 */
public abstract class BaseHandler {

    protected final Logger log = LoggerFactory.getLogger(getClass());
}

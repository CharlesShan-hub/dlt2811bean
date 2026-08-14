package com.ysh.jcms.app.handler.base;

/**
 * Base class for CLI-based server service handlers.
 *
 * <p>
 * CLI server handlers are invoked from the command-line interface for manual
 * testing. They typically read user input, invoke a {@link BaseServerHandler},
 * and display the result.
 */
public abstract class BaseServerCliHandler extends BaseHandler {
}
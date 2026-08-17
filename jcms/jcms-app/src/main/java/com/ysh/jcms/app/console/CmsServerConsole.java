package com.ysh.jcms.app.console;

import com.ysh.jcms.app.handler.console.server.ListHandler;
import com.ysh.jcms.app.handler.console.server.MaxEntriesHandler;
import com.ysh.jcms.app.handler.console.LogHandler;
import com.ysh.jcms.app.handler.console.ClearHandler;
import com.ysh.jcms.app.node.CmsServer;

/**
 * Server-side CMS console.  Only defines which handlers to register;
 * all other behaviour is inherited from {@link CmsConsole} default methods.
 */
public class CmsServerConsole extends CmsServer implements CmsConsole {

    @Override
    public void registerHandlers() {
        register(new ClearHandler());
        register(new ListHandler());
        register(new LogHandler());
        register(new MaxEntriesHandler());
    }

    public static void main(String[] args) {
        new CmsServerConsole().run();
    }
}
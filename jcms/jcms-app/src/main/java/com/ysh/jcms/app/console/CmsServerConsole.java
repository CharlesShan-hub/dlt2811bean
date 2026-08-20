package com.ysh.jcms.app.console;

import com.ysh.jcms.app.handler.console.maxentries.MaxEntriesHandler;
import com.ysh.jcms.app.handler.console.log.LogHandler;
import com.ysh.jcms.app.handler.console.clear.ClearHandler;
import com.ysh.jcms.app.handler.console.ap.ApServerHandler;
import com.ysh.jcms.app.node.CmsServer;

/**
 * Server-side CMS console.  Only defines which handlers to register;
 * all other behaviour is inherited from {@link CmsConsole} default methods.
 */
public class CmsServerConsole extends CmsServer implements CmsConsole {

    @Override
    public void registerHandlers() {
        register(new ClearHandler());
        register(new LogHandler());
        register(new MaxEntriesHandler());
        register(new ApServerHandler());
    }

    public static void main(String[] args) {
        new CmsServerConsole().run();
    }
}
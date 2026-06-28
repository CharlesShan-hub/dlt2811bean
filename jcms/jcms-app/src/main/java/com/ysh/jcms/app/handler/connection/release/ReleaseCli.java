package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.console.ConsoleContext;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ReleaseCli implements CommandHandler {

    @Override
    public String name() { return "release"; }

    @Override
    public String description() { return "释放关联 (Release)"; }

    @Override
    public List<Param> params() {
        return Collections.emptyList();
    }

    @Override
    public void execute(ConsoleContext ctx, Map<String, String> args) throws Exception {
        if (!ctx.isConnected()) { ConsolePrinter.error("Not connected."); return; }
        ctx.node().getClient(com.ysh.jcms.app.handler.connection.release.ReleaseClient.class).execute();
        ConsolePrinter.success("Released.");
    }
}

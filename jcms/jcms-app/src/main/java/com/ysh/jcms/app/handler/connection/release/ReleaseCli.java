package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.cli.CliContext;
import com.ysh.jcms.app.cli.CliPrinter;
import com.ysh.jcms.app.cli.CommandHandler;
import com.ysh.jcms.app.cli.Param;

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
    public void execute(CliContext ctx, Map<String, String> args) throws Exception {
        if (!ctx.isConnected()) { CliPrinter.error("Not connected."); return; }
        ctx.node().getClient(com.ysh.jcms.app.handler.connection.release.ReleaseClient.class).execute();
        CliPrinter.success("Released.");
    }
}

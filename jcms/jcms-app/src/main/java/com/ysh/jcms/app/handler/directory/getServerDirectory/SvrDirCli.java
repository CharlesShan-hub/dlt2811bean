package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.cli.CliContext;
import com.ysh.jcms.app.cli.CliPrinter;
import com.ysh.jcms.app.cli.CommandHandler;
import com.ysh.jcms.app.cli.Param;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SvrDirCli implements CommandHandler {

    @Override
    public String name() { return "server-dir"; }

    @Override
    public String description() { return "获取逻辑设备目录 (GetServerDirectory)"; }

    @Override
    public List<Param> params() {
        return Collections.emptyList();
    }

    @Override
    public void execute(CliContext ctx, Map<String, String> args) throws Exception {
        String msg = check(ctx);
        if (msg != null) { CliPrinter.error(msg); return; }
        ctx.node().getClient(SvrDirClient.class)
            .execute(new SvrDirDao());
        CliPrinter.list("Logical Devices",
            new ArrayList<>(ctx.node().getContentManager().getLdNames()),
            s -> s);
    }

    static String check(CliContext ctx) {
        if (!ctx.isConnected()) return "Not connected. Type 'connect' first.";
        if (ctx.node().getClient(SvrDirClient.class) == null) return "SvrDirClient not registered.";
        return null;
    }
}

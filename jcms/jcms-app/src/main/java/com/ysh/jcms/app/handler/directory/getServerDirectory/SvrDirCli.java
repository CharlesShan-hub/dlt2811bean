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
        return Collections.singletonList(
            new Param("referenceAfter", "起始引用（分页，不传则从头开始）", null)
        );
    }

    @Override
    public void execute(CliContext ctx, Map<String, String> args) throws Exception {
        String msg = check(ctx);
        if (msg != null) { CliPrinter.error(msg); return; }
        SvrDirDao dao = new SvrDirDao();
        String after = args.get("referenceAfter");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }
        ctx.node().getClient(SvrDirClient.class)
            .execute(dao);
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

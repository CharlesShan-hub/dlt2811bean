package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.cli.CliContext;
import com.ysh.jcms.app.cli.CliPrinter;
import com.ysh.jcms.app.cli.CommandHandler;
import com.ysh.jcms.app.cli.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LdDirCli implements CommandHandler {

    @Override
    public String name() { return "ld-dir"; }

    @Override
    public String description() { return "获取逻辑节点目录 (GetLogicalDeviceDirectory)"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ldName", "逻辑设备名", "C1"),
            new Param("referenceAfter", "起始引用", null)
        );
    }

    @Override
    public void execute(CliContext ctx, Map<String, String> args) throws Exception {
        if (!ctx.isConnected()) { CliPrinter.error("Not connected. Type 'connect' first."); return; }
        LdDirDao dao = new LdDirDao()
            .ldName(args.get("ldName"));
        String after = args.get("referenceAfter");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }
        ctx.node().getClient(LdDirClient.class).execute(dao);
        CliPrinter.list("Logical Nodes",
            new ArrayList<>(ctx.node().getContentManager().getLnNames()),
            s -> s);
    }
}

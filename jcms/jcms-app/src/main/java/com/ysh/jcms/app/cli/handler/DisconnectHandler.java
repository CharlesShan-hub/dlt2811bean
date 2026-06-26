package com.ysh.jcms.app.cli.handler;

import com.ysh.jcms.app.cli.CliContext;
import com.ysh.jcms.app.cli.CliPrinter;
import com.ysh.jcms.app.cli.CommandHandler;
import com.ysh.jcms.app.cli.Param;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DisconnectHandler implements CommandHandler {

    @Override
    public String name() { return "disconnect"; }

    @Override
    public String description() { return "断开当前连接"; }

    @Override
    public List<Param> params() {
        return Collections.emptyList();
    }

    @Override
    public void execute(CliContext ctx, Map<String, String> args) {
        if (!ctx.isConnected()) {
            CliPrinter.info("Not connected.");
            return;
        }
        ctx.node().close();
        ctx.node(null);
        CliPrinter.success("Disconnected.");
    }
}

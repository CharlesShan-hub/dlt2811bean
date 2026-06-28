package com.ysh.jcms.app.handler.console;

import com.ysh.jcms.app.console.ConsoleContext;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DisconnectHandler implements CommandHandler {

    @Override
    public String name() { return "disconnect"; }
    @Override
    public String description() { return "断开当前连接"; }
    @Override
    public List<Param> params() { return Collections.emptyList(); }

    @Override
    public void execute(ConsoleContext ctx, Map<String, String> args) {
        if (!ctx.isConnected()) { ConsolePrinter.info("Not connected."); return; }
        ctx.node().close();
        ctx.node(null);
        ConsolePrinter.success("Disconnected.");
    }
}

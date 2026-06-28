package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.console.ConsoleContext;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestCli implements CommandHandler {

    @Override
    public String name() { return "test"; }

    @Override
    public String description() { return "测试连接 (Test ping/pong)"; }

    @Override
    public List<Param> params() {
        return Collections.emptyList();
    }

    @Override
    public void execute(ConsoleContext ctx, Map<String, String> args) throws Exception {
        if (!ctx.isConnected()) { ConsolePrinter.error("Not connected."); return; }
        ctx.node().getClient(TestClient.class).execute();
        ConsolePrinter.success("Ping/pong OK");
    }
}

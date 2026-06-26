package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.cli.CliContext;
import com.ysh.jcms.app.cli.CliPrinter;
import com.ysh.jcms.app.cli.CommandHandler;
import com.ysh.jcms.app.cli.Param;

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
    public void execute(CliContext ctx, Map<String, String> args) throws Exception {
        if (!ctx.isConnected()) { CliPrinter.error("Not connected."); return; }
        ctx.node().getClient(TestClient.class).execute();
        CliPrinter.success("Ping/pong OK");
    }
}

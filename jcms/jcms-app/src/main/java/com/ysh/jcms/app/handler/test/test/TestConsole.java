package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestConsole implements CommandHandler {

    @Override
    public String name() { return "test"; }

    @Override
    public String description() { return "测试连接 (Test ping/pong)"; }

    @Override
    public List<Param> params() {
        return Collections.emptyList();
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) { ConsolePrinter.error("Not connected."); return; }
        console.getClient(TestClient.class).execute();
        ConsolePrinter.success("Ping/pong OK");
    }
}

package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestConsole implements CommandHandler {

    @Override
    public String name() {
        return "test";
    }

    @Override
    public String description() {
        return "测试连接 (Test ping/pong) [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        console.getClient(TestClient.class).execute();
        CmsConsole.outputMessage("Ping/pong OK", args);
    }
}

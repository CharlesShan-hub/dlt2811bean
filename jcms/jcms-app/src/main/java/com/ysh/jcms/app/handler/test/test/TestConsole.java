package com.ysh.jcms.app.handler.test.test;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestConsole extends CommandHandler {

    public TestConsole() {
        super(CommandInfo.TEST);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;
        console.getClient(TestClient.class).execute(new TestDao());
        ConsolePrinter.success("Ping/pong OK");
    }
}

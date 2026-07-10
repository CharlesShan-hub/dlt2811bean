package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AbortConsole implements CommandHandler {

    @Override
    public String name() {
        return "abort";
    }

    @Override
    public String description() {
        return "异常中止关联 (Abort) [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("reason", "中止原因码", "0"), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        int reason = Integer.parseInt(args.get("reason"));
        console.getClient(AbortClient.class).execute(new AbortClientDao().reason(reason));
        CmsConsole.outputMessage("Abort sent (reason=" + reason + ")", args);
    }
}

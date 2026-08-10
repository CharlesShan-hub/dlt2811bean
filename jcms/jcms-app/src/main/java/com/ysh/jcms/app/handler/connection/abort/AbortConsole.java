package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.data.enumerate.CmsAbortReason;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AbortConsole extends CommandHandler {

    public AbortConsole() {
        super(CommandInfo.ABORT);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("reason", "中止原因码", "0"), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireTcpConnected(args))
            return;
        int reason = Integer.parseInt(args.get("reason"));
        console.getClient(AbortClient.class).execute(new AbortClientDao().reason(reason));
        ConsolePrinter.success("Abort sent: " + new CmsAbortReason(reason).value() + " (" + reason + ")");
    }
}

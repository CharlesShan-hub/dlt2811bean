package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.console.ConsoleContext;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AbortCli implements CommandHandler {

    @Override
    public String name() { return "abort"; }

    @Override
    public String description() { return "异常中止关联 (Abort)"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("reason", "中止原因码", "1")
        );
    }

    @Override
    public void execute(ConsoleContext ctx, Map<String, String> args) throws Exception {
        if (!ctx.isConnected()) { ConsolePrinter.error("Not connected."); return; }
        int reason = Integer.parseInt(args.get("reason"));
        ctx.node().getClient(AbortClient.class)
            .execute(new AbortClientDao().reason(reason));
        ConsolePrinter.success("Abort sent (reason=" + reason + ")");
    }
}

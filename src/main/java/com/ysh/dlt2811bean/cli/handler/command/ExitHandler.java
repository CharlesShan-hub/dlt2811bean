package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.common.AbstractSystemHandler;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class ExitHandler extends AbstractSystemHandler {

    public ExitHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "exit"; }
    public String getDescription() { return "退出程序"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) {
        ctx.getAutoTestHeartbeat().stop();
        if (client.isConnected()) {
            try { client.release(); } catch (Exception ignored) {}
            client.close();
        }
        ctx.getCachedHierarchy().clear();
        System.out.println("Bye!");
        Runtime.getRuntime().halt(0);
    }
}

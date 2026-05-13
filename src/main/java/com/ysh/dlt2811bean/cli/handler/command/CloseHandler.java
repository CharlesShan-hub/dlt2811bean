package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class CloseHandler implements CommandHandler {

    private final CliContext ctx;

    public CloseHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "close"; }
    public String getDescription() { return "断开连接"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) {
        ctx.getAutoTestHeartbeat().stop();
        client.close();
        System.out.println(CmsColor.green("  Disconnected"));
    }
}

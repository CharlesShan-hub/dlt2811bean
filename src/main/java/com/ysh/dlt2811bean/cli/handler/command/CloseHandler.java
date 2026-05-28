package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.cli.handler.common.AbstractSystemHandler;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class CloseHandler extends AbstractSystemHandler {

    public CloseHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "close"; }
    public String getDescription() { return "断开连接"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) {
        ctx.getAutoTestHeartbeat().stop();
        client.close();
        System.out.println(CmsColor.green("  Disconnected"));
    }
}

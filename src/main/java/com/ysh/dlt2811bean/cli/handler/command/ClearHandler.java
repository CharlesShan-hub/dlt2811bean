package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.common.AbstractSystemHandler;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class ClearHandler extends AbstractSystemHandler {

    public ClearHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "clear"; }
    public String getDescription() { return "清空控制台"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

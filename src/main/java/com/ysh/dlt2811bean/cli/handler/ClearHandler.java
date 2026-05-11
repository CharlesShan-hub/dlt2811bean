package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class ClearHandler implements CommandHandler {

    private final CliContext ctx;

    public ClearHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "clear"; }
    public String getDescription() { return "清空控制台"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

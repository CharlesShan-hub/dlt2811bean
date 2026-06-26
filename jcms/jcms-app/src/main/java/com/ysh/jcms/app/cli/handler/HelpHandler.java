package com.ysh.jcms.app.cli.handler;

import com.ysh.jcms.app.cli.CliContext;
import com.ysh.jcms.app.cli.CliPrinter;
import com.ysh.jcms.app.cli.CmsCli;
import com.ysh.jcms.app.cli.CommandHandler;
import com.ysh.jcms.app.cli.Param;

import java.util.*;

public class HelpHandler implements CommandHandler {

    private final CmsCli cli;

    public HelpHandler(CmsCli cli) {
        this.cli = cli;
    }

    @Override
    public String name() { return "help"; }

    @Override
    public String description() { return "显示帮助信息"; }

    @Override
    public List<Param> params() {
        return Collections.emptyList();
    }

    @Override
    public void execute(CliContext ctx, Map<String, String> args) {
        System.out.println();
        CliPrinter.info("可用命令:");
        for (CommandHandler h : cli.handlers().values()) {
            if (h.name().equals("help")) continue;
            StringBuilder sb = new StringBuilder();
            sb.append("  ").append(String.format("%-16s", h.name()));
            sb.append(h.description());
            List<Param> ps = h.params();
            if (!ps.isEmpty()) {
                sb.append("  [");
                for (int i = 0; i < ps.size(); i++) {
                    if (i > 0) sb.append(" ");
                    sb.append(ps.get(i).name());
                }
                sb.append("]");
            }
            System.out.println(sb.toString());
        }
        System.out.println();
    }
}

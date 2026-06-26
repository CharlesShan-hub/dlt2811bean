package com.ysh.jcms.app.cli.handler;

import com.ysh.jcms.app.cli.CliContext;
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
        System.out.format("  %-16s  %s\n", "命令", "说明");
        System.out.format("  %-16s  %s\n", "---------", "----");
        for (CommandHandler h : cli.handlers().values()) {
            if (h.name().equals("help")) continue;
            StringBuilder line = new StringBuilder();
            line.append(String.format("  %-16s  ", h.name()));
            line.append(h.description());
            List<Param> ps = h.params();
            if (!ps.isEmpty()) {
                line.append("  [");
                for (int i = 0; i < ps.size(); i++) {
                    if (i > 0) line.append(" ");
                    line.append(ps.get(i).name());
                    String def = ps.get(i).defaultValue();
                    if (def != null) line.append("=").append(def);
                }
                line.append("]");
            }
            System.out.println(line.toString());
        }
        System.out.println();
    }
}

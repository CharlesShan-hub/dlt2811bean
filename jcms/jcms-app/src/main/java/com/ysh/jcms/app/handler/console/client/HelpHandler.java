package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.app.console.*;
import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.BaseDao;
import java.util.*;

public class HelpHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    private final CmsConsole console;

    public HelpHandler(CmsConsole console) {
        super(CommandInfo.HELP);
        this.console = console;
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) {
        CmsPrinter.gray("");
        CmsPrinter.gray(String.format("  %-16s  %s", "命令", "说明"));
        CmsPrinter.gray(String.format("  %-16s  %s", "---------", "----"));
        for (CommandHandler h : console.handlers().values()) {
            if (h.name().equals("help"))
                continue;
            StringBuilder line = new StringBuilder();
            line.append(String.format("  %-16s  ", h.name()));
            line.append(h.description());
            List<Param> ps = h.params();
            if (!ps.isEmpty()) {
                line.append("  [");
                for (int i = 0; i < ps.size(); i++) {
                    if (i > 0)
                        line.append(" ");
                    line.append(ps.get(i).cliName());
                    String def = ps.get(i).defaultValue();
                    if (def != null)
                        line.append("=").append(def);
                }
                line.append("]");
            }
            CmsPrinter.gray(line.toString());
        }
        CmsPrinter.gray("");
    }
}

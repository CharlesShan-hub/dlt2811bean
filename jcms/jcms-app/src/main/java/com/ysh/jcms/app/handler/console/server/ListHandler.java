package com.ysh.jcms.app.handler.console.server;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.utils.scl.model.ied.SclIED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ListHandler extends CommandHandler {

    public ListHandler() {
        super(CommandInfo.LIST_AP);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("limit", "数量（不传则列出全部）", ""), new Param("offset", "起始索引", "0"));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.getSclManager().isLoaded()) {
            ConsolePrinter.error("SCL not loaded.");
            return;
        }

        List<String> aps = new ArrayList<>();
        for (SclIED ied : console.getSclManager().getIeds()) {
            ied.accessPoints().forEach(ap -> aps.add(ied.name() + "/" + ap.name()));
        }

        if (aps.isEmpty()) {
            ConsolePrinter.gray("No access points found.");
            return;
        }

        String limitStr = args.get("limit");
        String offsetStr = args.get("offset");

        int offset = Integer.parseInt(offsetStr);
        int limit = (limitStr != null && !limitStr.isEmpty()) ? Integer.parseInt(limitStr) : aps.size();

        int to = Math.min(offset + limit, aps.size());
        if (offset >= aps.size()) {
            ConsolePrinter.gray("Offset out of range (max " + (aps.size() - 1) + ").");
            return;
        }
        List<String> display = aps.subList(offset, to);

        ConsolePrinter.list("Access Points (" + aps.size() + " total)", display, s -> s);
    }
}

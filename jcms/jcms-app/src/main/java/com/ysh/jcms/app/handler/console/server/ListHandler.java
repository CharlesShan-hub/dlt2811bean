package com.ysh.jcms.app.handler.console.server;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.utils.scl.model.ied.SclIED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ListHandler implements CommandHandler {

    @Override
    public String name() { return "list"; }

    @Override
    public String description() { return "列出资源  ap: 访问点"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("type", "资源类型: ap", "ap"),
            new Param("limit", "数量（不传则列出全部）", null),
            new Param("offset", "起始索引", "0")
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.getSclManager().isLoaded()) {
            ConsolePrinter.error("SCL not loaded.");
            return;
        }

        String type = args.get("type");

        if ("ap".equals(type)) {
            listAccessPoints(console, args);
        } else {
            ConsolePrinter.error("Unknown list type: " + type + "  (available: ap)");
        }
    }

    private void listAccessPoints(CmsConsole console, Map<String, String> args) {
        List<String> aps = new ArrayList<>();
        for (SclIED ied : console.getSclManager().getIeds()) {
            ied.getAccessPoints().forEach(ap -> aps.add(ied.getName() + "/" + ap.getName()));
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

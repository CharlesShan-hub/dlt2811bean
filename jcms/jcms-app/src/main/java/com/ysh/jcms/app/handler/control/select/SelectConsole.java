package com.ysh.jcms.app.handler.control.select;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SelectConsole implements CommandHandler {

    @Override
    public String name() {
        return "select";
    }

    @Override
    public String description() {
        return "选择控制对象 (Select)。用法: select --ref <reference> [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "控制对象引用，如 LD0/CTRL1.SPC1", null), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        if (!CmsConsole.requireParam(args, "ref", "Usage: select --ref <reference>"))
            return;

        String ref = args.get("ref").trim();
        ConsolePrinter.info("Selecting: " + ref);
        console.getClient(SelectClient.class).execute(ref);
        CmsConsole.outputMessage("Selected " + ref, args);
    }
}

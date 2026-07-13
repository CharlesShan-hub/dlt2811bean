package com.ysh.jcms.app.handler.control.cancel;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CancelConsole implements CommandHandler {

    @Override
    public String name() {
        return "cancel";
    }

    @Override
    public String description() {
        return "取消选择控制对象 (Cancel)。用法: cancel --ref <reference> [--value true/false] [--origin <num>] [--ctlNum <num>] [--test true/false] [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "控制对象引用", null), new Param("value", "控制值 (SPC: true/false)", ""),
                new Param("origin", "操作源 (0=本地, 1=远程)", ""), new Param("ctlNum", "控制序号", ""), new Param("test", "测试标志", ""),
                new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        if (!CmsConsole.requireParam(args, "ref", "Usage: cancel --ref <reference>"))
            return;

        String ref = args.get("ref").trim();
        ConsolePrinter.info("Cancelling: " + ref);
        console.getClient(CancelClient.class).execute(ref, args);
        CmsConsole.outputMessage("Cancelled " + ref, args);
    }
}

package com.ysh.jcms.app.handler.control.select;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.control.selectWithValue.SelectWithValueClient;
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
        return "选择控制对象 (Select/SelectWithValue)。用法: select --ref <reference> [--value true/false] [--origin <num>] [--ctlNum <num>] [--test true/false] [--check syncheck,interlock] [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "控制对象引用，如 LD0/CTRL1.SPC1", null),
                new Param("value", "控制值（有值则走 SelectWithValue，无则走 Select）", ""), new Param("origin", "操作源 (0=本地, 1=远程)", ""),
                new Param("ctlNum", "控制序号", ""), new Param("test", "测试标志", ""), new Param("check", "校验 (syncheck,interlock)", ""),
                new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        if (!CmsConsole.requireParam(args, "ref", "Usage: select --ref <reference>"))
            return;

        String ref = args.get("ref").trim();

        String value = args.get("value");
        if (value != null && !value.isEmpty()) {
            ConsolePrinter.info("Selecting (with value): " + ref);
            console.getClient(SelectWithValueClient.class).execute(ref, args);
            CmsConsole.outputMessage("Selected (with value) " + ref, args);
        } else {
            ConsolePrinter.info("Selecting: " + ref);
            console.getClient(SelectClient.class).execute(ref);
            CmsConsole.outputMessage("Selected " + ref, args);
        }
    }
}

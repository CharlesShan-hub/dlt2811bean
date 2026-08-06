package com.ysh.jcms.app.handler.control.selectWithValue;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SelectWithValueConsole extends CommandHandler {

    public SelectWithValueConsole() {
        super(CommandInfo.SELECT_WITH_VALUE);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "控制对象引用", null), new Param("value", "控制值 (SPC: true/false)", ""),
                new Param("origin", "操作源 (0=本地, 1=远程)", ""), new Param("ctlNum", "控制序号", ""), new Param("test", "测试标志", ""),
                new Param("check", "校验 (syncheck,interlock)", ""), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        if (!CmsConsole.requireParam(args, "ref", "Usage: select-with-value --ref <reference>"))
            return;

        String ref = args.get("ref").trim();
        ConsolePrinter.info("Selecting with value: " + ref);
        SelectWithValueDao dao = new SelectWithValueDao().ref(ref).args(args);
        console.getClient(SelectWithValueClient.class).execute(dao);
        CmsConsole.outputMessage("Selected (with value) " + ref, args);
    }
}

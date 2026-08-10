package com.ysh.jcms.app.handler.control.selectWithValue;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;

import java.util.Map;

public class SelectWithValueConsole extends CommandHandler<SelectWithValueDao, SelectWithValueClient> {

    public SelectWithValueConsole() {
        super(CommandInfo.SELECT_WITH_VALUE, false);
        param("ref", "控制对象引用", null);
        param("value", "控制值 (SPC: true/false)", "");
        param("origin", "操作源 (0=本地, 1=远程)", "");
        param("ctlNum", "控制序号", "");
        param("test", "测试标志", "");
        param("check", "校验 (syncheck,interlock)", "");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;
        if (!CmsConsole.requireParam(args, "ref", "Usage: select-with-value --ref <reference>"))
            return;

        String ref = args.get("ref").trim();
        ConsolePrinter.info("Selecting with value: " + ref);
        SelectWithValueDao dao = new SelectWithValueDao().ref(ref).args(args);
        console.getClient(SelectWithValueClient.class).execute(dao);
        ConsolePrinter.success("Selected (with value) " + ref);
    }
}

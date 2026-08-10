package com.ysh.jcms.app.handler.control.select;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.control.selectWithValue.SelectWithValueClient;
import com.ysh.jcms.app.handler.control.selectWithValue.SelectWithValueDao;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SelectConsole extends CommandHandler<SelectDao, SelectClient> {

    public SelectConsole() {
        super(CommandInfo.SELECT, false);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(
                new Param("ref", "控制对象引用，如 LD0/CTRL1.SPC1", null),
                new Param("value", "控制值（有值则走 SelectWithValue，无则走 Select）", ""),
                new Param("origin", "操作源 (0=本地, 1=远程)", ""),
                new Param("ctlNum", "控制序号", ""),
                new Param("test", "测试标志", ""),
                new Param("check", "校验 (syncheck,interlock)", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;
        if (!CmsConsole.requireParam(args, "ref", "Usage: select --ref <reference>"))
            return;

        String ref = args.get("ref").trim();

        String value = args.get("value");
        if (value != null && !value.isEmpty()) {
            ConsolePrinter.info("Selecting (with value): " + ref);
            SelectWithValueDao dao = new SelectWithValueDao().ref(ref).args(args);
            console.getClient(SelectWithValueClient.class).execute(dao);
            ConsolePrinter.success("Selected (with value) " + ref);
        } else {
            SelectDao dao = new SelectDao().ref(ref);
            console.getClient(SelectClient.class).execute(dao);
            ConsolePrinter.success("Selected " + ref);
        }
    }
}
package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class AllDataDefConsole extends CommandHandler<AllDataDefDao, AllDataDefClient> {

    public AllDataDefConsole() {
        super(CommandInfo.ALL_DEF);
        Param p = Param.of("ln", null, "ln", String.class, true);
        param(p, "ldName 或 lnReference（如 LD0 或 LD0/LLN0）");
        Param p2 = Param.of("fc", "XX", "fc", String.class, false);
        param(p2, "功能约束过滤（如 ST, MX, CF, DC），默认 XX 即不过滤");
        Param p3 = Param.of("after", "", "referenceAfter", String.class, false);
        param(p3, "起始引用（分页截取）");
        Param p4 = Param.of("auto-pull", "false", null, String.class, false);
        param(p4, "自动续拉分页（true/false）");
    }
}

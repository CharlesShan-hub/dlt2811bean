package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class LnDirConsole extends CommandHandler<LnDirDao, LnDirClient> {

    public LnDirConsole() {
        super(CommandInfo.LN_DIR);
        Param p = Param.of("ln", null, "reference", String.class, true);
        param(p, "逻辑节点引用，如 LD0 或 LD0/LTSM1");
        Param p3 = Param.of("after", null, "referenceAfter", String.class, false);
        param(p3, "起始引用（分页截取，不传则从头开始）");
        Param p4 = Param.of("auto-pull", "false", null, String.class, false);
        param(p4, "自动续拉分页（true/false）");
    }
}

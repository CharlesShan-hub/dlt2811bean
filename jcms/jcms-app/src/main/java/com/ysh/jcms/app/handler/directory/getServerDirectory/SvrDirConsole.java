package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class SvrDirConsole extends CommandHandler<SvrDirDao, SvrDirClient> {

    public SvrDirConsole() {
        super(CommandInfo.SERVER_DIR);
        Param p = Param.of("after", "", "referenceAfter", String.class, false);
        param(p, "起始引用（分页截取，不传则从头开始）");
        Param p2 = Param.of("auto-pull", "false", null, String.class, false);
        param(p2, "自动续拉分页（true/false）");
    }
}
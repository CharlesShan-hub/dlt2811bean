package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class LdDirConsole extends CommandHandler<LdDirDao, LdDirClient> {

    public LdDirConsole() {
        super(CommandInfo.LD_DIR);
        Param p = Param.of("ld", null, "ldName", String.class, false);
        param(p, "逻辑设备名，如 LD0（不传则返回所有逻辑设备的完整引用）");
        Param p2 = Param.of("after", null, "referenceAfter", String.class, false);
        param(p2, "起始引用（分页截取，不传则从头开始）");
        Param p3 = Param.of("auto-pull", "false", null, String.class, false);
        param(p3, "自动续拉分页（true/false）");
    }
}

package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class GetDataDirectoryConsole extends CommandHandler<GetDataDirectoryDao, GetDataDirectoryClient> {

    public GetDataDirectoryConsole() {
        super(CommandInfo.DATA_DIR);
        Param p = Param.of("ref", null, "dataReference", String.class, true);
        param(p, "数据引用，如 LD0/LLN0 或 LD0/LLN0.Mod");
        Param p2 = Param.of("after", null, "referenceAfter", String.class, false);
        param(p2, "起始引用（分页截取）");
        Param p3 = Param.of("auto-pull", "false", null, String.class, false);
        param(p3, "自动续拉分页");
    }
}

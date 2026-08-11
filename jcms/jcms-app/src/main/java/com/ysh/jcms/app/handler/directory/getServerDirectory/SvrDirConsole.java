package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;

public class SvrDirConsole extends CommandHandler<SvrDirDao, SvrDirClient> {

    public SvrDirConsole() {
        super(CommandInfo.SERVER_DIR);
        param("after", "起始引用（分页截取，不传则从头开始）", "", "referenceAfter");
        param("auto-pull", "自动续拉分页（true/false）", "false");
    }
}

package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;

public class SvrDirConsole extends CommandHandler<SvrDirDao, SvrDirClient> {

    public SvrDirConsole() {
        super(CommandInfo.SERVER_DIR);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("after", "起始引用（分页截取，不传则从头开始）", "", "referenceAfter"),
                new Param("auto-pull", "自动续拉分页（true/false）", "false"));
    }
}

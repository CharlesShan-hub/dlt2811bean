package com.ysh.jcms.app.handler.sg.selectActiveSg;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class SelectActiveSgConsole extends CommandHandler<SelectActiveSgDao, SelectActiveSgClient> {

    public SelectActiveSgConsole() {
        super(CommandInfo.SELECT_ACTIVE_SG, false);
        Param p1 = Param.of("ref", null, "sgcbReference", String.class, true);
        param(p1, "SGCB 引用，如 LD0/LLN0.SG1");
        Param p2 = Param.of("num", null, "settingGroupNumber", int.class, true);
        param(p2, "定值组号（1~numOfSG）");
    }
}

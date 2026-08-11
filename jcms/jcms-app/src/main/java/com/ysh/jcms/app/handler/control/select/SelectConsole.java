package com.ysh.jcms.app.handler.control.select;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class SelectConsole extends CommandHandler<SelectDao, SelectClient> {

    public SelectConsole() {
        super(CommandInfo.SELECT, false);
        Param p = Param.of("ref", null, "ref", String.class, true);
        param(p, "控制对象引用，格式 LD/LN.DO");
    }
}

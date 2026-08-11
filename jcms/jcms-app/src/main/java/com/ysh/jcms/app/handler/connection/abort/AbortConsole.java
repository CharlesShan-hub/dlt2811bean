package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class AbortConsole extends CommandHandler<AbortDao, AbortClient> {

    public AbortConsole() {
        super(CommandInfo.ABORT, false);
        Param p = Param.of("reason", "0", "reason", Integer.class, false);
        param(p, "中止原因码");
    }
}

package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param.ParamType;

public class AbortConsole extends CommandHandler<AbortDao, AbortClient> {

    public AbortConsole() {
        super(CommandInfo.ABORT, false);
        param("reason", "中止原因码", "0", "reason", ParamType.INT);
    }
}

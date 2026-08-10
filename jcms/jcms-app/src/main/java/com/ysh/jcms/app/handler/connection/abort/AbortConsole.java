package com.ysh.jcms.app.handler.connection.abort;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.console.Param.ParamType;

import java.util.Arrays;
import java.util.List;

public class AbortConsole extends CommandHandler<AbortDao, AbortClient> {

    public AbortConsole() {
        super(CommandInfo.ABORT, false);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("reason", "中止原因码", "0", ParamType.INT));
    }
}

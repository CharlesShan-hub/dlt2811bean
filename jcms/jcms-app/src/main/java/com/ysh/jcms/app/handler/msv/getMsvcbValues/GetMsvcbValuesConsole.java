package com.ysh.jcms.app.handler.msv.getMsvcbValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class GetMsvcbValuesConsole extends CommandHandler<GetMsvcbValuesDao, GetMsvcbValuesClient> {

    public GetMsvcbValuesConsole() {
        super(CommandInfo.GET_MSVCB_VALS, true);
        Param p = Param.of("refs", null, "refs", List.class, true);
        param(p, "MSVCB 引用列表（空格分隔），如 \"LD0/SV1.msvcb01 LD0/SV1.msvcb02\"");
        Param p2 = Param.of("delimiter", null, null, String.class, false);
        param(p2, "列表分隔符（默认空格，可指定逗号、竖线等）");
    }
}

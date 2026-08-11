package com.ysh.jcms.app.handler.report.getUrcbValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class GetUrcbValuesConsole extends CommandHandler<GetUrcbValuesDao, GetUrcbValuesClient> {

    public GetUrcbValuesConsole() {
        super(CommandInfo.GET_URCB_VALS, true);
        Param p = Param.of("refs", null, "refs", List.class, true);
        param(p, "URCB 引用列表（空格分隔），如 \"LD0/LLN0.urcbAin LD0/LLN0.urcbEvent\"");
        Param p2 = Param.of("delimiter", null, null, String.class, false);
        param(p2, "列表分隔符（默认空格，可指定逗号、竖线等）");
    }
}

package com.ysh.jcms.app.handler.report.getBrcbValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class GetBrcbValuesConsole extends CommandHandler<GetBrcbValuesDao, GetBrcbValuesClient> {

    public GetBrcbValuesConsole() {
        super(CommandInfo.GET_BRCB_VALS, true);
        Param p = Param.of("refs", null, "refs", List.class, true);
        param(p, "BRCB 引用列表（空格分隔），如 \"LD0/LLN0.brcbAlarm LD0/LLN0.brcbEvent\"");
        Param p2 = Param.of("delimiter", null, null, String.class, false);
        param(p2, "列表分隔符（默认空格，可指定逗号、竖线等）");
    }
}

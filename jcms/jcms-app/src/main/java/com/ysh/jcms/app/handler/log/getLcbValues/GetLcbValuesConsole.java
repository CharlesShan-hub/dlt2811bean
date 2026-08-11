package com.ysh.jcms.app.handler.log.getLcbValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class GetLcbValuesConsole extends CommandHandler<GetLcbValuesDao, GetLcbValuesClient> {

    public GetLcbValuesConsole() {
        super(CommandInfo.GET_LCB_VALS, true);
        Param p = Param.of("refs", null, "refs", List.class, true);
        param(p, "LCB 引用列表（空格分隔），如 \"LD0/LLN0.lcb1\"");
    }
}
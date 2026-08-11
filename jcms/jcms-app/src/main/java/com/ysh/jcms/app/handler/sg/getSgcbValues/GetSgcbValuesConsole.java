package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class GetSgcbValuesConsole extends CommandHandler<GetSgcbValuesDao, GetSgcbValuesClient> {

    public GetSgcbValuesConsole() {
        super(CommandInfo.SGCB_VALS);
        Param p = Param.of("refs", null, "refs", List.class, true);
        param(p, "SGCB 引用列表（空格分隔），如 \"LD0/LLN0.SG1\"");
    }
}

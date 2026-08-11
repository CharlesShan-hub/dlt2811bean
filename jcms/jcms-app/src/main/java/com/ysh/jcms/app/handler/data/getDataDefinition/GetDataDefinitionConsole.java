package com.ysh.jcms.app.handler.data.getDataDefinition;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class GetDataDefinitionConsole extends CommandHandler<GetDataDefinitionDao, GetDataDefinitionClient> {

    public GetDataDefinitionConsole() {
        super(CommandInfo.GET_DATA_DEF);
        Param p = Param.of("refs", null, "refs", List.class, true);
        param(p, "数据引用列表（空格分隔），如 \"LD0/LLN0.Mod LD0/LLN0.Beh.stVal\"");
        Param p2 = Param.of("fc", "XX", "fc", String.class, false);
        param(p2, "功能约束过滤（如 ST, MX, CF, DC），默认 XX 即不过滤");
        Param p3 = Param.of("delimiter", null, null, String.class, false);
        param(p3, "列表分隔符（默认空格，可指定逗号、竖线等）");
    }
}

package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class GetEditSgValueConsole extends CommandHandler<GetEditSgValueDao, GetEditSgValueClient> {

    public GetEditSgValueConsole() {
        super(CommandInfo.GET_EDIT_SG);
        Param p1 = Param.of("refs", null, "refs", List.class, true);
        param(p1, "数据引用列表（空格分隔），如 LD0/LLN0.Mod LD0/LLN0.Beh");
        Param p2 = Param.of("fc", "SG", "fc", String.class, false);
        param(p2, "功能约束（SG 或 SE），默认 SG");
        Param p3 = Param.of("delimiter", null, null, String.class, false);
        param(p3, "列表分隔符（默认空格，可指定逗号、竖线等）");
    }
}

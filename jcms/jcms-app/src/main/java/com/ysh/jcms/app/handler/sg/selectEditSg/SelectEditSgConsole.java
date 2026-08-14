package com.ysh.jcms.app.handler.sg.selectEditSg;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class SelectEditSgConsole extends CommandHandler<SelectEditSgDao, SelectEditSgClient> {

    public SelectEditSgConsole() {
        super(CommandInfo.SELECT_EDIT_SG, false);
        Param p1 = Param.of("ref", null, "sgcbReference", String.class, true);
        param(p1, "SGCB 引用，如 LD0/LLN0.SG（仅 LLN0 下有效）");
        Param p2 = Param.of("num", null, "settingGroupNumber", int.class, true);
        param(p2, "定值组号（1~numOfSG）");
    }
}

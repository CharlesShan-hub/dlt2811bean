package com.ysh.jcms.app.handler.sg.confirmEditSgValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class ConfirmEditSgValuesConsole extends CommandHandler<ConfirmEditSgValuesDao, ConfirmEditSgValuesClient> {

    public ConfirmEditSgValuesConsole() {
        super(CommandInfo.CONFIRM_EDIT_SG, false);
        Param p = Param.of("ref", null, "sgcbReference", String.class, true);
        param(p, "SGCB 引用，如 PROT/DeZonePTOC1.SG1");
    }
}

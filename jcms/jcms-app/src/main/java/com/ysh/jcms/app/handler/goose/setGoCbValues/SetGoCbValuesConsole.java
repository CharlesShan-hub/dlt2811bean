package com.ysh.jcms.app.handler.goose.setGoCbValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class SetGoCbValuesConsole extends CommandHandler<SetGoCbValuesDao, SetGoCbValuesClient> {

    public SetGoCbValuesConsole() {
        super(CommandInfo.SET_GOCB_VALS);
        Param p = Param.of("ref", null, "ref", String.class, true);
        param(p, "GoCB 引用，如 LD0/LLN0.gocb1");
        Param p2 = Param.of("go-ena", null, "goEna", Boolean.class, false);
        param(p2, "GOOSE 使能 (true/false)");
        Param p3 = Param.of("go-id", null, "goID", String.class, false);
        param(p3, "GOOSE ID (VisibleString129)");
        Param p4 = Param.of("dat-set", null, "datSet", String.class, false);
        param(p4, "数据集引用 (ObjectReference)");
    }
}
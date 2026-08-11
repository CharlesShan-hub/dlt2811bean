package com.ysh.jcms.app.handler.control.cancel;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class CancelConsole extends CommandHandler<CancelDao, CancelClient> {

    public CancelConsole() {
        super(CommandInfo.CANCEL, false);
        Param p = Param.of("ref", null, "ref", String.class, true);
        param(p, "控制对象引用，格式 LD/LN.DO");
        Param p2 = Param.of("value", "", "value", String.class, false);
        param(p2, "控制值，SPC 填 true/false");
        Param p3 = Param.of("origin", "", "origin", String.class, false);
        param(p3, "操作源，0 本地 1 远程");
        Param p4 = Param.of("ctlNum", "", "ctlNum", String.class, false);
        param(p4, "控制序号");
        Param p5 = Param.of("test", "", "test", String.class, false);
        param(p5, "测试标志");
    }
}

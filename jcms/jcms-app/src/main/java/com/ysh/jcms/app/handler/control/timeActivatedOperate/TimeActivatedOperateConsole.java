package com.ysh.jcms.app.handler.control.timeActivatedOperate;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class TimeActivatedOperateConsole extends CommandHandler<TimeActivatedOperateDao, TimeActivatedOperateClient> {

    public TimeActivatedOperateConsole() {
        super(CommandInfo.TIME_ACT_OPE, false);
        Param p = Param.of("ref", null, "ref", String.class, true);
        param(p, "控制对象引用，格式 LD/LN.DO");
        Param p2 = Param.of("ctlVal", null, "ctlVal", String.class, false);
        param(p2, "控制值，SPC 填 true/false");
        Param p3 = Param.of("operTm", null, "operTm", String.class, true);
        param(p3, "执行时间，UTC 秒级时间戳");
        Param p4 = Param.of("origin", null, "origin", String.class, false);
        param(p4, "操作源，0 本地 1 远程");
        Param p5 = Param.of("ctlNum", null, "ctlNum", String.class, false);
        param(p5, "控制序号");
        Param p6 = Param.of("t", null, "t", String.class, false);
        param(p6, "时间戳");
        Param p7 = Param.of("test", null, "test", String.class, false);
        param(p7, "测试标志");
        Param p8 = Param.of("check", null, "check", String.class, false);
        param(p8, "校验方式，syncheck 或 interlock");
    }
}

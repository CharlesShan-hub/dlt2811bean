package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class SetUrcbValuesConsole extends CommandHandler<SetUrcbValuesDao, SetUrcbValuesClient> {

    public SetUrcbValuesConsole() {
        super(CommandInfo.SET_URCB_VALS);
        Param p1 = Param.of("ref", null, "ref", String.class, true);
        param(p1, "URCB 引用，如 LD0/LLN0.urcb1");
        Param p2 = Param.of("rpt-id", null, "rptId", String.class, false);
        param(p2, "报告标识 (VisibleString129)");
        Param p3 = Param.of("rpt-ena", null, "rptEna", Boolean.class, false);
        param(p3, "报告使能 (true/false)");
        Param p4 = Param.of("resv", null, "resv", Boolean.class, false);
        param(p4, "保留 (BOOLEAN)");
        Param p5 = Param.of("dat-set", null, "datSet", String.class, false);
        param(p5, "数据集引用 (ObjectReference)");
        Param p6 = Param.of("opt-flds", null, "optFlds", Integer.class, false);
        param(p6, "选项字段 (RCBOptFlds bitmask)");
        Param p7 = Param.of("buf-tm", null, "bufTm", Integer.class, false);
        param(p7, "缓存时间 (INT32U, 毫秒)");
        Param p8 = Param.of("trg-ops", null, "trgOps", Integer.class, false);
        param(p8, "触发条件 (TriggerConditions bitmask)");
        Param p9 = Param.of("intg-pd", null, "intgPd", Integer.class, false);
        param(p9, "完整性周期 (INT32U, 毫秒)");
        Param p10 = Param.of("gi", null, "gi", Boolean.class, false);
        param(p10, "总召唤命令 (BOOLEAN: true=触发一次)");
    }
}

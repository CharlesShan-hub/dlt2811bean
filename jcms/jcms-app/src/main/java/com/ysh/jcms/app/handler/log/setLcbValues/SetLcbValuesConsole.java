package com.ysh.jcms.app.handler.log.setLcbValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class SetLcbValuesConsole extends CommandHandler<SetLcbValuesDao, SetLcbValuesClient> {

    public SetLcbValuesConsole() {
        super(CommandInfo.SET_LCB_VALS);
        Param p1 = Param.of("ref", null, "ref", String.class, true);
        param(p1, "LCB 引用，如 LD0/LLN0.lcb1");
        Param p2 = Param.of("log-ena", null, "logEna", Boolean.class, false);
        param(p2, "日志使能 (true/false)");
        Param p3 = Param.of("dat-set", null, "datSet", String.class, false);
        param(p3, "数据集引用 (ObjectReference)");
        Param p4 = Param.of("trg-ops", null, "trgOps", Integer.class, false);
        param(p4, "触发条件 (bitmask: 1=integrity,2=data_change,4=quality_change,8=data_update,16=gi)");
        Param p5 = Param.of("intg-pd", null, "intgPd", Integer.class, false);
        param(p5, "完整性周期 (INT32U, 毫秒)");
        Param p6 = Param.of("log-ref", null, "logRef", String.class, false);
        param(p6, "日志引用 (ObjectReference)");
        Param p7 = Param.of("opt-flds", null, "optFlds", Integer.class, false);
        param(p7, "选项字段 (LCBOptFlds bitmask)");
        Param p8 = Param.of("buf-tm", null, "bufTm", Integer.class, false);
        param(p8, "缓存时间 (INT32U, 毫秒)");
    }
}
package com.ysh.jcms.app.handler.msv.setMsvcbValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class SetMsvcbValuesConsole extends CommandHandler<SetMsvcbValuesDao, SetMsvcbValuesClient> {

    public SetMsvcbValuesConsole() {
        super(CommandInfo.SET_MSVCB_VALS);
        Param p1 = Param.of("ref", null, "ref", String.class, true);
        param(p1, "MSVCB 引用，如 LD0/SV1.msvcb01");
        Param p2 = Param.of("sv-ena", null, "svEna", Boolean.class, false);
        param(p2, "启用采样值发送 (true/false)");
        Param p3 = Param.of("msv-id", null, "msvId", String.class, false);
        param(p3, "MSV 标识 (VisibleString129)");
        Param p4 = Param.of("dat-set", null, "datSet", String.class, false);
        param(p4, "数据集引用 (ObjectReference)");
        Param p5 = Param.of("smp-mod", null, "smpMod", Integer.class, false);
        param(p5, "采样模式 (SmpMod: 0=per-nominal-period, 1=per-second, 2=per-sample)");
        Param p6 = Param.of("smp-rate", null, "smpRate", Integer.class, false);
        param(p6, "采样率 (INT16U)");
        Param p7 = Param.of("opt-flds", null, "optFlds", Integer.class, false);
        param(p7, "选项字段 (MSVCBOptFlds bitmask)");
    }
}

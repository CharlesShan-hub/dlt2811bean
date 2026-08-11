package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class SetBrcbValuesConsole extends CommandHandler<SetBrcbValuesDao, SetBrcbValuesClient> {

    public SetBrcbValuesConsole() {
        super(CommandInfo.SET_BRCB_VALS);
        Param p1 = Param.of("ref", null, "ref", String.class, true);
        param(p1, "BRCB 引用，如 LD0/LLN0.brcb1");
        Param p2 = Param.of("rpt-id", null, "rptId", String.class, false);
        param(p2, "报告标识 (VisibleString129)");
        Param p3 = Param.of("rpt-ena", null, "rptEna", Boolean.class, false);
        param(p3, "报告使能 (true/false)");
        Param p4 = Param.of("dat-set", null, "datSet", String.class, false);
        param(p4, "数据集引用 (ObjectReference)");
        Param p5 = Param.of("opt-flds", null, "optFlds", Integer.class, false);
        param(p5, "选项字段 (RCBOptFlds bitmask)");
        Param p6 = Param.of("buf-tm", null, "bufTm", Integer.class, false);
        param(p6, "缓存时间 (INT32U, 毫秒)");
        Param p7 = Param.of("trg-ops", null, "trgOps", Integer.class, false);
        param(p7, "触发条件 (TriggerConditions bitmask)");
        Param p8 = Param.of("intg-pd", null, "intgPd", Integer.class, false);
        param(p8, "完整性周期 (INT32U, 毫秒)");
        Param p9 = Param.of("gi", null, "gi", Boolean.class, false);
        param(p9, "总召唤命令 (BOOLEAN: true=触发一次)");
        Param p10 = Param.of("purge-buf", null, "purgeBuf", Boolean.class, false);
        param(p10, "清空缓存命令 (BOOLEAN: true=触发一次)");
        Param p11 = Param.of("entry-id", null, "entryId", String.class, false);
        param(p11, "条目 ID (EntryID, 8字节)");
        Param p12 = Param.of("resv-tms", null, "resvTms", Integer.class, false);
        param(p12, "保留时间 (INT16)");
    }
}

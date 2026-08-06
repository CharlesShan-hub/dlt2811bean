package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetBrcbValuesConsole extends CommandHandler {

    public SetBrcbValuesConsole() {
        super(CommandInfo.SET_BRCB_VALS);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "BRCB 引用，如 LD0/LLN0.brcb1", null), new Param("rpt-id", "报告标识 (VisibleString129)", null),
                new Param("rpt-ena", "报告使能 (true/false)", null), new Param("dat-set", "数据集引用 (ObjectReference)", null),
                new Param("buf-tm", "缓存时间 (INT32U, 毫秒)", null), new Param("intg-pd", "完整性周期 (INT32U, 毫秒)", null),
                new Param("gi", "总召唤命令 (BOOLEAN: true=触发一次)", null), new Param("purge-buf", "清空缓存命令 (BOOLEAN: true=触发一次)", null),
                new Param("resv-tms", "保留时间 (INT16)", null), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;

        if (!CmsConsole.requireParam(args, "ref", "Usage: set-brcb-vals --ref <brcbRef> [options]"))
            return;

        String ref = args.get("ref");
        SetBrcbValuesDao dao = new SetBrcbValuesDao().ref(ref.trim());
        String v;

        v = args.get("rpt-id");
        if (v != null && !v.isEmpty())
            dao.rptId(v);
        v = args.get("rpt-ena");
        if (v != null && !v.isEmpty())
            dao.rptEna(Boolean.parseBoolean(v));
        v = args.get("dat-set");
        if (v != null && !v.isEmpty())
            dao.datSet(v);
        v = args.get("buf-tm");
        if (v != null && !v.isEmpty())
            dao.bufTm(Integer.parseInt(v));
        v = args.get("intg-pd");
        if (v != null && !v.isEmpty())
            dao.intgPd(Integer.parseInt(v));
        v = args.get("gi");
        if (v != null && !v.isEmpty())
            dao.gi(Boolean.parseBoolean(v));
        v = args.get("purge-buf");
        if (v != null && !v.isEmpty())
            dao.purgeBuf(Boolean.parseBoolean(v));
        v = args.get("resv-tms");
        if (v != null && !v.isEmpty())
            dao.resvTms(Integer.parseInt(v));

        ConsolePrinter.info("Setting BRCB values: ref=" + ref);
        console.getClient(SetBrcbValuesClient.class).execute(dao);
        CmsConsole.outputMessage("BRCB values set for " + ref, args);
    }
}

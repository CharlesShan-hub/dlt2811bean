package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetBrcbValuesConsole implements CommandHandler {

    @Override
    public String name() { return "set-brcb-vals"; }

    @Override
    public String description() { return "设置缓存报告控制块值 (SetBRCBValues)。\n" +
        "  案例:\n" +
        "    set-brcb-vals --ref LD0/LLN0.brcb1                               # 仅引用（无字段修改）\n" +
        "    set-brcb-vals --ref LD0/LLN0.brcb1 --rpt-ena true                 # 启用报告\n" +
        "    set-brcb-vals --ref LD0/LLN0.brcb1 --rpt-id \"MyRptID\" --dat-set \"dsAlarm\"  # 改标识和数据集\n" +
        "    set-brcb-vals --ref LD0/LLN0.brcb1 --buf-tm 2000 --intg-pd 5000   # 改缓存时间和完整性周期\n" +
        "    set-brcb-vals --ref LD0/LLN0.brcb1 --gi true                       # 总召唤\n" +
        "    set-brcb-vals --ref LD0/LLN0.brcb1 --purge-buf true                # 清空缓存\n" +
        "    set-brcb-vals --ref LD0/LLN0.brcb1 --rpt-ena false --resv-tms 10   # 关闭报告+保留时间"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ref", "BRCB 引用，如 LD0/LLN0.brcb1", null),
            new Param("rpt-id", "报告标识 (VisibleString129)", null),
            new Param("rpt-ena", "报告使能 (true/false)", null),
            new Param("dat-set", "数据集引用 (ObjectReference)", null),
            new Param("buf-tm", "缓存时间 (INT32U, 毫秒)", null),
            new Param("intg-pd", "完整性周期 (INT32U, 毫秒)", null),
            new Param("gi", "总召唤命令 (BOOLEAN: true=触发一次)", null),
            new Param("purge-buf", "清空缓存命令 (BOOLEAN: true=触发一次)", null),
            new Param("resv-tms", "保留时间 (INT16)", null)
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) {
            ConsolePrinter.error("Not connected. Type 'connect' first.");
            return;
        }

        String ref = args.get("ref");
        if (ref == null || ref.trim().isEmpty()) {
            ConsolePrinter.error("Missing --ref. Usage: set-brcb-vals --ref <brcbRef> [options]");
            return;
        }

        SetBrcbValuesDao dao = new SetBrcbValuesDao().ref(ref.trim());
        String v;

        v = args.get("rpt-id"); if (v != null && !v.isEmpty()) dao.rptId(v);
        v = args.get("rpt-ena"); if (v != null && !v.isEmpty()) dao.rptEna(Boolean.parseBoolean(v));
        v = args.get("dat-set"); if (v != null && !v.isEmpty()) dao.datSet(v);
        v = args.get("buf-tm"); if (v != null && !v.isEmpty()) dao.bufTm(Integer.parseInt(v));
        v = args.get("intg-pd"); if (v != null && !v.isEmpty()) dao.intgPd(Integer.parseInt(v));
        v = args.get("gi"); if (v != null && !v.isEmpty()) dao.gi(Boolean.parseBoolean(v));
        v = args.get("purge-buf"); if (v != null && !v.isEmpty()) dao.purgeBuf(Boolean.parseBoolean(v));
        v = args.get("resv-tms"); if (v != null && !v.isEmpty()) dao.resvTms(Integer.parseInt(v));

        ConsolePrinter.info("Setting BRCB values: ref=" + ref);
        console.getClient(SetBrcbValuesClient.class).execute(dao);
        ConsolePrinter.success("BRCB values set for " + ref);
    }
}

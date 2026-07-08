package com.ysh.jcms.app.handler.log.setLcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetLcbValuesConsole implements CommandHandler {

    @Override
    public String name() { return "set-lcb-vals"; }

    @Override
    public String description() { return "设置日志控制块值 (SetLCBValues) [--json]。\n" +
        "  案例:\n" +
        "    set-lcb-vals --ref LD0/LLN0.lcb1                               # 仅引用（无字段修改）\n" +
        "    set-lcb-vals --ref LD0/LLN0.lcb1 --log-ena true                 # 启用日志\n" +
        "    set-lcb-vals --ref LD0/LLN0.lcb1 --log-ena false                # 停用日志\n" +
        "    set-lcb-vals --ref LD0/LLN0.lcb1 --dat-set \"dsLog\"             # 改数据集\n" +
        "    set-lcb-vals --ref LD0/LLN0.lcb1 --intg-pd 10000 --buf-tm 2000  # 改完整性和缓存"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ref", "LCB 引用，如 LD0/LLN0.lcb1", null),
            new Param("log-ena", "日志使能 (true/false)", null),
            new Param("dat-set", "数据集引用 (ObjectReference)", null),
            new Param("trg-ops", "触发条件 (bitmask)", null),
            new Param("intg-pd", "完整性周期 (INT32U, 毫秒)", null),
            new Param("log-ref", "日志引用 (ObjectReference)", null),
            new Param("opt-flds", "选项字段 (LCBOptFlds bitmask)", null),
            new Param("buf-tm", "缓存时间 (INT32U, 毫秒)", null),
            new Param("json", "JSON 格式输出", "")
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = "true".equals(args.get("json"));
        if (!console.isConnected()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Not connected. Type 'connect' first.\"}");
            } else {
                ConsolePrinter.error("Not connected. Type 'connect' first.");
            }
            return;
        }

        String ref = args.get("ref");
        if (ref == null || ref.trim().isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Missing --ref.\"}");
            } else {
                ConsolePrinter.error("Missing --ref. Usage: set-lcb-vals --ref <lcbRef> [options]");
            }
            return;
        }

        SetLcbValuesDao dao = new SetLcbValuesDao().ref(ref.trim());
        String v;

        v = args.get("log-ena"); if (v != null && !v.isEmpty()) dao.logEna(Boolean.parseBoolean(v));
        v = args.get("dat-set"); if (v != null && !v.isEmpty()) dao.datSet(v);
        v = args.get("trg-ops"); if (v != null && !v.isEmpty()) dao.trgOps(Integer.parseInt(v));
        v = args.get("intg-pd"); if (v != null && !v.isEmpty()) dao.intgPd(Integer.parseInt(v));
        v = args.get("log-ref"); if (v != null && !v.isEmpty()) dao.logRef(v);
        v = args.get("opt-flds"); if (v != null && !v.isEmpty()) dao.optFlds(Integer.parseInt(v));
        v = args.get("buf-tm"); if (v != null && !v.isEmpty()) dao.bufTm(Integer.parseInt(v));

        if (!jsonMode) {
            ConsolePrinter.info("Setting LCB values: ref=" + ref);
        }
        console.getClient(SetLcbValuesClient.class).execute(dao);
        if (jsonMode) {
            ConsolePrinter.raw("{\"success\":true,\"message\":\"LCB values set for " + CmsFormatUtil.escapeJson(ref) + "\"}");
        } else {
            ConsolePrinter.success("LCB values set for " + ref);
        }
    }
}

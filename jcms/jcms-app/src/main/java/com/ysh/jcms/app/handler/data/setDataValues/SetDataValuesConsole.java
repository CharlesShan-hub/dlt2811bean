package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetDataValuesConsole implements CommandHandler {

    @Override
    public String name() { return "set-data-values"; }

    @Override
    public String description() { return "设置数据值 (SetDataValues) —— 用法: set-data-values --pairs \"<ref1>=<val1> <ref2>=<val2>...\" [--fc FC] [--json]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("pairs", "数据引用=值 对（空格分隔），如 \"LD0/LLN0.Mod.stVal=true LD0/LLN0.Beh.stVal=1\"", null),
            new Param("fc", "功能约束过滤（如 ST, MX, CF, DC），默认 XX 即不过滤", "XX"),
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

        String pairsStr = args.get("pairs");
        if (pairsStr == null || pairsStr.trim().isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Missing --pairs.\"}");
            } else {
                ConsolePrinter.error("Missing --pairs. Usage: set-data-values --pairs \"<ref1>=<val1> <ref2>=<val2>...\" [--fc FC]");
            }
            return;
        }

        String[] tokens = pairsStr.trim().split("\\s+");
        SetDataValuesDao dao = new SetDataValuesDao();

        for (String token : tokens) {
            if (token.isEmpty()) continue;
            int eqIdx = token.indexOf('=');
            if (eqIdx <= 0) {
                if (jsonMode) {
                    ConsolePrinter.raw("{\"success\":false,\"error\":\"Invalid pair: " + CmsFormatUtil.escapeJson(token) + " (expected ref=value)\"}");
                } else {
                    ConsolePrinter.error("Invalid pair: " + token + " (expected ref=value)");
                }
                return;
            }
            String ref = token.substring(0, eqIdx);
            String value = token.substring(eqIdx + 1);
            dao.addEntry(ref, value);
        }

        ConsolePrinter.info("Setting " + dao.entries().size() + " data value(s)...");

        console.getClient(SetDataValuesClient.class).execute(dao);

        String msg = "Set " + dao.entries().size() + " data value(s) successfully";
        if (jsonMode) {
            ConsolePrinter.raw("{\"success\":true,\"message\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
        } else {
            ConsolePrinter.success(msg);
        }
    }
}

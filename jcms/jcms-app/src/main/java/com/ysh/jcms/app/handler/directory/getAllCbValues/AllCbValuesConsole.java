package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AllCbValuesConsole implements CommandHandler {

    private static final Map<String, Integer> ACSI_MAP = new LinkedHashMap<>();
    static {
        ACSI_MAP.put("brcb", 3);
        ACSI_MAP.put("urcb", 4);
        ACSI_MAP.put("lcb", 5);
        ACSI_MAP.put("sgecb", 7);
        ACSI_MAP.put("gocb", 8);
        ACSI_MAP.put("msvcb", 10);
        // numeric aliases
        ACSI_MAP.put("3", 3);
        ACSI_MAP.put("4", 4);
        ACSI_MAP.put("5", 5);
        ACSI_MAP.put("7", 7);
        ACSI_MAP.put("8", 8);
        ACSI_MAP.put("10", 10);
    }

    private static final String[] CB_TYPE_NAMES = {"BRCB", "URCB", "LCB", "SGECB", "GOCB", "MSVCB"};

    @Override
    public String name() {
        return "all-cb";
    }

    @Override
    public String description() {
        return "获取所有控制块值 (GetAllCBValues)。用法: all-cb --ln <ldName|lnReference> --acsi <type> [--after REF] [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ln", "ldName 或 lnReference（如 LD0 或 LD0/LLN0）", null),
                new Param("acsi", "ACSI 类型: brcb/urcb/lcb/sgecb/gocb/msvcb 或数字", null), new Param("after", "起始引用（分页截取）", ""),
                new Param("json", "JSON 格式输出", ""));
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

        String target = args.get("ln");
        if (target == null || target.isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Missing --ln.\"}");
            } else {
                ConsolePrinter.error("Missing --ln. Usage: all-cb --ln <ldName|lnReference> --acsi <type> [--after REF]");
            }
            return;
        }

        String acsiStr = args.get("acsi");
        if (acsiStr == null || acsiStr.isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Missing --acsi.\"}");
            } else {
                ConsolePrinter.error("Missing --acsi. Usage: all-cb --ln <ldName|lnReference> --acsi <type> [--after REF]");
            }
            return;
        }
        Integer acsiClass = ACSI_MAP.get(acsiStr.toLowerCase());
        if (acsiClass == null) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Invalid acsiClass: " + CmsFormatUtil.escapeJson(acsiStr) + ".\"}");
            } else {
                ConsolePrinter.error("Invalid acsiClass: " + acsiStr + ". Valid values: brcb, urcb, lcb, sgecb, gocb, msvcb");
            }
            return;
        }

        AllCbValuesDao dao = new AllCbValuesDao();
        if (target.contains("/")) {
            dao.lnReference(target);
        } else {
            dao.ldName(target);
        }
        dao.acsiClass(acsiClass);

        String after = args.get("after");
        if (after != null && !after.isEmpty() && !after.equals("0")) {
            dao.referenceAfter(after);
        }

        String cbTypeName = acsiClass >= 3 && acsiClass <= 10
                ? CB_TYPE_NAMES[acsiClass == 10 ? 5 : acsiClass - 3]
                : String.valueOf(acsiClass);
        if (!jsonMode) {
            ConsolePrinter.info("Fetching CB values: target=" + target + " type=" + cbTypeName);
        }

        console.getClient(AllCbValuesClient.class).execute(dao);

        List<AllCbValuesClient.CbEntry> entries = console.getClient(AllCbValuesClient.class).getLastEntries();
        if (entries.isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":true,\"data\":[]}");
            } else {
                ConsolePrinter.info("No CB values found");
            }
            return;
        }
        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
            for (int i = 0; i < entries.size(); i++) {
                if (i > 0)
                    sb.append(',');
                AllCbValuesClient.CbEntry e = entries.get(i);
                int idx = e.cbType;
                String typeName = idx >= 0 && idx < CB_TYPE_NAMES.length ? CB_TYPE_NAMES[idx] : "?";
                sb.append("{\"reference\":\"").append(CmsFormatUtil.escapeJson(e.reference)).append("\",\"type\":\"")
                        .append(CmsFormatUtil.escapeJson(typeName)).append("\"}");
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            ConsolePrinter.list("CB values (" + entries.size() + " items)", new java.util.ArrayList<>(entries), e -> {
                int idx = e.cbType; /* CmsCbValueChoice: 0=BRCB, 1=URCB, 2=LCB, 3=SGECB, 4=GOCB, 5=MSVCB */
                String typeName = idx >= 0 && idx < CB_TYPE_NAMES.length ? CB_TYPE_NAMES[idx] : "?";
                return e.reference + "  [" + typeName + "]";
            });
        }
    }
}

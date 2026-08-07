package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.util.CmsFormatUtil;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AllCbValuesConsole extends CommandHandler {

    private static final Map<String, Integer> ACSI_MAP = new LinkedHashMap<>();
    static {
        ACSI_MAP.put("brcb", 3);
        ACSI_MAP.put("urcb", 4);
        ACSI_MAP.put("lcb", 5);
        ACSI_MAP.put("sgcb", 7);
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

    private static final String[] CB_TYPE_NAMES = {"BRCB", "URCB", "LCB", "SGCB", "GOCB", "MSVCB"};

    public AllCbValuesConsole() {
        super(CommandInfo.ALL_CB);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ln", "ldName 或 lnReference（如 LD0 或 LD0/LLN0）", null),
                new Param("acsi", "ACSI 类型: brcb/urcb/lcb/sgcb/gocb/msvcb 或数字", null), new Param("after", "起始引用（分页截取）", ""),
                new Param("auto-pull", "自动续拉分页（true/false）", "false"), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = CmsConsole.isJsonMode(args);
        if (!console.requireConnected(args))
            return;

        if (!CmsConsole.requireParam(args, "ln", "Usage: all-cb --ln <ldName|lnReference> --acsi <type> [--after REF]"))
            return;
        if (!CmsConsole.requireParam(args, "acsi", "Usage: all-cb --ln <ldName|lnReference> --acsi <type> [--after REF]"))
            return;

        String target = args.get("ln");
        String acsiStr = args.get("acsi");
        Integer acsiClass = ACSI_MAP.get(acsiStr.toLowerCase());
        if (acsiClass == null) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Invalid acsiClass: " + CmsFormatUtil.escapeJson(acsiStr) + ".\"}");
            } else {
                ConsolePrinter.error("Invalid acsiClass: " + acsiStr + ". Valid values: brcb, urcb, lcb, sgcb, gocb, msvcb");
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

        String autoPull = args.get("auto-pull");
        if ("true".equalsIgnoreCase(autoPull)) {
            dao.autoPull(true);
        }

        String cbTypeName = acsiClass >= 3 && acsiClass <= 10
                ? CB_TYPE_NAMES[acsiClass == 10 ? 5 : acsiClass - 3]
                : String.valueOf(acsiClass);
        if (!jsonMode) {
            ConsolePrinter.info("Fetching CB values: target=" + target + " type=" + cbTypeName);
        }

        PaginationContext ctx = new PaginationContext();
        console.getClient(AllCbValuesClient.class).execute(dao, ctx);
        boolean moreFollows = ctx.isLastMoreFollows();

        @SuppressWarnings("unchecked")
        List<AllCbValuesClient.CbEntry> entries = (List<AllCbValuesClient.CbEntry>) ctx.getResult();
        if (entries == null) {
            entries = java.util.Collections.emptyList();
        }
        if (entries.isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":true,\"moreFollows\":" + moreFollows + ",\"data\":[]}");
            } else {
                ConsolePrinter.info("No CB values found");
            }
            return;
        }
        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"moreFollows\":" + moreFollows + ",\"data\":[");
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
                int idx = e.cbType; /* CmsCbValueChoice: 0=BRCB, 1=URCB, 2=LCB, 3=SGCB, 4=GOCB, 5=MSVCB */
                String typeName = idx >= 0 && idx < CB_TYPE_NAMES.length ? CB_TYPE_NAMES[idx] : "?";
                return e.reference + "  [" + typeName + "]";
            });
        }
    }
}

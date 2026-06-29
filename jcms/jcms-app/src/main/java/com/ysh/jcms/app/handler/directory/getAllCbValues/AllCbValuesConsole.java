package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

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

    private static final String[] CB_TYPE_NAMES = {
        "BRCB", "URCB", "LCB", "SGECB", "GOCB", "MSVCB"
    };

    @Override
    public String name() { return "all-cb"; }

    @Override
    public String description() { return "获取所有控制块值 (GetAllCBValues)"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("target", "ldName 或 lnReference（如 LD0 或 LD0/LLN0）", null),
            new Param("acsiClass", "ACSI 类型: brcb/urcb/lcb/sgecb/gocb/msvcb 或数字", null),
            new Param("referenceAfter", "起始引用（分页截取）", null)
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) {
            ConsolePrinter.error("Not connected. Type 'connect' first.");
            return;
        }

        String target = args.get("target");
        if (target == null || target.isEmpty()) {
            ConsolePrinter.error("Missing target. Usage: all-cb <ldName|lnReference> <acsiClass> [referenceAfter]");
            return;
        }

        String acsiStr = args.get("acsiClass");
        if (acsiStr == null || acsiStr.isEmpty()) {
            ConsolePrinter.error("Missing acsiClass. Usage: all-cb <ldName|lnReference> <acsiClass> [referenceAfter]");
            return;
        }
        Integer acsiClass = ACSI_MAP.get(acsiStr.toLowerCase());
        if (acsiClass == null) {
            ConsolePrinter.error("Invalid acsiClass: " + acsiStr + ". Valid values: brcb, urcb, lcb, sgecb, gocb, msvcb");
            return;
        }

        AllCbValuesDao dao = new AllCbValuesDao();
        if (target.contains("/")) {
            dao.lnReference(target);
        } else {
            dao.ldName(target);
        }
        dao.acsiClass(acsiClass);

        String after = args.get("referenceAfter");
        if (after != null && !after.isEmpty() && !after.equals("0")) {
            dao.referenceAfter(after);
        }

        String cbTypeName = acsiClass >= 3 && acsiClass <= 10
            ? CB_TYPE_NAMES[acsiClass == 10 ? 5 : acsiClass - 3] : String.valueOf(acsiClass);
        ConsolePrinter.info("Fetching CB values: target=" + target + " type=" + cbTypeName);

        console.getClient(AllCbValuesClient.class).execute(dao);

        List<AllCbValuesClient.CbEntry> entries = console.getClient(AllCbValuesClient.class).getLastEntries();
        if (entries.isEmpty()) {
            ConsolePrinter.info("No CB values found");
            return;
        }
        ConsolePrinter.list("CB values (" + entries.size() + " items)",
            new java.util.ArrayList<>(entries),
            e -> {
                int idx = e.cbType;  /* CmsCbValueChoice: 0=BRCB, 1=URCB, 2=LCB, 3=SGECB, 4=GOCB, 5=MSVCB */
                String typeName = idx >= 0 && idx < CB_TYPE_NAMES.length ? CB_TYPE_NAMES[idx] : "?";
                return e.reference + "  [" + typeName + "]";
            });
    }
}

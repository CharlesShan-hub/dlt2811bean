package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.node.ContentManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AllDataValuesConsole implements CommandHandler {

    private static final String[] CHOICE_NAMES = {
        "error", "array", "structure", "boolean", "int8", "int16", "int32", "int64",
        "int8u", "int16u", "int32u", "int64u", "float32", "float64",
        "bit-string", "octet-string", "visible-string", "unicode-string",
        "utc-time", "binary-time", "quality", "dbpos", "tcmd", "check"
    };

    @Override
    public String name() { return "all-data"; }

    @Override
    public String description() { return "获取所有数据值 (GetAllDataValues)。用法: all-data --target <ldName|lnReference> [--fc FC] [--after REF]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("target", "ldName 或 lnReference（如 LD0 或 LD0/LLN0）", null),
            new Param("fc", "功能约束过滤（如 ST, MX），默认 0 即不过滤", "0"),
            new Param("after", "起始引用（分页截取）", "")
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
            ConsolePrinter.error("Missing --target. Usage: all-data --target <ldName|lnReference> [--fc FC] [--after REF]");
            return;
        }

        AllDataValuesDao dao = new AllDataValuesDao();
        if (target.contains("/")) {
            dao.lnReference(target);
        } else {
            dao.ldName(target);
        }

        String fcStr = args.get("fc");
        if (fcStr != null && !fcStr.isEmpty()) {
            int fcVal = Integer.parseInt(fcStr);
            if (fcVal != 0) {
                dao.fc(fcVal);
            }
        }

        String after = args.get("after");
        if (after != null && !after.isEmpty() && !after.equals("0")) {
            dao.referenceAfter(after);
        }

        console.getClient(AllDataValuesClient.class).execute(dao);

        List<ContentManager.AllDataEntry> entries = console.getContentManager().getAllDataEntries();
        ConsolePrinter.list("Data values (" + entries.size() + " items)",
            new java.util.ArrayList<>(entries),
            e -> {
                String typeName = e.choiceType >= 0 && e.choiceType < CHOICE_NAMES.length
                    ? CHOICE_NAMES[e.choiceType] : "?";
                return e.reference + "  [" + typeName + "] " + e.valueString;
            });
    }
}

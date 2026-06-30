package com.ysh.jcms.app.handler.data.getDataValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetDataValuesConsole implements CommandHandler {

    private static final String[] CHOICE_NAMES = {
        "error", "array", "structure", "boolean", "int8", "int16", "int32", "int64",
        "int8u", "int16u", "int32u", "int64u", "float32", "float64",
        "bit-string", "octet-string", "visible-string", "unicode-string",
        "utc-time", "binary-time", "quality", "dbpos", "tcmd", "check"
    };

    @Override
    public String name() { return "get-data-values"; }

    @Override
    public String description() { return "获取数据值 (GetDataValues) —— 用法: get-data-values --refs \"<ref1> <ref2>...\" [--fc FC]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("refs", "数据引用列表（空格分隔），如 \"LD0/LLN0.Mod LD0/LLN0.Beh\"", null),
            new Param("fc", "功能约束过滤（如 ST, MX, CF, DC），默认 XX 即不过滤", "XX")
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) {
            ConsolePrinter.error("Not connected. Type 'connect' first.");
            return;
        }

        String refsStr = args.get("refs");
        if (refsStr == null || refsStr.trim().isEmpty()) {
            ConsolePrinter.error("Missing --refs. Usage: get-data-values --refs \"<ref1> <ref2>...\" [--fc FC]");
            return;
        }

        String fcStr = args.get("fc");
        boolean hasFc = fcStr != null && !fcStr.isEmpty();
        int fcCode = hasFc ? com.ysh.jcms.data.fc.CmsFC.fromString(fcStr) : com.ysh.jcms.data.fc.CmsFC.XX;

        String[] refs = refsStr.trim().split("\\s+");
        GetDataValuesDao dao = new GetDataValuesDao();
        for (String ref : refs) {
            if (!ref.isEmpty()) {
                dao.addRef(ref, fcCode);
            }
        }

        ConsolePrinter.info("Fetching data values for " + dao.dataRefs().size() + " reference(s)");

        console.getClient(GetDataValuesClient.class).execute(dao);

        List<GetDataValuesClient.DataValue> values = console.getClient(GetDataValuesClient.class).getLastValues();
        if (values.isEmpty()) {
            ConsolePrinter.info("No data values returned");
            return;
        }

        List<RefValuePair> displayPairs = new java.util.ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            GetDataValuesClient.DataValue v = values.get(i);
            String typeName = v.choiceType >= 0 && v.choiceType < CHOICE_NAMES.length
                ? CHOICE_NAMES[v.choiceType] : "?";
            String ref = i < refs.length ? refs[i] : "#" + i;
            displayPairs.add(new RefValuePair(ref, typeName, v.valueString));
        }
        ConsolePrinter.list("Data values (" + values.size() + " items)",
            displayPairs,
            p -> p.ref + "  [" + p.typeName + "] " + p.value);
    }

    private static final class RefValuePair {
        final String ref;
        final String typeName;
        final String value;
        RefValuePair(String ref, String typeName, String value) {
            this.ref = ref; this.typeName = typeName; this.value = value;
        }
    }
}

package com.ysh.jcms.app.handler.data.getDataDefinition;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetDataDefinitionConsole implements CommandHandler {

    private static final String[] CHOICE_NAMES = {
        "error", "array", "structure", "boolean", "int8", "int16", "int32", "int64",
        "int8u", "int16u", "int32u", "int64u", "float32", "float64",
        "bit-string", "octet-string", "visible-string", "unicode-string",
        "utc-time", "binary-time", "quality", "dbpos", "tcmd", "check"
    };

    @Override
    public String name() { return "get-data-def"; }

    @Override
    public String description() { return "获取数据定义 (GetDataDefinition)。用法: get-data-def --refs \"<ref1> <ref2>...\" [--fc FC]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("refs", "数据引用列表（空格分隔），如 \"LD0/LLN0.Mod LD0/LLN0.Beh.stVal\"", null),
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
            ConsolePrinter.error("Missing --refs. Usage: get-data-def --refs \"<ref1> <ref2>...\" [--fc FC]");
            return;
        }

        String fcStr = args.get("fc");

        String[] refs = refsStr.trim().split("\\s+");
        GetDataDefinitionDao dao = new GetDataDefinitionDao();
        if (fcStr != null && !fcStr.isEmpty() && !"XX".equalsIgnoreCase(fcStr)) {
            int fcCode = com.ysh.jcms.data.fc.CmsFC.fromString(fcStr);
            for (String ref : refs) {
                if (!ref.isEmpty()) dao.addRef(ref, fcCode);
            }
        } else {
            for (String ref : refs) {
                if (!ref.isEmpty()) dao.addRef(ref);
            }
        }

        ConsolePrinter.info("Fetching data definitions for " + dao.dataRefs().size() + " reference(s)");

        console.getClient(GetDataDefinitionClient.class).execute(dao);

        List<GetDataDefinitionClient.DefEntry> entries =
            console.getClient(GetDataDefinitionClient.class).getLastEntries();

        if (entries.isEmpty()) {
            ConsolePrinter.info("No data definitions returned");
            return;
        }

        List<RefDefPair> displayPairs = new java.util.ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            GetDataDefinitionClient.DefEntry e = entries.get(i);
            String typeName = e.choiceType >= 0 && e.choiceType < CHOICE_NAMES.length
                ? CHOICE_NAMES[e.choiceType] : "?";
            String ref = i < refs.length ? refs[i] : "#" + i;
            String cdcPart = e.cdcType.isEmpty() ? "" : "  cdc=" + e.cdcType;
            displayPairs.add(new RefDefPair(ref, typeName, cdcPart));
        }
        ConsolePrinter.list("Data definitions (" + entries.size() + " items)",
            displayPairs,
            p -> p.ref + "  [" + p.typeName + "]" + p.cdcPart);
    }

    private static final class RefDefPair {
        final String ref;
        final String typeName;
        final String cdcPart;
        RefDefPair(String ref, String typeName, String cdcPart) {
            this.ref = ref; this.typeName = typeName; this.cdcPart = cdcPart;
        }
    }
}

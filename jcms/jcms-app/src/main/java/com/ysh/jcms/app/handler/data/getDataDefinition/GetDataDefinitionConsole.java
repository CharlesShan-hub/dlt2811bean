package com.ysh.jcms.app.handler.data.getDataDefinition;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.util.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ysh.jcms.data.choice.CmsData;

public class GetDataDefinitionConsole extends CommandHandler {

    public GetDataDefinitionConsole() {
        super(CommandInfo.GET_DATA_DEF);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("refs", "数据引用列表（空格分隔），如 \"LD0/LLN0.Mod LD0/LLN0.Beh.stVal\"", null),
                new Param("fc", "功能约束过滤（如 ST, MX, CF, DC），默认 XX 即不过滤", "XX"), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = CmsConsole.isJsonMode(args);
        if (!console.requireConnected(args))
            return;

        if (!CmsConsole.requireParam(args, "refs", "Usage: get-data-def --refs \"<ref1> <ref2>...\" [--fc FC]"))
            return;

        String refsStr = args.get("refs");

        String fcStr = args.get("fc");

        String[] refs = refsStr.trim().split("\\s+");
        GetDataDefinitionDao dao = new GetDataDefinitionDao();
        if (fcStr != null && !fcStr.isEmpty() && !"XX".equalsIgnoreCase(fcStr)) {
            int fcCode = CmsFC.fromString(fcStr);
            for (String ref : refs) {
                if (!ref.isEmpty())
                    dao.addRef(ref, fcCode);
            }
        } else {
            for (String ref : refs) {
                if (!ref.isEmpty())
                    dao.addRef(ref);
            }
        }

        if (!jsonMode) {
            ConsolePrinter.info("Fetching data definitions for " + dao.dataRefs().size() + " reference(s)");
        }

        PaginationContext ctx = new PaginationContext();
        console.getClient(GetDataDefinitionClient.class).execute(dao, ctx);

        @SuppressWarnings("unchecked")
        List<GetDataDefinitionClient.DefEntry> entries = (List<GetDataDefinitionClient.DefEntry>) ctx.getResult();

        if (entries.isEmpty()) {
            ConsolePrinter.info("No data definitions returned");
            return;
        }

        List<RefDefPair> displayPairs = new java.util.ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            GetDataDefinitionClient.DefEntry e = entries.get(i);
            String typeName = e.choiceType >= 0 && e.choiceType < CmsData.CHOICE_NAMES.length ? CmsData.CHOICE_NAMES[e.choiceType] : "?";
            String ref = i < refs.length ? refs[i] : "#" + i;
            String cdcPart = e.cdcType.isEmpty() ? "" : "  cdc=" + e.cdcType;
            displayPairs.add(new RefDefPair(ref, typeName, cdcPart));
        }
        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
            for (int i = 0; i < displayPairs.size(); i++) {
                if (i > 0)
                    sb.append(',');
                RefDefPair p = displayPairs.get(i);
                String val = p.ref + "  [" + p.typeName + "]" + p.cdcPart;
                sb.append('"').append(CmsFormatUtil.escapeJson(val)).append('"');
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            ConsolePrinter.list("Data definitions (" + entries.size() + " items)", displayPairs,
                    p -> p.ref + "  [" + p.typeName + "]" + p.cdcPart);
        }
    }

    private static final class RefDefPair {
        final String ref;
        final String typeName;
        final String cdcPart;
        RefDefPair(String ref, String typeName, String cdcPart) {
            this.ref = ref;
            this.typeName = typeName;
            this.cdcPart = cdcPart;
        }
    }
}

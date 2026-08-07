package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.node.ContentManager;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.util.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ysh.jcms.data.choice.CmsData;

public class AllDataDefConsole extends CommandHandler {

    public AllDataDefConsole() {
        super(CommandInfo.ALL_DEF);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ln", "ldName 或 lnReference（如 LD0 或 LD0/LLN0）", null),
                new Param("fc", "功能约束过滤（如 ST, MX, CF, DC），默认 XX 即不过滤", "XX"), new Param("after", "起始引用（分页截取）", ""),
                new Param("auto-pull", "自动续拉分页（true/false）", "false"), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = CmsConsole.isJsonMode(args);
        if (!console.requireConnected(args))
            return;
        if (!CmsConsole.requireParam(args, "ln", "Usage: all-def --ln <ldName|lnReference> [--fc FC] [--after REF]"))
            return;
        String target = args.get("ln");

        AllDataDefDao dao = new AllDataDefDao();
        if (target.contains("/")) {
            dao.lnReference(target);
        } else {
            dao.ldName(target);
        }

        String fcStr = args.get("fc");
        if (fcStr != null && !fcStr.isEmpty() && !"XX".equalsIgnoreCase(fcStr)) {
            dao.fc(CmsFC.fromString(fcStr));
        }

        String after = args.get("after");
        if (after != null && !after.isEmpty() && !after.equals("0")) {
            dao.referenceAfter(after);
        }

        String autoPull = args.get("auto-pull");
        if ("true".equalsIgnoreCase(autoPull)) {
            dao.autoPull(true);
        }

        console.getClient(AllDataDefClient.class).execute(dao);
        boolean moreFollows = console.getClient(AllDataDefClient.class).isLastMoreFollows();

        List<ContentManager.DataDefEntry> entries = console.getContentManager().getDataDefEntries();
        if (entries.isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":true,\"moreFollows\":" + moreFollows + ",\"data\":[]}");
            } else {
                ConsolePrinter.info("No data definitions returned");
            }
            return;
        }

        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"moreFollows\":" + moreFollows + ",\"data\":[");
            for (int i = 0; i < entries.size(); i++) {
                if (i > 0)
                    sb.append(',');
                ContentManager.DataDefEntry e = entries.get(i);
                String typeName = e.choiceType >= 0 && e.choiceType < CmsData.CHOICE_NAMES.length
                        ? CmsData.CHOICE_NAMES[e.choiceType]
                        : "?";
                sb.append("{\"ref\":\"").append(CmsFormatUtil.escapeJson(e.reference)).append("\",\"type\":\"")
                        .append(CmsFormatUtil.escapeJson(typeName)).append("\",\"cdc\":\"").append(CmsFormatUtil.escapeJson(e.cdcType))
                        .append("\"}");
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            ConsolePrinter.list("Data definitions (" + entries.size() + " items)", new java.util.ArrayList<>(entries), e -> {
                String typeName = e.choiceType >= 0 && e.choiceType < CmsData.CHOICE_NAMES.length
                        ? CmsData.CHOICE_NAMES[e.choiceType]
                        : "?";
                String cdcPart = e.cdcType.isEmpty() ? "" : "  cdc=" + e.cdcType;
                return e.reference + "  [" + typeName + "]" + cdcPart;
            });
        }
    }
}

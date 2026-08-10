package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.app.node.ContentManager;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.util.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ysh.jcms.data.choice.CmsData;

public class AllDataValuesConsole extends CommandHandler {

    public AllDataValuesConsole() {
        super(CommandInfo.ALL_DATA);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ln", "ldName 或 lnReference（如 LD0 或 LD0/LLN0）", null),
                new Param("fc", "功能约束过滤（如 ST, MX, CF, DC），默认 XX 即不过滤", "XX"), new Param("after", "起始引用（分页截取）", ""),
                new Param("auto-pull", "自动续拉分页（true/false）", "false"));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;
        if (!CmsConsole.requireParam(args, "ln", "Usage: all-data --ln <ldName|lnReference> [--fc FC] [--after REF]"))
            return;
        String target = args.get("ln");

        AllDataValuesDao dao = new AllDataValuesDao();
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

        console.getClient(AllDataValuesClient.class).execute(dao);
        PaginationContext ctx = dao.paginationContext();
        boolean moreFollows = ctx.isLastMoreFollows();

        @SuppressWarnings("unchecked")
        List<ContentManager.AllDataEntry> entries = (List<ContentManager.AllDataEntry>) ctx.getResult();
        if (entries == null) {
            entries = java.util.Collections.emptyList();
        }
        if (entries.isEmpty()) {
            ConsolePrinter.raw("{\"success\":true,\"moreFollows\":" + moreFollows + ",\"data\":[]}");
            return;
        }

        StringBuilder sb = new StringBuilder("{\"success\":true,\"moreFollows\":" + moreFollows + ",\"data\":[");
        for (int i = 0; i < entries.size(); i++) {
            if (i > 0)
                sb.append(',');
            ContentManager.AllDataEntry e = entries.get(i);
            String typeName = e.choiceType >= 0 && e.choiceType < CmsData.CHOICE_NAMES.length ? CmsData.CHOICE_NAMES[e.choiceType] : "?";
            sb.append("{\"ref\":\"").append(CmsFormatUtil.escapeJson(e.reference)).append("\",\"type\":\"")
                    .append(CmsFormatUtil.escapeJson(typeName)).append("\",\"value\":\"").append(CmsFormatUtil.escapeJson(e.valueString))
                    .append("\"}");
        }
        sb.append("]}");
        ConsolePrinter.raw(sb.toString());
    }
}

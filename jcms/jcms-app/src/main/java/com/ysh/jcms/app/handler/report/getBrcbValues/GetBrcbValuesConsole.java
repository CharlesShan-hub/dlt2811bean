package com.ysh.jcms.app.handler.report.getBrcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.util.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetBrcbValuesConsole extends CommandHandler {

    public GetBrcbValuesConsole() {
        super(CommandInfo.GET_BRCB_VALS);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("refs", "BRCB 引用列表（空格分隔），如 \"LD0/LLN0.brcbAlarm\"", null), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = CmsConsole.isJsonMode(args);
        if (!console.requireConnected(args))
            return;

        if (!CmsConsole.requireParam(args, "refs", "Usage: get-brcb-vals --refs \"<ref1> <ref2>...\""))
            return;

        String refsStr = args.get("refs");

        String[] refs = refsStr.trim().split("\\s+");
        GetBrcbValuesDao dao = new GetBrcbValuesDao();
        for (String ref : refs) {
            if (!ref.isEmpty())
                dao.addRef(ref.trim());
        }

        if (!jsonMode) {
            ConsolePrinter.info("Fetching BRCB values for " + dao.refs().size() + " reference(s)");
        }

        PaginationContext ctx = new PaginationContext();
        console.getClient(GetBrcbValuesClient.class).execute(dao, ctx);

        @SuppressWarnings("unchecked")
        List<GetBrcbValuesClient.BrcbEntry> entries = (List<GetBrcbValuesClient.BrcbEntry>) ctx.getResult();

        if (entries.isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":true,\"data\":[]}");
            } else {
                ConsolePrinter.info("No BRCB values returned");
            }
            return;
        }

        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
            for (int i = 0; i < entries.size(); i++) {
                String ref = i < refs.length ? refs[i] : "#" + i;
                if (i > 0)
                    sb.append(',');
                sb.append("{\"ref\":\"").append(CmsFormatUtil.escapeJson(ref)).append("\"");
                sb.append(",\"desc\":\"").append(CmsFormatUtil.escapeJson(entries.get(i).desc)).append("\"");
                sb.append("}");
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            List<Object> display = new java.util.ArrayList<>();
            for (int i = 0; i < entries.size(); i++) {
                String ref = i < refs.length ? refs[i] : "#" + i;
                display.add(ref + "  " + entries.get(i).desc);
            }
            ConsolePrinter.list("BRCB values (" + entries.size() + " items)", display, Object::toString);
        }
    }
}

package com.ysh.jcms.app.handler.log.getLcbValues;

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

public class GetLcbValuesConsole extends CommandHandler {

    public GetLcbValuesConsole() {
        super(CommandInfo.GET_LCB_VALS);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("refs", "LCB 引用列表（空格分隔），如 \"LD0/LLN0.lcb1\"", null));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        if (!CmsConsole.requireParam(args, "refs", "Usage: get-lcb-vals --refs \"<ref1> <ref2>...\""))
            return;

        String refsStr = args.get("refs");

        String[] refs = refsStr.trim().split("\\s+");
        GetLcbValuesDao dao = new GetLcbValuesDao();
        for (String ref : refs) {
            if (!ref.isEmpty())
                dao.addRef(ref.trim());
        }

        console.getClient(GetLcbValuesClient.class).execute(dao);
        PaginationContext ctx = dao.paginationContext();

        @SuppressWarnings("unchecked")
        List<GetLcbValuesClient.LcbEntry> entries = (List<GetLcbValuesClient.LcbEntry>) ctx.getResult();

        if (entries.isEmpty()) {
            ConsolePrinter.raw("{\"success\":true,\"data\":[]}");
            return;
        }

        StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
        for (int i = 0; i < entries.size(); i++) {
            if (i > 0)
                sb.append(',');
            String ref = i < refs.length ? refs[i] : "#" + i;
            sb.append("{\"ref\":\"").append(CmsFormatUtil.escapeJson(ref)).append("\",\"desc\":\"")
                    .append(CmsFormatUtil.escapeJson(entries.get(i).desc)).append("\"}");
        }
        sb.append("]}");
        ConsolePrinter.raw(sb.toString());
    }
}

package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.util.CmsFormatUtil;
import com.ysh.jcms.data.scalar.CmsFC;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ysh.jcms.data.choice.CmsData;

public class GetEditSgValueConsole extends CommandHandler {

    public GetEditSgValueConsole() {
        super(CommandInfo.GET_EDIT_SG);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("refs", "数据引用列表（空格分隔），如 LD0/LLN0.Mod LD0/LLN0.Beh", null),
                new Param("fc", "功能约束（SG 或 SE），默认 SG", "SG"));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        if (!CmsConsole.requireParam(args, "refs", "Missing --refs"))
            return;

        String refsStr = args.get("refs");
        String fcStr = args.get("fc");
        if (fcStr == null)
            fcStr = "SG";

        int fcCode = CmsFC.fromString(fcStr);

        String[] refs = refsStr.trim().split("\\s+");
        GetEditSgValueDao dao = new GetEditSgValueDao();
        for (String ref : refs) {
            if (!ref.isEmpty())
                dao.addRef(ref.trim(), fcCode);
        }

        console.getClient(GetEditSgValueClient.class).execute(dao);
        PaginationContext ctx = dao.paginationContext();

        @SuppressWarnings("unchecked")
        List<GetEditSgValueClient.ValueEntry> values = (List<GetEditSgValueClient.ValueEntry>) ctx.getResult();

        if (values.isEmpty()) {
            ConsolePrinter.raw("{\"success\":true,\"data\":[]}");
            return;
        }

        StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":[");
        for (int i = 0; i < values.size(); i++) {
            GetEditSgValueClient.ValueEntry v = values.get(i);
            String ref = i < refs.length ? refs[i] : "#" + i;
            String typeName = v.choice >= 0 && v.choice < CmsData.CHOICE_NAMES.length ? CmsData.CHOICE_NAMES[v.choice] : "?";
            if (i > 0)
                sb.append(',');
            sb.append("{\"ref\":\"").append(CmsFormatUtil.escapeJson(ref)).append("\"");
            sb.append(",\"type\":\"").append(CmsFormatUtil.escapeJson(typeName)).append("\"");
            sb.append(",\"value\":\"").append(CmsFormatUtil.escapeJson(v.text)).append("\"");
            sb.append("}");
        }
        sb.append("]}");
        ConsolePrinter.raw(sb.toString());
    }
}

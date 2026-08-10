package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.util.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetGooseElementNumberConsole extends CommandHandler {

    public GetGooseElementNumberConsole() {
        super(CommandInfo.GET_GOOSE_ELEM_NUM);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "GoCB 引用，如 LD0/LLN0.gocb1", null),
                new Param("members", "成员列表（空格分隔），格式 ref:fc，如 \"LD0/LLN0.DO1:1 LD0/LLN0.DO2:6\"", null));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        if (!CmsConsole.requireParam(args, "ref", "Usage: get-goose-elem-num --ref <gocbRef> --members \"...\""))
            return;
        if (!CmsConsole.requireParam(args, "members", "Usage: get-goose-elem-num --ref <gocbRef> --members \"...\""))
            return;

        String ref = args.get("ref");
        String membersStr = args.get("members");

        GetGooseElementNumberDao dao = new GetGooseElementNumberDao().gocbReference(ref.trim());
        for (String s : membersStr.trim().split("\\s+")) {
            if (s.isEmpty())
                continue;
            String[] parts = s.split(":");
            if (parts.length < 2) {
                ConsolePrinter.raw(
                        "{\"success\":false,\"error\":\"Invalid member spec: " + CmsFormatUtil.escapeJson(s) + " (expected ref:fc)\"}");
                return;
            }
            dao.addMember(parts[0], Integer.parseInt(parts[1]));
        }

        console.getClient(GetGooseElementNumberClient.class).execute(dao);

        GetGooseElementNumberClient.ElementNumberResult result = (GetGooseElementNumberClient.ElementNumberResult) dao.result();

        if (result == null) {
            ConsolePrinter.raw("{\"success\":true,\"data\":null}");
            return;
        }

        StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":{");
        sb.append("\"gocbRef\":\"").append(CmsFormatUtil.escapeJson(result.gocbReference)).append("\",");
        sb.append("\"confRev\":").append(result.confRev).append(",");
        sb.append("\"datSet\":\"").append(CmsFormatUtil.escapeJson(result.datSet)).append("\",");
        sb.append("\"memberOffsets\":[");
        for (int i = 0; i < result.memberOffsets.size(); i++) {
            if (i > 0)
                sb.append(',');
            sb.append(result.memberOffsets.get(i));
        }
        sb.append("]}");
        ConsolePrinter.raw(sb.toString());
    }
}

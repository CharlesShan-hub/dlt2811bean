package com.ysh.jcms.app.handler.goose.getGoReference;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetGoReferenceConsole implements CommandHandler {

    @Override
    public String name() { return "get-go-ref"; }

    @Override
    public String description() { return "读 GOOSE 引用 (GetGoReference, 8.9.2)。\n" +
        "  用法: get-go-ref --ref LD0/LLN0.gocb1 --offsets \"0 1 2\""; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ref", "GoCB 引用，如 LD0/LLN0.gocb1", null),
            new Param("offsets", "成员偏移列表（空格分隔），如 \"0 1 2\"", null)
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) {
            ConsolePrinter.error("Not connected. Type 'connect' first.");
            return;
        }

        String ref = args.get("ref");
        if (ref == null || ref.trim().isEmpty()) {
            ConsolePrinter.error("Missing --ref. Usage: get-go-ref --ref <gocbRef> --offsets \"0 1 ...\"");
            return;
        }

        String offsetsStr = args.get("offsets");
        if (offsetsStr == null || offsetsStr.trim().isEmpty()) {
            ConsolePrinter.error("Missing --offsets. Usage: get-go-ref --ref <gocbRef> --offsets \"0 1 ...\"");
            return;
        }

        GetGoReferenceDao dao = new GetGoReferenceDao().gocbReference(ref.trim());
        for (String s : offsetsStr.trim().split("\\s+")) {
            if (!s.isEmpty()) dao.addMemberOffset(Integer.parseInt(s));
        }

        ConsolePrinter.info("Fetching Go reference for " + ref + " with " + dao.memberOffsets().size() + " offset(s)");

        console.getClient(GetGoReferenceClient.class).execute(dao);

        GetGoReferenceClient.GoRefResult result =
            console.getClient(GetGoReferenceClient.class).getLastResult();

        if (result == null) {
            ConsolePrinter.info("No Go reference returned");
            return;
        }

        ConsolePrinter.info("  gocbRef=" + result.gocbReference);
        ConsolePrinter.info("  confRev=" + result.confRev);
        ConsolePrinter.info("  datSet=" + result.datSet);
        for (int i = 0; i < result.members.size(); i++) {
            GetGoReferenceClient.MemberDataEntry m = result.members.get(i);
            ConsolePrinter.info("  member[" + i + "]: ref=" + m.reference + " fc=" + m.fc);
        }
    }
}

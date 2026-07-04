package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetGooseElementNumberConsole implements CommandHandler {

    @Override
    public String name() { return "get-goose-elem-num"; }

    @Override
    public String description() { return "读 GOOSE 元素序号 (GetGOOSEElementNumber, 8.9.3)。\n" +
        "  用法: get-goose-elem-num --ref LD0/LLN0.gocb1 --members \"ref1:fc1 ref2:fc2\""; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ref", "GoCB 引用，如 LD0/LLN0.gocb1", null),
            new Param("members", "成员列表（空格分隔），格式 ref:fc，如 \"LD0/LLN0.DO1:1 LD0/LLN0.DO2:6\"", null)
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
            ConsolePrinter.error("Missing --ref. Usage: get-goose-elem-num --ref <gocbRef> --members \"...\"");
            return;
        }

        String membersStr = args.get("members");
        if (membersStr == null || membersStr.trim().isEmpty()) {
            ConsolePrinter.error("Missing --members. Usage: get-goose-elem-num --ref <gocbRef> --members \"...\"");
            return;
        }

        GetGooseElementNumberDao dao = new GetGooseElementNumberDao().gocbReference(ref.trim());
        for (String s : membersStr.trim().split("\\s+")) {
            if (s.isEmpty()) continue;
            String[] parts = s.split(":");
            if (parts.length < 2) {
                ConsolePrinter.error("Invalid member spec: " + s + " (expected ref:fc)");
                return;
            }
            dao.addMember(parts[0], Integer.parseInt(parts[1]));
        }

        ConsolePrinter.info("Fetching element number for " + ref + " with " + dao.members().size() + " member(s)");

        console.getClient(GetGooseElementNumberClient.class).execute(dao);

        GetGooseElementNumberClient.ElementNumberResult result =
            console.getClient(GetGooseElementNumberClient.class).getLastResult();

        if (result == null) {
            ConsolePrinter.info("No element number returned");
            return;
        }

        ConsolePrinter.info("  gocbRef=" + result.gocbReference);
        ConsolePrinter.info("  confRev=" + result.confRev);
        ConsolePrinter.info("  datSet=" + result.datSet);
        for (int i = 0; i < result.memberOffsets.size(); i++) {
            ConsolePrinter.info("  offset[" + i + "]=" + result.memberOffsets.get(i));
        }
    }
}

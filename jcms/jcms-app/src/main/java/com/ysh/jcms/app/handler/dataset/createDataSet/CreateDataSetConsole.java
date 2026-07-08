package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CreateDataSetConsole implements CommandHandler {

    @Override
    public String name() { return "create-dataset"; }

    @Override
    public String description() { return "创建数据集 (CreateDataSet)。用法: create-dataset --ds <ref> --members \"<ref1>,<fc1> <ref2>,<fc2>...\" [--json]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ds", "数据集引用，如 \"LD0/LLN0.myDs\"", null),
            new Param("members", "成员列表（空格分隔，每个成员的格式 ref,fc），如 \"LD0/GGIO1.Alm1,ST LD0/GGIO1.Alm2,ST\"", null),
            new Param("after", "追加到现有数据集后的最后一个成员引用", ""),
            new Param("json", "JSON 格式输出", "")
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = "true".equals(args.get("json"));
        if (!console.isConnected()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Not connected. Type 'connect' first.\"}");
            } else {
                ConsolePrinter.error("Not connected. Type 'connect' first.");
            }
            return;
        }

        String dsRef = args.get("ds");
        if (dsRef == null || dsRef.trim().isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Missing --ds.\"}");
            } else {
                ConsolePrinter.error("Missing --ds. Usage: create-dataset --ds <ref> --members \"...\"");
            }
            return;
        }

        String membersStr = args.get("members");
        if (membersStr == null || membersStr.trim().isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Missing --members.\"}");
            } else {
                ConsolePrinter.error("Missing --members");
            }
            return;
        }

        CreateDataSetDao dao = new CreateDataSetDao()
            .datasetReference(dsRef.trim());

        String[] tokens = membersStr.trim().split("\\s+");
        for (String token : tokens) {
            if (token.isEmpty()) continue;
            int commaIdx = token.indexOf(',');
            if (commaIdx <= 0) {
                if (jsonMode) {
                    ConsolePrinter.raw("{\"success\":false,\"error\":\"Invalid member: " + CmsFormatUtil.escapeJson(token) + " (expected ref,fc)\"}");
                } else {
                    ConsolePrinter.error("Invalid member: " + token + " (expected ref,fc)");
                }
                return;
            }
            String ref = token.substring(0, commaIdx);
            String fcStr = token.substring(commaIdx + 1);
            int fcCode = com.ysh.jcms.data.fc.CmsFC.fromString(fcStr);
            dao.addMember(ref, fcCode);
        }

        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }

        ConsolePrinter.info("Creating dataset " + dsRef + " with " + dao.members().size() + " member(s)");

        console.getClient(CreateDataSetClient.class).execute(dao);

        String msg = "Created dataset " + dsRef + " successfully";
        if (jsonMode) {
            ConsolePrinter.raw("{\"success\":true,\"message\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
        } else {
            ConsolePrinter.success(msg);
        }
    }
}

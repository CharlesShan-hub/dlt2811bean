package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetDataSetValuesConsole implements CommandHandler {

    @Override
    public String name() {
        return "set-dataset-values";
    }

    @Override
    public String description() {
        return "设置数据集值 (SetDataSetValues)。用法: set-dataset-values --ds <ref> --values \"<val1> <val2>...\" [--after REF] [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ds", "数据集引用，如 \"LD0/LLN0.dsAlarm\"", null), new Param("values", "数据值列表（空格分隔），如 \"aa bb cc\"", null),
                new Param("after", "起始引用（分页截取）", ""), new Param("json", "JSON 格式输出", ""));
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
                ConsolePrinter.error("Missing --ds. Usage: set-dataset-values --ds <ref> --values \"<val1> <val2>...\"");
            }
            return;
        }

        String valuesStr = args.get("values");
        if (valuesStr == null || valuesStr.trim().isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"Missing --values.\"}");
            } else {
                ConsolePrinter.error("Missing --values. Usage: set-dataset-values --ds <ref> --values \"<val1> <val2>...\"");
            }
            return;
        }

        SetDataSetValuesDao dao = new SetDataSetValuesDao().datasetReference(dsRef.trim());

        String[] vals = valuesStr.trim().split("\\s+");
        for (String v : vals) {
            if (!v.isEmpty())
                dao.addValue(v);
        }

        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }

        ConsolePrinter.info("Setting " + dao.values().size() + " dataset value(s) for " + dsRef);

        console.getClient(SetDataSetValuesClient.class).execute(dao);

        String msg = "Set " + dao.values().size() + " dataset value(s) successfully";
        if (jsonMode) {
            ConsolePrinter.raw("{\"success\":true,\"message\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
        } else {
            ConsolePrinter.success(msg);
        }
    }
}

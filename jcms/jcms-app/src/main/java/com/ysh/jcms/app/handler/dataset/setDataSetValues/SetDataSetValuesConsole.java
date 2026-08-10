package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetDataSetValuesConsole extends CommandHandler {

    public SetDataSetValuesConsole() {
        super(CommandInfo.SET_DATASET_VALUES);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ds", "数据集引用，如 \"LD0/LLN0.dsAlarm\"", null), new Param("values", "数据值列表（空格分隔），如 \"aa bb cc\"", null),
                new Param("after", "起始引用（分页截取）", ""), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;

        if (!CmsConsole.requireParam(args, "ds", "Usage: set-dataset-values --ds <ref> --values \"<val1> <val2>...\""))
            return;
        if (!CmsConsole.requireParam(args, "values", "Usage: set-dataset-values --ds <ref> --values \"<val1> <val2>...\""))
            return;

        String dsRef = args.get("ds");
        String valuesStr = args.get("values");

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

        CmsConsole.outputMessage("Set " + dao.values().size() + " dataset value(s) successfully");
    }
}

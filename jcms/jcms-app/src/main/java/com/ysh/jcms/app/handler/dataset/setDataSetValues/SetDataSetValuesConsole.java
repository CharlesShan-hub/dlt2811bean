package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import java.util.Map;

public class SetDataSetValuesConsole extends CommandHandler<SetDataSetValuesDao, SetDataSetValuesClient> {

    public SetDataSetValuesConsole() {
        super(CommandInfo.SET_DATASET_VALUES);
        param("ds", "数据集引用，如 \"LD0/LLN0.dsAlarm\"", null);
        param("values", "数据值列表（空格分隔），如 \"aa bb cc\"", null);
        param("after", "起始引用（分页截取）", "");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
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

        ConsolePrinter.success("Set " + dao.values().size() + " dataset value(s) successfully");
    }
}

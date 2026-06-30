package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetDataSetValuesConsole implements CommandHandler {

    private static final String[] CHOICE_NAMES = {
        "error", "array", "structure", "boolean", "int8", "int16", "int32", "int64",
        "int8u", "int16u", "int32u", "int64u", "float32", "float64",
        "bit-string", "octet-string", "visible-string", "unicode-string",
        "utc-time", "binary-time", "quality", "dbpos", "tcmd", "check"
    };

    @Override
    public String name() { return "get-dataset-values"; }

    @Override
    public String description() { return "获取数据集值 (GetDataSetValues)。用法: get-dataset-values --ds <ref> [--after REF]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ds", "数据集引用，如 \"LD0/LLN0.dsAlarm\"", null),
            new Param("after", "起始引用（分页截取）", "")
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) {
            ConsolePrinter.error("Not connected. Type 'connect' first.");
            return;
        }

        String dsRef = args.get("ds");
        if (dsRef == null || dsRef.trim().isEmpty()) {
            ConsolePrinter.error("Missing --ds. Usage: get-dataset-values --ds <ref> [--after REF]");
            return;
        }

        GetDataSetValuesDao dao = new GetDataSetValuesDao()
            .datasetReference(dsRef.trim());

        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }

        ConsolePrinter.info("Fetching dataset values for " + dsRef);

        console.getClient(GetDataSetValuesClient.class).execute(dao);

        List<GetDataSetValuesClient.DataSetValue> values =
            console.getClient(GetDataSetValuesClient.class).getLastValues();

        if (values.isEmpty()) {
            ConsolePrinter.info("No dataset values returned");
            return;
        }

        ConsolePrinter.list("DataSet values (" + values.size() + " items)",
            new java.util.ArrayList<>(values),
            v -> {
                String typeName = v.choiceType >= 0 && v.choiceType < CHOICE_NAMES.length
                    ? CHOICE_NAMES[v.choiceType] : "?";
                return "[" + typeName + "] " + v.valueString;
            });
    }
}

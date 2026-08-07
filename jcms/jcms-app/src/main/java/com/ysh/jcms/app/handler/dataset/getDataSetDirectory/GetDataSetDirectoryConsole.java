package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetDataSetDirectoryConsole extends CommandHandler {

    public GetDataSetDirectoryConsole() {
        super(CommandInfo.GET_DATASET_DIR);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ds", "数据集引用，如 \"LD0/LLN0.dsAlarm\"", null), new Param("after", "起始引用（分页截取）", ""),
                new Param("auto-pull", "自动续拉分页（true/false）", "false"), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;

        if (!CmsConsole.requireParam(args, "ds", "Usage: get-dataset-dir --ds <ref> [--after REF]"))
            return;

        String dsRef = args.get("ds");
        GetDataSetDirectoryDao dao = new GetDataSetDirectoryDao().datasetReference(dsRef.trim());

        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }

        String autoPull = args.get("auto-pull");
        if ("true".equalsIgnoreCase(autoPull)) {
            dao.autoPull(true);
        }

        if (!CmsConsole.isJsonMode(args)) {
            ConsolePrinter.info("Fetching dataset directory for " + dsRef);
        }

        console.getClient(GetDataSetDirectoryClient.class).execute(dao);

        List<GetDataSetDirectoryClient.DirEntry> entries = console.getClient(GetDataSetDirectoryClient.class).getLastEntries();

        if (entries.isEmpty()) {
            if (CmsConsole.isJsonMode(args)) {
                CmsConsole.jsonArray("");
            } else {
                ConsolePrinter.info("No dataset directory entries");
            }
            return;
        }

        CmsConsole.outputList("DataSet directory (" + entries.size() + " items)", new java.util.ArrayList<>(entries),
                e -> "[" + e.fc + "]  " + e.reference, args);
    }
}

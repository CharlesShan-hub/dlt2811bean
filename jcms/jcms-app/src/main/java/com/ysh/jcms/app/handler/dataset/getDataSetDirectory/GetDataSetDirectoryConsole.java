package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.PaginationContext;

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
                new Param("auto-pull", "自动续拉分页（true/false）", "false"));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
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

        console.getClient(GetDataSetDirectoryClient.class).execute(dao);
        PaginationContext ctx = dao.paginationContext();

        @SuppressWarnings("unchecked")
        List<GetDataSetDirectoryClient.DirEntry> entries = (List<GetDataSetDirectoryClient.DirEntry>) ctx.getResult();
        if (entries == null) {
            entries = java.util.Collections.emptyList();
        }

        if (entries.isEmpty()) {
            ConsolePrinter.raw("No dataset directory entries");
            return;
        }

        CmsConsole.outputList("DataSet directory (" + entries.size() + " items)", new java.util.ArrayList<>(entries),
                e -> "[" + e.fc + "]  " + e.reference, args);
    }
}

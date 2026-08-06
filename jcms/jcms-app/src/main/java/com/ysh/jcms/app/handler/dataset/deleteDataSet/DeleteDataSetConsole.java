package com.ysh.jcms.app.handler.dataset.deleteDataSet;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DeleteDataSetConsole extends CommandHandler {

    public DeleteDataSetConsole() {
        super(CommandInfo.DELETE_DATASET);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ds", "数据集引用，如 \"LD0/LLN0.myDs\"", null), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;

        if (!CmsConsole.requireParam(args, "ds", "Usage: delete-dataset --ds <ref>"))
            return;

        String dsRef = args.get("ds");
        DeleteDataSetDao dao = new DeleteDataSetDao().datasetReference(dsRef.trim());

        ConsolePrinter.info("Deleting dataset " + dsRef);

        console.getClient(DeleteDataSetClient.class).execute(dao);

        CmsConsole.outputMessage("Deleted dataset " + dsRef + " successfully", args);
    }
}

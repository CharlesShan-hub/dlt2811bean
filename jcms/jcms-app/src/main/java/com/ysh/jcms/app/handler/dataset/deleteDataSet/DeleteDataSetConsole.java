package com.ysh.jcms.app.handler.dataset.deleteDataSet;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DeleteDataSetConsole implements CommandHandler {

    @Override
    public String name() { return "delete-dataset"; }

    @Override
    public String description() { return "删除数据集 (DeleteDataSet)。用法: delete-dataset --ds <ref>"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ds", "数据集引用，如 \"LD0/LLN0.myDs\"", null)
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
            ConsolePrinter.error("Missing --ds. Usage: delete-dataset --ds <ref>");
            return;
        }

        DeleteDataSetDao dao = new DeleteDataSetDao()
            .datasetReference(dsRef.trim());

        ConsolePrinter.info("Deleting dataset " + dsRef);

        console.getClient(DeleteDataSetClient.class).execute(dao);

        ConsolePrinter.success("Deleted dataset " + dsRef + " successfully");
    }
}

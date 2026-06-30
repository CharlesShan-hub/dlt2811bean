package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetDataDirectoryConsole implements CommandHandler {

    @Override
    public String name() { return "data-dir"; }

    @Override
    public String description() { return "获取数据目录 (GetDataDirectory)。用法: data-dir --ref <ref> [--after REF]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ref", "数据引用，如 LD0/LLN0 或 LD0/LLN0.Mod", null),
            new Param("after", "起始引用（分页截取）", "")
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
            ConsolePrinter.error("Missing --ref. Usage: data-dir --ref <ref> [--after REF]");
            return;
        }

        GetDataDirectoryDao dao = new GetDataDirectoryDao()
            .dataReference(ref.trim());

        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }

        ConsolePrinter.info("Fetching data directory for " + ref);

        console.getClient(GetDataDirectoryClient.class).execute(dao);

        List<GetDataDirectoryClient.DirEntry> entries =
            console.getClient(GetDataDirectoryClient.class).getLastEntries();

        if (entries.isEmpty()) {
            ConsolePrinter.info("No data directory entries");
            return;
        }

        ConsolePrinter.list("Data directory (" + entries.size() + " items)",
            new java.util.ArrayList<>(entries),
            e -> {
                if (e.fc != null) return "[" + e.fc + "]  " + e.reference;
                return e.reference;
            });
    }
}

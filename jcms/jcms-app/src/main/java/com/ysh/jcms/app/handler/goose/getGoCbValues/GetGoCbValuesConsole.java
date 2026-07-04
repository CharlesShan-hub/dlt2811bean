package com.ysh.jcms.app.handler.goose.getGoCbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetGoCbValuesConsole implements CommandHandler {

    @Override
    public String name() { return "get-gocb-vals"; }

    @Override
    public String description() { return "获取 GOOSE 控制块值 (GetGoCBValues)。用法: get-gocb-vals --refs \"<ref1> <ref2>...\""; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("refs", "GoCB 引用列表（空格分隔），如 \"LD0/LLN0.gocb1\"", null)
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) {
            ConsolePrinter.error("Not connected. Type 'connect' first.");
            return;
        }

        String refsStr = args.get("refs");
        if (refsStr == null || refsStr.trim().isEmpty()) {
            ConsolePrinter.error("Missing --refs. Usage: get-gocb-vals --refs \"<ref1> <ref2>...\"");
            return;
        }

        String[] refs = refsStr.trim().split("\\s+");
        GetGoCbValuesDao dao = new GetGoCbValuesDao();
        for (String ref : refs) {
            if (!ref.isEmpty()) dao.addRef(ref.trim());
        }

        ConsolePrinter.info("Fetching GoCB values for " + dao.refs().size() + " reference(s)");

        console.getClient(GetGoCbValuesClient.class).execute(dao);

        List<GetGoCbValuesClient.GoCbEntry> entries =
            console.getClient(GetGoCbValuesClient.class).getLastEntries();

        if (entries.isEmpty()) {
            ConsolePrinter.info("No GoCB values returned");
            return;
        }

        for (int i = 0; i < entries.size(); i++) {
            String ref = i < refs.length ? refs[i] : "#" + i;
            ConsolePrinter.info("  [" + ref + "] " + entries.get(i).desc);
        }
    }
}

package com.ysh.jcms.app.handler.log.getLogStatusValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetLogStatusValuesConsole implements CommandHandler {

    @Override
    public String name() { return "get-log-status"; }

    @Override
    public String description() { return "获取日志状态值 (GetLogStatusValues)。用法: get-log-status --refs \"<ref1> <ref2>...\""; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("refs", "LCB 引用列表（空格分隔），如 \"LD0/LLN0.lcb1\"", null)
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
            ConsolePrinter.error("Missing --refs. Usage: get-log-status --refs \"<ref1> <ref2>...\"");
            return;
        }

        String[] refs = refsStr.trim().split("\\s+");
        GetLogStatusValuesDao dao = new GetLogStatusValuesDao();
        for (String ref : refs) {
            if (!ref.isEmpty()) dao.addRef(ref.trim());
        }

        ConsolePrinter.info("Fetching log status for " + dao.refs().size() + " reference(s)");

        console.getClient(GetLogStatusValuesClient.class).execute(dao);

        List<GetLogStatusValuesClient.LogStatusEntry> entries =
            console.getClient(GetLogStatusValuesClient.class).getLastEntries();

        if (entries.isEmpty()) {
            ConsolePrinter.info("No log status values returned");
            return;
        }

        for (int i = 0; i < entries.size(); i++) {
            String ref = i < refs.length ? refs[i] : "#" + i;
            ConsolePrinter.info("  [" + ref + "] " + entries.get(i).desc);
        }
    }
}

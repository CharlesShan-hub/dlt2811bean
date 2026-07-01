package com.ysh.jcms.app.handler.report.getUrcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetUrcbValuesConsole implements CommandHandler {

    @Override
    public String name() { return "get-urcb-vals"; }

    @Override
    public String description() { return "获取非缓存报告控制块值 (GetURCBValues)。用法: get-urcb-vals --refs \"<ref1> <ref2>...\""; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("refs", "URCB 引用列表（空格分隔），如 \"LD0/LLN0.urcbAin\"", null)
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
            ConsolePrinter.error("Missing --refs. Usage: get-urcb-vals --refs \"<ref1> <ref2>...\"");
            return;
        }

        String[] refs = refsStr.trim().split("\\s+");
        GetUrcbValuesDao dao = new GetUrcbValuesDao();
        for (String ref : refs) {
            if (!ref.isEmpty()) dao.addRef(ref.trim());
        }

        ConsolePrinter.info("Fetching URCB values for " + dao.refs().size() + " reference(s)");
        console.getClient(GetUrcbValuesClient.class).execute(dao);

        List<GetUrcbValuesClient.UrcbEntry> entries =
            console.getClient(GetUrcbValuesClient.class).getLastEntries();

        if (entries.isEmpty()) {
            ConsolePrinter.info("No URCB values returned");
            return;
        }

        List<Object> display = new java.util.ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            String ref = i < refs.length ? refs[i] : "#" + i;
            display.add(ref + "  " + entries.get(i).desc);
        }
        ConsolePrinter.list("URCB values (" + entries.size() + " items)", display, Object::toString);
    }
}

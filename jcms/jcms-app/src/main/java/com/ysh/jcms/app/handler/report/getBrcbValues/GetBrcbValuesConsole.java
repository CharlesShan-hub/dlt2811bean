package com.ysh.jcms.app.handler.report.getBrcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetBrcbValuesConsole implements CommandHandler {

    @Override
    public String name() { return "get-brcb-vals"; }

    @Override
    public String description() { return "获取缓存报告控制块值 (GetBRCBValues)。用法: get-brcb-vals --refs \"<ref1> <ref2>...\""; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("refs", "BRCB 引用列表（空格分隔），如 \"LD0/LLN0.brcbAlarm\"", null)
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
            ConsolePrinter.error("Missing --refs. Usage: get-brcb-vals --refs \"<ref1> <ref2>...\"");
            return;
        }

        String[] refs = refsStr.trim().split("\\s+");
        GetBrcbValuesDao dao = new GetBrcbValuesDao();
        for (String ref : refs) {
            if (!ref.isEmpty()) dao.addRef(ref.trim());
        }

        ConsolePrinter.info("Fetching BRCB values for " + dao.refs().size() + " reference(s)");

        console.getClient(GetBrcbValuesClient.class).execute(dao);

        List<GetBrcbValuesClient.BrcbEntry> entries =
            console.getClient(GetBrcbValuesClient.class).getLastEntries();

        if (entries.isEmpty()) {
            ConsolePrinter.info("No BRCB values returned");
            return;
        }

        List<Object> display = new java.util.ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            String ref = i < refs.length ? refs[i] : "#" + i;
            display.add(ref + "  " + entries.get(i).desc);
        }
        ConsolePrinter.list("BRCB values (" + entries.size() + " items)", display, Object::toString);
    }
}

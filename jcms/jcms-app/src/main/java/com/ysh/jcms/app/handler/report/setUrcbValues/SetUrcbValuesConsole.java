package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetUrcbValuesConsole implements CommandHandler {

    @Override
    public String name() { return "set-urcb-vals"; }

    @Override
    public String description() { return "设置非缓存报告控制块值 (SetURCBValues)。用法: set-urcb-vals --ref <urcbRef>"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ref", "URCB 引用，如 LD0/LLN0.urcbAin", null)
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
            ConsolePrinter.error("Missing --ref. Usage: set-urcb-vals --ref <urcbRef>");
            return;
        }

        SetUrcbValuesDao dao = new SetUrcbValuesDao().ref(ref.trim());
        ConsolePrinter.info("Setting URCB values: ref=" + ref);
        console.getClient(SetUrcbValuesClient.class).execute(dao);
        ConsolePrinter.success("URCB values set for " + ref);
    }
}

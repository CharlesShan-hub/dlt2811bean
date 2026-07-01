package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetBrcbValuesConsole implements CommandHandler {

    @Override
    public String name() { return "set-brcb-vals"; }

    @Override
    public String description() { return "设置缓存报告控制块值 (SetBRCBValues)。用法: set-brcb-vals --ref <brcbRef>"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ref", "BRCB 引用，如 LD0/LLN0.brcbAlarm", null)
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
            ConsolePrinter.error("Missing --ref. Usage: set-brcb-vals --ref <brcbRef>");
            return;
        }

        SetBrcbValuesDao dao = new SetBrcbValuesDao().ref(ref.trim());
        ConsolePrinter.info("Setting BRCB values: ref=" + ref);
        console.getClient(SetBrcbValuesClient.class).execute(dao);
        ConsolePrinter.success("BRCB values set for " + ref);
    }
}

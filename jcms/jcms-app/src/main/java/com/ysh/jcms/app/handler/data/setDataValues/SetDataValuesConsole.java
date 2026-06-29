package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetDataValuesConsole implements CommandHandler {

    @Override
    public String name() { return "set-data-values"; }

    @Override
    public String description() { return "设置数据值 (SetDataValues) —— 用法: set-data-values <ref1>=<value1> <ref2>=<value2> ..."; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("pairs", "数据引用=值 对（空格分隔），如 LD0/LLN0.Mod.stVal=true LD0/LLN0.Beh.stVal=1", null)
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) {
            ConsolePrinter.error("Not connected. Type 'connect' first.");
            return;
        }

        String pairsStr = args.get("pairs");
        if (pairsStr == null || pairsStr.trim().isEmpty()) {
            ConsolePrinter.error("Missing pairs. Usage: set-data-values <ref1>=<value1> <ref2>=<value2> ...");
            return;
        }

        String[] tokens = pairsStr.trim().split("\\s+");
        SetDataValuesDao dao = new SetDataValuesDao();

        for (String token : tokens) {
            if (token.isEmpty()) continue;
            int eqIdx = token.indexOf('=');
            if (eqIdx <= 0) {
                ConsolePrinter.error("Invalid pair: " + token + " (expected ref=value)");
                return;
            }
            String ref = token.substring(0, eqIdx);
            String value = token.substring(eqIdx + 1);
            dao.addEntry(ref, value);
        }

        ConsolePrinter.info("Setting " + dao.entries().size() + " data value(s)...");

        console.getClient(SetDataValuesClient.class).execute(dao);

        ConsolePrinter.success("Set " + dao.entries().size() + " data value(s) successfully");
    }
}

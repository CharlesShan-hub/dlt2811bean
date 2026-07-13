package com.ysh.jcms.app.handler.msv.getMsvcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetMsvcbValuesConsole implements CommandHandler {

    @Override
    public String name() {
        return "get-msvcb-vals";
    }

    @Override
    public String description() {
        return "读多播采样值控制块值 (GetMSVCBValues)。用法: get-msvcb-vals --refs \"<ref1> <ref2>...\" [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("refs", "MSVCB引用列表，如 \"LD0/SV1.msvcb01 LD0/SV1.msvcb02\"", null),
                new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        if (!CmsConsole.requireParam(args, "refs", "Usage: get-msvcb-vals --refs \"<ref1> <ref2>...\""))
            return;

        List<String> refs = Arrays.asList(args.get("refs").trim().split("\\s+"));
        ConsolePrinter.info("Fetching MSVCB values for " + refs.size() + " reference(s)");
        console.getClient(GetMsvcbValuesClient.class).execute(refs);
        CmsConsole.outputMessage("MSVCB values fetched for " + refs.size() + " reference(s)", args);
    }
}

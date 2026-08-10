package com.ysh.jcms.app.handler.msv.getMsvcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetMsvcbValuesConsole extends CommandHandler {

    public GetMsvcbValuesConsole() {
        super(CommandInfo.GET_MSVCB_VALS);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("refs", "MSVCB引用列表，如 \"LD0/SV1.msvcb01 LD0/SV1.msvcb02\"", null));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;
        if (!CmsConsole.requireParam(args, "refs", "Usage: get-msvcb-vals --refs \"<ref1> <ref2>...\""))
            return;

        List<String> refs = Arrays.asList(args.get("refs").trim().split("\\s+"));
        ConsolePrinter.info("Fetching MSVCB values for " + refs.size() + " reference(s)");
        GetMsvcbValuesDao dao = new GetMsvcbValuesDao().refs(refs);
        console.getClient(GetMsvcbValuesClient.class).execute(dao);
        ConsolePrinter.success("MSVCB values fetched for " + refs.size() + " reference(s)");
    }
}

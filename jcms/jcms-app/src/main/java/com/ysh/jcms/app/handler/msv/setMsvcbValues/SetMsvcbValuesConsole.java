package com.ysh.jcms.app.handler.msv.setMsvcbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetMsvcbValuesConsole extends CommandHandler {

    public SetMsvcbValuesConsole() {
        super(CommandInfo.SET_MSVCB_VALS);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "MSVCB引用", null), new Param("sv-ena", "启用采样值发送", ""), new Param("msv-id", "MSV标识", ""),
                new Param("dat-set", "数据集引用", ""), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        if (!CmsConsole.requireParam(args, "ref", "Usage: set-msvcb-vals --ref <ref>"))
            return;

        String ref = args.get("ref").trim();
        String svEna = args.get("sv-ena");
        String msvId = args.get("msv-id");
        String datSet = args.get("dat-set");

        ConsolePrinter.info("Setting MSVCB values: ref=" + ref);
        SetMsvcbValuesDao dao = new SetMsvcbValuesDao().ref(ref).svEna(svEna).msvId(msvId).datSet(datSet);
        console.getClient(SetMsvcbValuesClient.class).execute(dao);
        CmsConsole.outputMessage("MSVCB values set for " + ref, args);
    }
}

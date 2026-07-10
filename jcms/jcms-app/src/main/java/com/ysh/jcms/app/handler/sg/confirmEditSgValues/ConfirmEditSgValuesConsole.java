package com.ysh.jcms.app.handler.sg.confirmEditSgValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConfirmEditSgValuesConsole implements CommandHandler {

    @Override
    public String name() {
        return "confirm-edit-sg";
    }

    @Override
    public String description() {
        return "确认编辑定值组值生效 (ConfirmEditSGValues)。" + "用法: confirm-edit-sg --ref <sgcbRef> [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "SGCB 引用，如 PROT/DeZonePTOC1.SG1", null), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;

        if (!CmsConsole.requireParam(args, "ref", "Usage: confirm-edit-sg --ref <sgcbRef>"))
            return;

        String ref = args.get("ref");
        ConfirmEditSgValuesDao dao = new ConfirmEditSgValuesDao().sgcbReference(ref.trim());

        ConsolePrinter.info("Confirming edit SG values: ref=" + ref);

        console.getClient(ConfirmEditSgValuesClient.class).execute(dao);

        CmsConsole.outputMessage("Edit SG values confirmed for " + ref, args);
    }
}

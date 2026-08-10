package com.ysh.jcms.app.handler.sg.selectEditSg;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SelectEditSgConsole extends CommandHandler {

    public SelectEditSgConsole() {
        super(CommandInfo.SELECT_EDIT_SG);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "SGCB 引用，如 LD0/LLN0.SG1", null), new Param("num", "定值组号（1~numOfSG）", null),
                new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        if (!CmsConsole.requireParam(args, "ref", "Usage: select-edit-sg --ref <sgcbRef> --num <groupNumber>"))
            return;
        if (!CmsConsole.requireParam(args, "num", "Usage: select-edit-sg --ref <sgcbRef> --num <groupNumber>"))
            return;

        String ref = args.get("ref");
        String numStr = args.get("num");
        int sgNum;
        try {
            sgNum = Integer.parseInt(numStr.trim());
        } catch (NumberFormatException e) {
            if (CmsConsole.isJsonMode(args)) {
                CmsConsole.jsonError("Invalid group number: " + numStr);
            } else {
                ConsolePrinter.error("Invalid group number: " + numStr);
            }
            return;
        }

        SelectEditSgDao dao = new SelectEditSgDao().sgcbReference(ref.trim()).settingGroupNumber(sgNum);

        ConsolePrinter.info("Selecting edit SG: ref=" + ref + " num=" + sgNum);

        console.getClient(SelectEditSgClient.class).execute(dao);

        ConsolePrinter.success("Edit SG set to " + sgNum + " for " + ref);
    }
}

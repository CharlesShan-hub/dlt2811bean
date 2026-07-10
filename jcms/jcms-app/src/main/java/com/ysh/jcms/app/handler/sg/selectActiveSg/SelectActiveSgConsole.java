package com.ysh.jcms.app.handler.sg.selectActiveSg;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SelectActiveSgConsole implements CommandHandler {

    @Override
    public String name() {
        return "select-active-sg";
    }

    @Override
    public String description() {
        return "选择激活定值组 (SelectActiveSG)。用法: select-active-sg --ref <sgcbRef> --num <groupNumber> [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "SGCB 引用，如 LD0/LLN0.SG1", null), new Param("num", "定值组号（1~numOfSG）", null),
                new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;

        if (!CmsConsole.requireParam(args, "ref", "Usage: select-active-sg --ref <sgcbRef> --num <groupNumber>"))
            return;
        if (!CmsConsole.requireParam(args, "num", "Usage: select-active-sg --ref <sgcbRef> --num <groupNumber>"))
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

        SelectActiveSgDao dao = new SelectActiveSgDao().sgcbReference(ref.trim()).settingGroupNumber(sgNum);

        ConsolePrinter.info("Selecting active SG: ref=" + ref + " num=" + sgNum);

        console.getClient(SelectActiveSgClient.class).execute(dao);

        CmsConsole.outputMessage("Active SG set to " + sgNum + " for " + ref, args);
    }
}

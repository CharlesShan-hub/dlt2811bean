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
    public String name() { return "select-active-sg"; }

    @Override
    public String description() { return "选择激活定值组 (SelectActiveSG)。用法: select-active-sg --ref <sgcbRef> --num <groupNumber>"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ref", "SGCB 引用，如 LD0/LLN0.SG1", null),
            new Param("num", "定值组号（1~numOfSG）", null)
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
            ConsolePrinter.error("Missing --ref. Usage: select-active-sg --ref <sgcbRef> --num <groupNumber>");
            return;
        }

        String numStr = args.get("num");
        if (numStr == null || numStr.trim().isEmpty()) {
            ConsolePrinter.error("Missing --num. Usage: select-active-sg --ref <sgcbRef> --num <groupNumber>");
            return;
        }

        int sgNum;
        try {
            sgNum = Integer.parseInt(numStr.trim());
        } catch (NumberFormatException e) {
            ConsolePrinter.error("Invalid group number: " + numStr);
            return;
        }

        SelectActiveSgDao dao = new SelectActiveSgDao()
            .sgcbReference(ref.trim())
            .settingGroupNumber(sgNum);

        ConsolePrinter.info("Selecting active SG: ref=" + ref + " num=" + sgNum);

        console.getClient(SelectActiveSgClient.class).execute(dao);

        ConsolePrinter.success("Active SG set to " + sgNum + " for " + ref);
    }
}

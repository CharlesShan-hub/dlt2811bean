package com.ysh.jcms.app.handler.sg.selectActiveSg;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SelectActiveSgConsole implements CommandHandler {

    @Override
    public String name() { return "select-active-sg"; }

    @Override
    public String description() { return "选择激活定值组 (SelectActiveSG)。用法: select-active-sg --ref <sgcbRef> --num <groupNumber> [--json]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("ref", "SGCB 引用，如 LD0/LLN0.SG1", null),
            new Param("num", "定值组号（1~numOfSG）", null),
            new Param("json", "JSON 格式输出", "")
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = "true".equals(args.get("json"));
        if (!console.isConnected()) {
            String msg = "Not connected. Type 'connect' first.";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }

        String ref = args.get("ref");
        if (ref == null || ref.trim().isEmpty()) {
            String msg = "Missing --ref. Usage: select-active-sg --ref <sgcbRef> --num <groupNumber>";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }

        String numStr = args.get("num");
        if (numStr == null || numStr.trim().isEmpty()) {
            String msg = "Missing --num. Usage: select-active-sg --ref <sgcbRef> --num <groupNumber>";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }

        int sgNum;
        try {
            sgNum = Integer.parseInt(numStr.trim());
        } catch (NumberFormatException e) {
            String msg = "Invalid group number: " + numStr;
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }

        SelectActiveSgDao dao = new SelectActiveSgDao()
            .sgcbReference(ref.trim())
            .settingGroupNumber(sgNum);

        ConsolePrinter.info("Selecting active SG: ref=" + ref + " num=" + sgNum);

        console.getClient(SelectActiveSgClient.class).execute(dao);

        String msg = "Active SG set to " + sgNum + " for " + ref;
        if (jsonMode) {
            ConsolePrinter.raw("{\"success\":true,\"message\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
        } else {
            ConsolePrinter.success(msg);
        }
    }
}

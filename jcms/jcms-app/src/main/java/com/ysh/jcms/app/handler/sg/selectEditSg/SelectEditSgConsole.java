package com.ysh.jcms.app.handler.sg.selectEditSg;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SelectEditSgConsole implements CommandHandler {

    @Override
    public String name() { return "select-edit-sg"; }

    @Override
    public String description() { return "选择编辑定值组 (SelectEditSG)。用法: select-edit-sg --ref <sgcbRef> --num <groupNumber> [--json]"; }

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
            String msg = "Missing --ref. Usage: select-edit-sg --ref <sgcbRef> --num <groupNumber>";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }

        String numStr = args.get("num");
        if (numStr == null || numStr.trim().isEmpty()) {
            String msg = "Missing --num. Usage: select-edit-sg --ref <sgcbRef> --num <groupNumber>";
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

        SelectEditSgDao dao = new SelectEditSgDao()
            .sgcbReference(ref.trim())
            .settingGroupNumber(sgNum);

        ConsolePrinter.info("Selecting edit SG: ref=" + ref + " num=" + sgNum);

        console.getClient(SelectEditSgClient.class).execute(dao);

        String msg = "Edit SG set to " + sgNum + " for " + ref;
        if (jsonMode) {
            ConsolePrinter.raw("{\"success\":true,\"message\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
        } else {
            ConsolePrinter.success(msg);
        }
    }
}

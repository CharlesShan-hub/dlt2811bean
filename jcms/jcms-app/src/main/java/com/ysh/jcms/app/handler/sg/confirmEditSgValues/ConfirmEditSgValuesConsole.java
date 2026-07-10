package com.ysh.jcms.app.handler.sg.confirmEditSgValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

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
            String msg = "Missing --ref. Usage: confirm-edit-sg --ref <sgcbRef>";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }

        ConfirmEditSgValuesDao dao = new ConfirmEditSgValuesDao().sgcbReference(ref.trim());

        ConsolePrinter.info("Confirming edit SG values: ref=" + ref);

        console.getClient(ConfirmEditSgValuesClient.class).execute(dao);

        String msg = "Edit SG values confirmed for " + ref;
        if (jsonMode) {
            ConsolePrinter.raw("{\"success\":true,\"message\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
        } else {
            ConsolePrinter.success(msg);
        }
    }
}

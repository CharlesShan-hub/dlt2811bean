package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AssociateConsole implements CommandHandler {

    @Override
    public String name() {
        return "associate";
    }

    @Override
    public String description() {
        return "建立关联 (Associate)。用法: associate --ap <IED/AP> [--secure] [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ap", "ServerAccessPoint 引用（如 C_B5041X/S1）", ""), new Param("secure", "加密关联（不传值，出现即启用）", ""),
                new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = "true".equals(args.get("json"));
        if (!console.isClientConnected()) {
            String msg = "Not connected. Use 'connect' first.";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }
        if (console.isConnected()) {
            String msg = "Already associated. Use 'release' or 'disconnect' first.";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }

        String sapRef = args.get("ap");
        if (sapRef == null || sapRef.isEmpty()) {
            String msg = "--ap is required";
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":false,\"error\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
            } else {
                ConsolePrinter.error(msg);
            }
            return;
        }

        boolean secure = "true".equals(args.get("secure"));

        console.getClient(AssociateClient.class).execute(new AssociateClientDao().sapRef(sapRef).secure(secure));

        String msg = "Associated: " + sapRef + (secure ? " (secure)" : "");
        if (jsonMode) {
            ConsolePrinter.raw("{\"success\":true,\"message\":\"" + CmsFormatUtil.escapeJson(msg) + "\"}");
        } else {
            ConsolePrinter.success(msg);
        }
    }
}

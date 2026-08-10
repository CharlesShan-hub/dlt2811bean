package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AssociateConsole extends CommandHandler {

    public AssociateConsole() {
        super(CommandInfo.ASSOCIATE);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ap", "ServerAccessPoint 引用（如 C_B5041X/S1）", ""), new Param("secure", "加密关联（不传值，出现即启用）", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.clientConnected()) {
            ConsolePrinter.error("Not connected. Use 'connect' first.");
            return;
        }
        if (console.connected()) {
            ConsolePrinter.error("Already associated. Use 'release' or 'disconnect' first.");
            return;
        }

        String sapRef = args.get("ap");
        if (sapRef != null && sapRef.isEmpty())
            sapRef = null;

        boolean secure = "true".equals(args.get("secure"));

        console.getClient(AssociateClient.class).execute(new AssociateClientDao().sapRef(sapRef).secure(secure));

        String msg = sapRef != null ? "Associated: " + sapRef + (secure ? " (secure)" : "") : "Associated: (default access point)";
        ConsolePrinter.success(msg);
    }
}

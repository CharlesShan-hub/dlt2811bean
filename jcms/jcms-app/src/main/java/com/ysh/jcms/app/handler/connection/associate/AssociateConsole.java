package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AssociateConsole implements CommandHandler {

    @Override
    public String name() { return "associate"; }

    @Override
    public String description() { return "建立关联 (Associate)"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("sapRef", "ServerAccessPoint 引用")
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isClientConnected()) {
            ConsolePrinter.error("Not connected. Use 'connect' first.");
            return;
        }
        if (console.isConnected()) {
            ConsolePrinter.error("Already associated. Use 'release' or 'disconnect' first.");
            return;
        }

        String sapRef = args.get("sapRef");
        if (sapRef == null || sapRef.isEmpty()) {
            ConsolePrinter.error("sapRef is required");
            return;
        }

        console.getClient(AssociateClient.class)
            .execute(new AssociateClientDao().sapRef(sapRef).secure(false));

        ConsolePrinter.success("Associated: " + sapRef);
    }
}

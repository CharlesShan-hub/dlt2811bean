package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DisconnectHandler implements CommandHandler {

    @Override
    public String name() { return "disconnect"; }
    @Override
    public String description() { return "断开当前连接"; }
    @Override
    public List<Param> params() { return Collections.emptyList(); }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) {
        if (!console.isConnected()) { ConsolePrinter.info("Not connected."); return; }
        console.close();
        ConsolePrinter.success("Disconnected.");
    }
}

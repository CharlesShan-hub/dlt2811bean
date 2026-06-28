package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ReleaseConsole implements CommandHandler {

    @Override
    public String name() { return "release"; }

    @Override
    public String description() { return "释放关联 (Release)"; }

    @Override
    public List<Param> params() {
        return Collections.emptyList();
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) { ConsolePrinter.error("Not connected."); return; }
        console.getClient(com.ysh.jcms.app.handler.connection.release.ReleaseClient.class).execute();
        ConsolePrinter.success("Released.");
    }
}

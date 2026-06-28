package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SvrDirConsole implements CommandHandler {

    @Override
    public String name() { return "server-dir"; }

    @Override
    public String description() { return "获取逻辑设备目录（referenceAfter 从指定引用开始截取）"; }

    @Override
    public List<Param> params() {
        return Collections.singletonList(
            new Param("referenceAfter", "起始引用（分页截取，不传则从头开始）", null)
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        String msg = check(console);
        if (msg != null) { ConsolePrinter.error(msg); return; }
        SvrDirDao dao = new SvrDirDao();
        String after = args.get("referenceAfter");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }
        console.getClient(SvrDirClient.class)
            .execute(dao);
        ConsolePrinter.list("Logical Devices",
            new ArrayList<>(console.getContentManager().getLdNames()),
            s -> s);
    }

    static String check(CmsConsole console) {
        if (!console.isConnected()) return "Not connected. Type 'connect' first.";
        if (console.getClient(SvrDirClient.class) == null) return "SvrDirClient not registered.";
        return null;
    }
}

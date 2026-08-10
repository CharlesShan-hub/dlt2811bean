package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;
import java.util.Map;

public class SvrDirConsole extends CommandHandler {

    public SvrDirConsole() {
        super(CommandInfo.SERVER_DIR);
    }

    @Override
    public List<Param> params() {
        return java.util.Arrays.asList(new Param("after", "起始引用（分页截取，不传则从头开始）", ""), new Param("auto-pull", "自动续拉分页（true/false）", "false"));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;
        if (console.getClient(SvrDirClient.class) == null) {
            ConsolePrinter.raw("{\"success\":false,\"error\":\"SvrDirClient not registered.\"}");
            return;
        }
        SvrDirDao dao = new SvrDirDao();
        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }
        String autoPull = args.get("auto-pull");
        if ("true".equalsIgnoreCase(autoPull)) {
            dao.autoPull(true);
        }
        console.getClient(SvrDirClient.class).execute(dao);
        ConsolePrinter.outputJson(dao.result());
    }
}

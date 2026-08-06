package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SvrDirConsole extends CommandHandler {

    public SvrDirConsole() {
        super(CommandInfo.SERVER_DIR);
    }

    @Override
    public List<Param> params() {
        return java.util.Arrays.asList(new Param("after", "起始引用（分页截取，不传则从头开始）", ""), new Param("json", "JSON 格式输出（不传则输出人类可读文本）", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        if (console.getClient(SvrDirClient.class) == null) {
            if (CmsConsole.isJsonMode(args)) {
                CmsConsole.jsonError("SvrDirClient not registered.");
            } else {
                ConsolePrinter.error("SvrDirClient not registered.");
            }
            return;
        }
        SvrDirDao dao = new SvrDirDao();
        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }
        console.getClient(SvrDirClient.class).execute(dao);
        List<String> ldNames = new ArrayList<>(console.getContentManager().getLdNames());
        CmsConsole.outputList("Logical Devices", ldNames, s -> s, args);
    }
}

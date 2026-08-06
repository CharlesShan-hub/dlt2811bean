package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReleaseConsole implements CommandHandler {

    @Override
    public String name() {
        return "release";
    }

    @Override
    public String description() {
        return "释放关联 (Release) [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        console.getClient(com.ysh.jcms.app.handler.connection.release.ReleaseClient.class).execute(new ReleaseDao());
        CmsConsole.outputMessage("Released.", args);
    }
}

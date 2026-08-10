package com.ysh.jcms.app.handler.console;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.BaseDao;
import java.util.Map;

public class ClearHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public ClearHandler() {
        super(CommandInfo.CLEAR);
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        // ANSI escape: clear screen + home cursor
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }
}

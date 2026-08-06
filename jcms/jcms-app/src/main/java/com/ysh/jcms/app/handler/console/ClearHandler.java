package com.ysh.jcms.app.handler.console;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ClearHandler extends CommandHandler {

    public ClearHandler() {
        super(CommandInfo.CLEAR);
    }

    @Override
    public List<Param> params() {
        return Collections.emptyList();
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        // ANSI escape: clear screen + home cursor
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }
}

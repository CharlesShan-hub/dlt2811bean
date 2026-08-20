package com.ysh.jcms.app.handler.console.clear;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.util.CmsPrinter;
import java.util.Map;

public class ClearHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public ClearHandler() {
        super(CommandInfo.CLEAR);
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        CmsPrinter.clear();
    }
}

package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.BaseDao;
import java.util.Map;

public class DisconnectHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public DisconnectHandler() {
        super(CommandInfo.DISCONNECT);
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) {
        // disconnect 是传输层操作：只要 TCP 连接就能断开，无需已关联
        if (!console.requireTcpConnected(args)) {
            return;
        }
        console.close();
        ConsolePrinter.success("Disconnected.");
    }
}

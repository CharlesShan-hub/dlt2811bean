package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.transport.app.CmsClient;

public class ConnectHandler extends AbstractConnectHandler {

    public ConnectHandler(CliContext ctx) {
        super(ctx);
    }

    protected String commandName() {
        return "connect";
    }

    protected String commandDescription() {
        return "连接服务器（自动协商与关联）";
    }

    protected int defaultPort() {
        return config().getServer().getPort();
    }

    protected void doConnect(CmsClient client, String host, int port) throws Exception {
        client.connect(host, port);
    }
}

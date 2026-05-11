package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class ExitHandler implements CommandHandler {

    private final CliContext ctx;

    public ExitHandler(CliContext ctx) {
        this.ctx = ctx;
    }

    public String getName() { return "exit"; }
    public String getDescription() { return "退出程序"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) {
        if (client.isConnected()) {
            try { client.release(); } catch (Exception ignored) {}
            client.close();
        }
        System.out.println("Bye!");
        System.exit(0);
    }
}

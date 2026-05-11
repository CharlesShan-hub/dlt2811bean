package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class StatusHandler implements CommandHandler {

    private final CliContext ctx;

    public StatusHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "status"; }
    public String getDescription() { return "查看当前连接和关联状态"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) {
        boolean connected = client.isConnected();
        byte[] assocId = client.getAssociationId();
        System.out.println("  Connected: " + (connected ? CmsColor.green("YES") : CmsColor.red("NO")));
        if (connected) {
            System.out.println("  TLS: " + (client.isTlsEnabled() ? CmsColor.green("YES") : CmsColor.gray("NO")));
        }
        System.out.println("  Associated: " + (assocId != null ? CmsColor.green("YES") + " (id=" + ctx.bytesToHex(assocId, 8) + "...)" : CmsColor.red("NO")));
        if (assocId != null) {
            System.out.println("  Secure: " + (client.isSecurityEnabled() ? CmsColor.green("YES") : CmsColor.gray("NO")));
        }
    }
}

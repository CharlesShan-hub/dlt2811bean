package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetServerDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsObjectClass;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class ServerDirHandler implements CommandHandler {

    private final CliContext ctx;

    public ServerDirHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "server-dir"; }
    public String getDescription() { return "读服务器目录"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println("  Not connected. Type 'connect' first.");
            return;
        }
        CmsGetServerDirectory reqAsdu = new CmsGetServerDirectory(MessageType.REQUEST)
                .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE));
        CmsApdu response = ctx.sendAndPrint(client, reqAsdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Request failed"); return;
        }
        CmsGetServerDirectory resAsdu = (CmsGetServerDirectory) response.getAsdu();
        if (resAsdu.reference().isEmpty()) {
            System.out.println(CmsColor.gray("  无数据"));
        } else {
            System.out.println("  Logical devices:");
            for (int i = 0; i < resAsdu.reference().size(); i++) {
                System.out.println("    [" + i + "] " + resAsdu.reference().get(i).get());
            }
        }
    }
}

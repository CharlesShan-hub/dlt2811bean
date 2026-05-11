package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDirectory;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class IfaceDirHandler implements CommandHandler {

    private final CliContext ctx;

    public IfaceDirHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "iface-dir"; }
    public String getDescription() { return "获取RPC接口目录 (IF1, IF2)"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println("  Not connected. Type 'connect' first.");
            return;
        }

        CmsGetRpcInterfaceDirectory asdu = new CmsGetRpcInterfaceDirectory(MessageType.REQUEST);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Request failed");
            return;
        }

        CmsGetRpcInterfaceDirectory dir = (CmsGetRpcInterfaceDirectory) response.getAsdu();
        System.out.println("  Interfaces: " + dir.reference.size());
        for (int i = 0; i < dir.reference.size(); i++) {
            System.out.println("    [" + i + "] " + dir.reference.get(i).get());
        }
    }
}

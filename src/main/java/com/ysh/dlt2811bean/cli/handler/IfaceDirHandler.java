package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDirectory;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.Map;

public class IfaceDirHandler extends AbstractServiceHandler {

    public IfaceDirHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_RPC_INTERFACE_DIRECTORY); }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        CmsGetRpcInterfaceDirectory asdu = new CmsGetRpcInterfaceDirectory(MessageType.REQUEST);
        CmsApdu response = sendAndVerify(client, asdu);

        CmsGetRpcInterfaceDirectory dir = (CmsGetRpcInterfaceDirectory) response.getAsdu();
        System.out.println("  Interfaces: " + dir.reference.size());
        for (int i = 0; i < dir.reference.size(); i++) {
            System.out.println("    [" + i + "] " + dir.reference.get(i).get());
        }
    }
}

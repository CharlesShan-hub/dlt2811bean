package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetServerDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsObjectClass;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.Map;

public class ServerDirHandler extends AbstractServiceHandler {

    public ServerDirHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_SERVER_DIRECTORY); }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        CmsGetServerDirectory reqAsdu = new CmsGetServerDirectory(MessageType.REQUEST)
                .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE));
        CmsApdu response = sendAndVerify(client, reqAsdu);
        CmsGetServerDirectory resAsdu = (CmsGetServerDirectory) response.getAsdu();
        if (!printIfEmpty(resAsdu.reference().isEmpty())) {
            System.out.println("  Logical devices:");
            for (int i = 0; i < resAsdu.reference().size(); i++) {
                System.out.println("    [" + i + "] " + resAsdu.reference().get(i).get());
            }
        }
    }
}

package com.ysh.dlt2811bean.cli.handler.association;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.Map;

public class ReleaseHandler extends AbstractServiceHandler {

    public ReleaseHandler(CliContext ctx) { super(ctx, ServiceInfo.RELEASE); }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        CmsRelease reqAsdu = new CmsRelease(MessageType.REQUEST);
        CliPrinter.printRequestPdu(ctx, reqAsdu);
        CmsApdu response = client.release();
        CliPrinter.printResponsePdu(ctx, response);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Released"));
        } else {
            System.out.println(CmsColor.red("  Release failed"));
        }
    }
}

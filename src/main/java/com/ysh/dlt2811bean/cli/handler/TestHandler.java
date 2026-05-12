package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.Map;

public class TestHandler extends AbstractServiceHandler {

    public TestHandler(CliContext ctx) { super(ctx, ServiceInfo.TEST); }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        CmsTest reqAsdu = new CmsTest(MessageType.REQUEST);
        printRequestPdu(reqAsdu);
        CmsApdu response = client.test();
        if (response != null) {
            printResponsePdu(response);
        }
        System.out.println("  Test " + (response != null ? CmsColor.green("OK") : CmsColor.red("failed")));
    }
}

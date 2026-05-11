package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class TestHandler extends AbstractServiceHandler {

    public TestHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "test"; }
    public String getDescription() { return "发送心跳测试"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        CmsTest reqAsdu = new CmsTest(MessageType.REQUEST);
        ctx.printGrayPdu("  >> Request PDU:", reqAsdu);
        CmsApdu response = client.test();
        if (response != null && ctx.getConfig().getCli().isTracePdu()) {
            ctx.printGrayPdu("  << Response PDU:", response);
        }
        System.out.println("  Test " + (response != null ? CmsColor.green("OK") : CmsColor.red("failed")));
    }
}

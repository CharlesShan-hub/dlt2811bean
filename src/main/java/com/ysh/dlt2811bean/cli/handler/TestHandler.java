package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class TestHandler implements CommandHandler {

    private final CliContext ctx;

    public TestHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "test"; }
    public String getDescription() { return "发送心跳测试"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println(CmsColor.gray("  Not connected. Type 'connect' first."));
            return;
        }
        CmsTest reqAsdu = new CmsTest(MessageType.REQUEST);
        System.out.println(CmsColor.gray("  >> Request PDU:\n" + reqAsdu.toString().indent(4).stripTrailing()));
        CmsApdu response = client.test();
        if (response != null) {
            System.out.println(CmsColor.gray("  << Response PDU:\n" + response.toString().indent(4).stripTrailing()));
        }
        System.out.println("  Test " + (response != null ? CmsColor.green("OK") : CmsColor.red("failed")));
    }
}

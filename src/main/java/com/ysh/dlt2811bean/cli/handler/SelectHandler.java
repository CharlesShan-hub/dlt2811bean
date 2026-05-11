package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsSelect;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class SelectHandler implements CommandHandler {

    private final CliContext ctx;

    public SelectHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "select"; }
    public String getDescription() { return "选择控制对象"; }
    public List<Param> getParams() {
        return List.of(
            new Param("reference", "对象引用", "E1Q1SB1/XCBR1.Pos")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println("  Not connected. Type 'connect' first.");
            return;
        }

        String ref = values.get("reference");
        CmsSelect asdu = new CmsSelect(MessageType.REQUEST).reference(ref);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Select failed");
            return;
        }
        System.out.println("  Selected: " + ref);
    }
}

package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class ReleaseHandler implements CommandHandler {

    private final CliContext ctx;

    public ReleaseHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "release"; }
    public String getDescription() { return "释放关联"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println(CmsColor.gray("  Not connected. Type 'connect' first."));
            return;
        }
        CmsRelease reqAsdu = new CmsRelease(MessageType.REQUEST);
        System.out.println(CmsColor.gray("  >> Request PDU:\n" + reqAsdu.toString().indent(4).stripTrailing()));
        CmsApdu response = client.release();
        System.out.println(CmsColor.gray("  << Response PDU:\n" + response.toString().indent(4).stripTrailing()));
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Released"));
        } else {
            System.out.println(CmsColor.red("  Release failed"));
        }
    }
}

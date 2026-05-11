package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class ReleaseHandler extends AbstractServiceHandler {

    public ReleaseHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "release"; }
    public String getDescription() { return "释放关联"; }
    public List<Param> getParams() { return List.of(); }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        CmsRelease reqAsdu = new CmsRelease(MessageType.REQUEST);
        ctx.printGrayPdu("  >> Request PDU:", reqAsdu);
        CmsApdu response = client.release();
        ctx.printGrayPdu("  << Response PDU:", response);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Released"));
        } else {
            System.out.println(CmsColor.red("  Release failed"));
        }
    }
}

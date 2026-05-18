package com.ysh.dlt2811bean.cli.handler.negotiation;

import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class NegotiateHandler extends AbstractServiceHandler {

    public NegotiateHandler(CliContext ctx) { super(ctx, ServiceInfo.ASSOCIATE_NEGOTIATE); }
    public List<Param> getParams() {
        return List.of(
            new Param("asduSize", "ASDU 大小", String.valueOf(config().getNegotiate().getAsduSize())),
            new Param("protocolVersion", "协议版本号", String.valueOf(config().getNegotiate().getProtocolVersion()))
        );
    }
    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        int asduSize = Integer.parseInt(values.get("asduSize"));
        int apduSize = asduSize + 4;
        long protocolVersion = Long.parseLong(values.get("protocolVersion"));
        CmsApdu response = client.associateNegotiate(apduSize, asduSize, protocolVersion);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Negotiated!"));
        } else {
            System.out.println(CmsColor.red("  Negotiate failed"));
        }
    }
}

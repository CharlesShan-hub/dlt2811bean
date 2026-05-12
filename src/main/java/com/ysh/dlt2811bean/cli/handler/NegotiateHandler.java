package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.negotiation.CmsAssociateNegotiate;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class NegotiateHandler extends AbstractServiceHandler {

    public NegotiateHandler(CliContext ctx) { super(ctx, ServiceInfo.ASSOCIATE_NEGOTIATE); }
    public List<Param> getParams() {
        CmsConfigLoader config = new CmsConfigLoader();
        return List.of(
            new Param("asduSize", "ASDU 大小", String.valueOf(CmsConfigLoader.load().getNegotiate().getAsduSize())),
            new Param("protocolVersion", "协议版本号", String.valueOf(CmsConfigLoader.load().getNegotiate().getProtocolVersion()))
        );
    }
    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        int asduSize = Integer.parseInt(values.get("asduSize"));
        int apduSize = asduSize + 4;
        long protocolVersion = Long.parseLong(values.get("protocolVersion"));
        CmsAssociateNegotiate reqAsdu = new CmsAssociateNegotiate(MessageType.REQUEST)
                .apduSize(apduSize)
                .asduSize(asduSize)
                .protocolVersion(protocolVersion);
        printRequestPdu(reqAsdu);
        CmsApdu response = client.associateNegotiate(apduSize, asduSize, protocolVersion);
        printResponsePdu(response);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Negotiated!"));
        } else {
            System.out.println(CmsColor.red("  Negotiate failed"));
        }
    }
}

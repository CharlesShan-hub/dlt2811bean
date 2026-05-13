package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.negotiation.CmsAssociateNegotiate;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class ConnectHandler implements CommandHandler {

    private final CliContext ctx;

    public ConnectHandler(CliContext ctx) {
        this.ctx = ctx;
    }

    public String getName() { return "connect"; }
    public String getDescription() { return "连接服务器（自动协商与关联）"; }
    public List<Param> getParams() {
        return List.of(
            new Param("host", "服务器 IP", "127.0.0.1"),
            new Param("port", "服务器端口", String.valueOf(config().getServer().getPort())),
            new Param("asduSize", "ASDU 大小（1~65531，留空跳过协商）", String.valueOf(config().getNegotiate().getAsduSize())),
            new Param("protocolVersion", "协议版本", String.valueOf(config().getNegotiate().getProtocolVersion())),
            new Param("iedName", "IED名称", config().getClient().getDefaultIedName()),
            new Param("accessPoint", "访问点", config().getClient().getDefaultAccessPoint()),
            new Param("secure", "携带证书认证 (true/false)", "false")
        );
    }
    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        String host = values.get("host");
        int port = Integer.parseInt(values.get("port"));
        client.connect(host, port);
        System.out.println(CmsColor.green("  Connected to " + host + ":" + port));

        String asduSizeStr = values.get("asduSize");
        if (!asduSizeStr.isEmpty()) {
            int asduSize = Integer.parseInt(asduSizeStr);
            int apduSize = asduSize + 4;
            long protocolVersion = Long.parseLong(values.get("protocolVersion"));
            String accessPoint = values.get("accessPoint");
            String iedName = values.get("iedName");

            client.setAccessPoint(iedName, accessPoint);

            CmsAssociateNegotiate negReq = new CmsAssociateNegotiate(MessageType.REQUEST)
                    .apduSize(apduSize).asduSize(asduSize).protocolVersion(protocolVersion);
            if (ctx.getConfig().getCli().isTracePdu()) {
                System.out.println(CmsColor.gray("  >> Request PDU:\n" + negReq.toString().indent(4).stripTrailing()));
            }
            CmsApdu negResponse = client.associateNegotiate(apduSize, asduSize, protocolVersion);
            if (ctx.getConfig().getCli().isTracePdu()) {
                System.out.println(CmsColor.gray("  << Response PDU:\n" + negResponse.toString().indent(4).stripTrailing()));
            }
            if (negResponse == null || negResponse.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println(CmsColor.red("  Negotiate failed"));
                return;
            }
            System.out.println(CmsColor.green("  Negotiated OK") + " (asduSize=" + asduSize + ")");

            boolean secure = Boolean.parseBoolean(values.get("secure"));
            if (secure) {
                client.enableSecurity();
                System.out.println(CmsColor.gray("  GM security enabled"));
            }

            CmsAssociate assocReq = new CmsAssociate(MessageType.REQUEST)
                    .serverAccessPointReference(iedName, accessPoint);
            if (ctx.getConfig().getCli().isTracePdu()) {
                System.out.println(CmsColor.gray("  >> Request PDU:\n" + assocReq.toString().indent(4).stripTrailing()));
            }
            CmsApdu assocResponse = client.associate();
            if (assocResponse != null && ctx.getConfig().getCli().isTracePdu()) {
                System.out.println(CmsColor.gray("  << Response PDU:\n" + assocResponse.toString().indent(4).stripTrailing()));
            }
            if (assocResponse != null && assocResponse.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                System.out.println(CmsColor.green("  Associated") + " (ID=" + ctx.bytesToHex(client.getAssociationId(), 8) + "...)");
                ctx.discoverAfterConnect(client);
            } else {
                System.out.println(CmsColor.red("  Associate failed"));
            }
        }
    }
}

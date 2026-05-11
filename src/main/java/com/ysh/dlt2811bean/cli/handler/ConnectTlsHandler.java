package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.security.GmSslContext;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.negotiation.CmsAssociateNegotiate;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class ConnectTlsHandler implements CommandHandler {

    private final CliContext ctx;

    public ConnectTlsHandler(CliContext ctx) {
        this.ctx = ctx;
    }

    public String getName() { return "connect-tls"; }
    public String getDescription() { return "TLS 连接服务器（自动协商与关联）"; }
    public List<Param> getParams() {
        return List.of(
            new Param("host", "服务器 IP", "127.0.0.1"),
            new Param("port", "服务器端口", String.valueOf(CmsConfigLoader.load().getServer().getSslPort())),
            new Param("asduSize", "ASDU 大小（1~65531，留空跳过协商）", String.valueOf(CmsConfigLoader.load().getNegotiate().getAsduSize())),
            new Param("protocolVersion", "协议版本", String.valueOf(CmsConfigLoader.load().getNegotiate().getProtocolVersion())),
            new Param("ap", "AccessPoint", CmsConfigLoader.load().getClient().getDefaultAccessPoint()),
            new Param("ep", "Endpoint", CmsConfigLoader.load().getClient().getDefaultEp()),
            new Param("secure", "携带证书认证 (true/false)", "false")
        );
    }
    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        String host = values.get("host");
        int port = Integer.parseInt(values.get("port"));

        CmsConfig config = CmsConfigLoader.load();
        GmSslContext sslContext = GmSslContext.forClient()
                .keyStore(config.getSecurity().getKeystore().getPath(), config.getSecurity().getKeystore().getPassword())
                .trustManager(new javax.net.ssl.X509TrustManager[]{
                    new javax.net.ssl.X509TrustManager() {
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
                    }
                })
                .useStandardTls()
                .build();
        client.sslContext(sslContext);
        client.connectTls(host, port);
        System.out.println(CmsColor.green("  Connected to " + host + ":" + port + " (TLS)"));

        String asduSizeStr = values.get("asduSize");
        if (!asduSizeStr.isEmpty()) {
            int asduSize = Integer.parseInt(asduSizeStr);
            int apduSize = asduSize + 4;
            long protocolVersion = Long.parseLong(values.get("protocolVersion"));
            String ap = values.get("ap");
            String ep = values.get("ep");

            client.setAccessPoint(ap, ep);

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
                    .serverAccessPointReference(ap, ep);
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

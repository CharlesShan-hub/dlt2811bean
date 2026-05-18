package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.common.CommandHandler;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.utils.CmsColor;

import java.util.List;
import java.util.Map;

public abstract class AbstractConnectHandler implements CommandHandler {

    protected final CliContext ctx;

    protected AbstractConnectHandler(CliContext ctx) {
        this.ctx = ctx;
    }

    // ── 子类需实现的"变点" ──

    protected abstract String commandName();

    protected abstract String commandDescription();

    protected abstract int defaultPort();

    protected abstract void doConnect(CmsClient client, String host, int port) throws Exception;

    protected String connectionSuffix() {
        return "";
    }

    @Override
    public String getName() {
        return commandName();
    }

    @Override
    public String getDescription() {
        return commandDescription();
    }

    @Override
    public List<Param> getParams() {
        return List.of(
            new Param("host", "服务器 IP", "127.0.0.1"),
            new Param("port", "服务器端口", String.valueOf(defaultPort())),
            new Param("asduSize", "ASDU 大小（1~65531，留空跳过协商）", ""),
            new Param("protocolVersion", "协议版本",
                    String.valueOf(config().getNegotiate().getProtocolVersion())),
            new Param("iedName", "IED名称", config().getClient().getDefaultIedName()),
            new Param("accessPoint", "访问点", config().getClient().getDefaultAccessPoint()),
            new Param("secure", "携带证书认证 (true/false)", "false")
        );
    }

    @Override
    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        String host = values.get("host");
        int port = Integer.parseInt(values.get("port"));

        ctx.getAutoTestHeartbeat().stop();
        doConnect(client, host, port);
        ctx.getAutoTestHeartbeat().start(client, 25);
        System.out.println(CmsColor.green("  Connected to " + host + ":" + port + connectionSuffix()));

        String asduSizeStr = values.get("asduSize");
        if (asduSizeStr == null || asduSizeStr.isEmpty()) return;

        int asduSize = Integer.parseInt(asduSizeStr);
        int apduSize = asduSize + 4;
        long protocolVersion = Long.parseLong(values.get("protocolVersion"));
        String accessPoint = values.get("accessPoint");
        String iedName = values.get("iedName");
        boolean secure = Boolean.parseBoolean(values.get("secure"));

        negotiateAndAssociate(client, apduSize, asduSize, protocolVersion, iedName, accessPoint, secure);
    }

    private void negotiateAndAssociate(CmsClient client, int apduSize, int asduSize,
                                        long protocolVersion, String iedName,
                                        String accessPoint, boolean secure) throws Exception {
        client.setAccessPoint(iedName, accessPoint);

        CmsApdu negResponse = client.associateNegotiate(apduSize, asduSize, protocolVersion);
        if (negResponse == null || negResponse.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.red("  Negotiate failed"));
            return;
        }
        System.out.println(CmsColor.green("  Negotiated OK") + " (asduSize=" + asduSize + ")");

        if (secure) {
            client.enableSecurity();
            System.out.println(CmsColor.gray("  GM security enabled"));
        }

        CmsApdu assocResponse = client.associate();
        if (assocResponse != null && assocResponse.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Associated") + " (ID=" + ctx.bytesToHex(client.getAssociationId(), 8) + "...)");
            ctx.discoverAfterConnect(client);
        } else {
            System.out.println(CmsColor.red("  Associate failed"));
        }
    }
}

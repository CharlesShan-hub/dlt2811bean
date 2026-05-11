package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class AssociateHandler extends AbstractServiceHandler {

    public AssociateHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "associate"; }
    public String getDescription() { return "建立关联"; }
    public List<Param> getParams() {
        var config = CmsConfigLoader.load();
        return List.of(
            new Param("ap", "访问点 (AccessPoint)", config.getClient().getDefaultAccessPoint()),
            new Param("ep", "EP", config.getClient().getDefaultEp()),
            new Param("secure", "携带证书认证 (true/false)", "false")
        );
    }
    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        if (Boolean.parseBoolean(values.get("secure"))) {
            client.enableSecurity();
            System.out.println(CmsColor.gray("  GM security enabled"));
        }
        String ap = values.get("ap");
        String ep = values.get("ep");
        CmsAssociate reqAsdu = new CmsAssociate(MessageType.REQUEST)
                .serverAccessPointReference(ap, ep);
        ctx.printGrayPdu("  >> Request PDU:", reqAsdu);
        CmsApdu response = "E1Q1SB1".equals(ap) && "S1".equals(ep)
            ? client.associate()
            : client.associate(ap, ep);
        ctx.printGrayPdu("  << Response PDU:", response);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Associated!"));
        } else {
            System.out.println(CmsColor.red("  Associate failed"));
        }
    }
}

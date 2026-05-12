package com.ysh.dlt2811bean.cli.handler.association;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class AssociateHandler extends AbstractServiceHandler {

    public AssociateHandler(CliContext ctx) { super(ctx, ServiceInfo.ASSOCIATE); }
    public List<Param> getParams() {
        var config = CmsConfigLoader.load();
        return List.of(
            new Param("iedName", "IED名称 [string]", config.getClient().getDefaultIedName()),
            new Param("accessPoint", "访问点 [string]", config.getClient().getDefaultAccessPoint()),
            new Param("secure", "携带证书认证 [boolean]", String.valueOf(config.getClient().isDefaultSecure()))
        );
    }
    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        if (Boolean.parseBoolean(values.get("secure"))) {
            client.enableSecurity();
            System.out.println(CmsColor.gray("  GM security enabled"));
        }
        String iedName = values.get("iedName");
        String accessPoint = values.get("accessPoint");
        CmsAssociate reqAsdu = new CmsAssociate(MessageType.REQUEST)
                .serverAccessPointReference(iedName, accessPoint);
        printRequestPdu(reqAsdu);
        CmsApdu response = client.associate(iedName, accessPoint);
        printResponsePdu(response);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Associated!"));
        } else {
            System.out.println(CmsColor.red("  Associate failed"));
        }
    }
}

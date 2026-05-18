package com.ysh.dlt2811bean.cli.handler.association;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class AssociateHandler extends AbstractServiceHandler {

    public AssociateHandler(CliContext ctx) { super(ctx, ServiceInfo.ASSOCIATE, false); }

    protected List<Param> setParams() {
        var cfg = config().getClient();
        return List.of(
            new Param("iedName", "IED名称", Param.ValueType.STRING, cfg.getDefaultIedName()),
            new Param("accessPoint", "访问点", Param.ValueType.STRING, cfg.getDefaultAccessPoint()),
            new Param("secure", "携带证书认证", Param.ValueType.BOOLEAN, String.valueOf(cfg.isDefaultSecure()))
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        if (booleanVal("secure")) {
            client.enableSecurity();
            CliPrinter.info("GM security enabled");
        }
        CmsApdu response = client.associate(val("iedName"), val("accessPoint"));
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE)
            CliPrinter.success("Associated!");
        else
            CliPrinter.error("Associate failed");
    }
}

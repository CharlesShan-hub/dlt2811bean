package com.ysh.dlt2811bean.cli.handler.association;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.Map;

public class ReleaseHandler extends AbstractServiceHandler {

    public ReleaseHandler(CliContext ctx) { super(ctx, ServiceInfo.RELEASE); }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        ctx.getAutoTestHeartbeat().stop();
        CmsApdu response = client.release();
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            CliPrinter.success("Released");
            ctx.getCachedHierarchy().clear();
        } else {
            CliPrinter.error("Release failed");
        }
    }
}

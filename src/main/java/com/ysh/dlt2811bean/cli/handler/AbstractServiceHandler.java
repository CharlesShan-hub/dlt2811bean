package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import java.util.List;

public abstract class AbstractServiceHandler implements CommandHandler {

    protected final CliContext ctx;
    private final ServiceInfo serviceInfo;
    protected CmsConfig config;

    protected AbstractServiceHandler(CliContext ctx, ServiceInfo serviceInfo) {
        this.ctx = ctx;
        this.serviceInfo = serviceInfo;
    }

    @Override
    public String getName() { return serviceInfo.getCliName(); }

    @Override
    public String getDescription() { return serviceInfo.getDescription(); }

    @Override
    public CmsConfig config() { return CmsConfigLoader.load(); }

    @Override
    public List<Param> updateConfigAndGetParams() {
        config = CmsConfigLoader.load();
        return getParams();
    }

    public List<Param> getParams() { return List.of(); }

    protected void requireConnected(CmsClient client) {
        if (!client.isConnected()) {
            throw new IllegalStateException("Not connected. Type 'connect' first.");
        }
    }

    protected CmsApdu sendAndVerify(CmsClient client, CmsAsdu<?> asdu) throws Exception {
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            throw new IllegalStateException("Request failed");
        }
        return response;
    }
}

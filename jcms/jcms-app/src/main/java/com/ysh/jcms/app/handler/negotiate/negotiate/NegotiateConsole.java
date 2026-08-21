package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.config.CmsConfig.Protocol.Negotiate;

public class NegotiateConsole extends CommandHandler<NegotiateDao, NegotiateClient> {

    public NegotiateConsole() {
        super(CommandInfo.NEGOTIATE);
        Negotiate cfg = CmsConfigLoader.load().protocol().negotiate();
        Param p1 = Param.of("apdu", String.valueOf(cfg.apduSize()), "apduSize", int.class, false);
        param(p1, "APDU 大小（默认取自配置文件）");
        Param p2 = Param.of("asdu", String.valueOf(cfg.asduSize()), "asduSize", long.class, false);
        param(p2, "ASDU 大小（默认取自配置文件）");
        Param p3 = Param.of("version", String.valueOf(cfg.protocolVersion()), "protocolVersion", long.class, false);
        param(p3, "协议版本（默认取自配置文件）");
    }
}
package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.utils.config.CmsConfigLoader;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class NegotiateClientDao {
    private int apduSize = CmsConfigLoader.load().getProtocol().getNegotiate().getApduSize();
    private long asduSize = CmsConfigLoader.load().getProtocol().getNegotiate().getAsduSize();
    private long protocolVersion = CmsConfigLoader.load().getProtocol().getNegotiate().getProtocolVersion();
}

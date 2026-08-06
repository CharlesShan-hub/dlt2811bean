package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.app.handler.BaseDao;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class NegotiateClientDao extends BaseDao {
    private int apduSize = CmsConfigLoader.load().getProtocol().getNegotiate().getApduSize();
    private long asduSize = CmsConfigLoader.load().getProtocol().getNegotiate().getAsduSize();
    private long protocolVersion = CmsConfigLoader.load().getProtocol().getNegotiate().getProtocolVersion();
}

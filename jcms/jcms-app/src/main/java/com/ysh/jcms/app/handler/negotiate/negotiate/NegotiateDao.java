package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.negotiate.CmsNegotiateRequest;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class NegotiateDao extends BaseDao {
    private int apduSize = CmsConfigLoader.load().protocol().negotiate().apduSize();
    private long asduSize = CmsConfigLoader.load().protocol().negotiate().asduSize();
    private long protocolVersion = CmsConfigLoader.load().protocol().negotiate().protocolVersion();

    @Override
    public CmsType toRequest() {
        return new CmsNegotiateRequest().apduSize(apduSize).asduSize(asduSize).protocolVersion(protocolVersion);
    }
}

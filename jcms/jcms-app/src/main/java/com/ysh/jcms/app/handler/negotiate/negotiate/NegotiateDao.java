package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.negotiate.CmsNegotiateRequest;
import com.ysh.jcms.utils.config.CmsConfigInjector;
import com.ysh.jcms.utils.config.CmsValue;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class NegotiateDao extends BaseDao {

    @CmsValue("protocol.negotiate.apduSize")
    private int apduSize;

    @CmsValue("protocol.negotiate.asduSize")
    private long asduSize;

    @CmsValue("protocol.negotiate.protocolVersion")
    private long protocolVersion;

    public NegotiateDao() {
        CmsConfigInjector.inject(this);
    }

    @Override
    public CmsType toRequest() {
        return new CmsNegotiateRequest()
            .apduSize(apduSize)
            .asduSize(asduSize)
            .protocolVersion(protocolVersion);
    }
}
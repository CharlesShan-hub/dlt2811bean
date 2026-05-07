package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSelectActiveSG;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for SelectActiveSG service (SC=0x54).
 */
public class SelectActiveSGHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SELECT_ACTIVE_SG;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsSelectActiveSG asdu = (CmsSelectActiveSG) request.getAsdu();
        return new CmsApdu(asdu);
    }
}

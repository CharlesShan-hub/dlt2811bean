package com.ysh.dlt2811bean.transport.protocol.log;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsQueryLogAfter;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for QueryLogAfter service (SC=0x62).
 */
public class QueryLogAfterHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.QUERY_LOG_AFTER;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsQueryLogAfter asdu = (CmsQueryLogAfter) request.getAsdu();
        return new CmsApdu(asdu);
    }
}

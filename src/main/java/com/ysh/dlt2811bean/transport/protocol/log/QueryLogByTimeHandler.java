package com.ysh.dlt2811bean.transport.protocol.log;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsQueryLogByTime;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for QueryLogByTime service (SC=0x61).
 */
public class QueryLogByTimeHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.QUERY_LOG_BY_TIME;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsQueryLogByTime asdu = (CmsQueryLogByTime) request.getAsdu();
        return new CmsApdu(asdu);
    }
}

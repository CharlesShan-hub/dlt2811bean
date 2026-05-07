package com.ysh.dlt2811bean.transport.protocol.log;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetLogStatusValues;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for GetLogStatusValues service (SC=0x63).
 */
public class GetLogStatusValuesHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_LOG_STATUS_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsGetLogStatusValues asdu = (CmsGetLogStatusValues) request.getAsdu();
        return new CmsApdu(asdu);
    }
}

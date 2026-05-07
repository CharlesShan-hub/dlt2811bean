package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsSetURCBValues;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for SetURCBValues service (SC=0x5E).
 */
public class SetURCBValuesHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_URCB_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsSetURCBValues asdu = (CmsSetURCBValues) request.getAsdu();
        return new CmsApdu(asdu);
    }
}

package com.ysh.dlt2811bean.transport.protocol.sv;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.sv.CmsGetMSVCBValues;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for GetMSVCBValues service (SC=0x69).
 */
public class GetMSVCBValuesHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_MSVCB_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsGetMSVCBValues asdu = (CmsGetMSVCBValues) request.getAsdu();
        return new CmsApdu(asdu);
    }
}

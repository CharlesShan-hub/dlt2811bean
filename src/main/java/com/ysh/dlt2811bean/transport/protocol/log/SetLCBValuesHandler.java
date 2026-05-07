package com.ysh.dlt2811bean.transport.protocol.log;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSetLCBValues;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for SetLCBValues service (SC=0x60).
 */
public class SetLCBValuesHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_LCB_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsSetLCBValues asdu = (CmsSetLCBValues) request.getAsdu();
        return new CmsApdu(asdu);
    }
}

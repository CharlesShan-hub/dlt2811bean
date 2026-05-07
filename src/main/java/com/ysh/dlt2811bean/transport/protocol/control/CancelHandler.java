package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsCancel;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for Cancel service (SC=0x46).
 */
public class CancelHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.CANCEL;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsCancel asdu = (CmsCancel) request.getAsdu();
        return new CmsApdu(asdu);
    }
}

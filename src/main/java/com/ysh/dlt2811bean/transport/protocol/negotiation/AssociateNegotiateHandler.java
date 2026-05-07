package com.ysh.dlt2811bean.transport.protocol.negotiation;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.negotiation.CmsAssociateNegotiate;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for AssociateNegotiate service (SC=0x9A).
 */
public class AssociateNegotiateHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.ASSOCIATE_NEGOTIATE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsAssociateNegotiate asdu = (CmsAssociateNegotiate) request.getAsdu();
        return new CmsApdu(asdu);
    }
}

package com.ysh.dlt2811bean.transport.protocol.association;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.transport.session.SessionState;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class ReleaseHandler extends AbstractCmsServiceHandler<CmsRelease> {

    public ReleaseHandler() {
        super(ServiceName.RELEASE, CmsRelease::new);
    }

    @Override
    protected CmsApdu doServerHandle() {
        
        serverSession.clearAssociationId();
        serverSession.setState(SessionState.DISCONNECTED);

        CmsRelease response = new CmsRelease(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .associationId(serverSession.getAssociationId())
                .serviceError(CmsServiceError.NO_ERROR);

        log.debug("Association released");
        return new CmsApdu(response);
    }
}
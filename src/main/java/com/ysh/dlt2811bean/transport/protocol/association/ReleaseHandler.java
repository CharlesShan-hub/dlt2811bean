package com.ysh.dlt2811bean.transport.protocol.association;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for Release service (SC=2).
 *
 * <p>Always responds positively and clears the association ID.
 * Release is the graceful way to terminate an association.
 */
public class ReleaseHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(ReleaseHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.RELEASE;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        CmsRelease asdu = (CmsRelease) request.getAsdu();
        byte[] localId = session.getAssociationId();

        session.clearAssociationId();
        session.setState(SessionState.DISCONNECTED);

        CmsRelease response = new CmsRelease(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .associationId(localId != null ? localId : new byte[64])
                .serviceError(CmsServiceError.NO_ERROR);

        log.debug("Association released");
        return new CmsApdu(response);
    }
}
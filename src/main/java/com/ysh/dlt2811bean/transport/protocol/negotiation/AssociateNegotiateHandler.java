package com.ysh.dlt2811bean.transport.protocol.negotiation;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.negotiation.CmsAssociateNegotiate;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssociateNegotiateHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(AssociateNegotiateHandler.class);

    private final int serverApduSize;
    private final int serverAsduSize;
    private final int serverProtocolVersion;
    private final String serverModelVersion;

    public AssociateNegotiateHandler() {
        this(65535, 65531, 1, "1.0");
    }

    public AssociateNegotiateHandler(int serverApduSize, int serverAsduSize,
                                     int serverProtocolVersion, String serverModelVersion) {
        this.serverApduSize = serverApduSize;
        this.serverAsduSize = serverAsduSize;
        this.serverProtocolVersion = serverProtocolVersion;
        this.serverModelVersion = serverModelVersion;
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.ASSOCIATE_NEGOTIATE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsAssociateNegotiate asdu = (CmsAssociateNegotiate) request.getAsdu();

        int clientApduSize = asdu.apduSize.get();
        long clientAsduSize = asdu.asduSize.get();
        long clientProtocolVersion = asdu.protocolVersion.get();

        log.debug("[Server] Negotiate request: apduSize={}, asduSize={}, protocolVersion={}",
                clientApduSize, clientAsduSize, clientProtocolVersion);

        if (clientProtocolVersion != serverProtocolVersion) {
            log.warn("[Server] Protocol version mismatch: client={}, server={}",
                    clientProtocolVersion, serverProtocolVersion);
            return buildNegativeResponse(asdu, CmsServiceError.TYPE_CONFLICT);
        }

        int negotiatedApduSize = Math.min(clientApduSize, serverApduSize);

        session.setNegotiated(true);
        session.setNegotiatedApduSize(negotiatedApduSize);
        session.setPeerAsduSize((int) clientAsduSize);
        session.setPeerProtocolVersion((int) clientProtocolVersion);

        CmsAssociateNegotiate response = new CmsAssociateNegotiate(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .apduSize(negotiatedApduSize)
                .asduSize(serverAsduSize)
                .protocolVersion(serverProtocolVersion)
                .modelVersion(serverModelVersion);

        log.debug("[Server] Negotiate response: apduSize={}, asduSize={}, protocolVersion={}, modelVersion={}",
                negotiatedApduSize, serverAsduSize, serverProtocolVersion, serverModelVersion);
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsAssociateNegotiate request, int errorCode) {
        CmsAssociateNegotiate response = new CmsAssociateNegotiate(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}

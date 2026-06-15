package com.ysh.dlt2811bean.transport.protocol.negotiation;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.negotiation.CmsAssociateNegotiate;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class AssociateNegotiateHandler extends AbstractCmsServiceHandler<CmsAssociateNegotiate> {

    /** APDU max size = FL + APCH(4bytes) */
    public static final int MAX_APDU_SIZE = 65535;
    /** ASDU max size = MAX_APDU_SIZE - APCH(4bytes) = FL max */
    public static final int MAX_ASDU_SIZE = CmsApdu.MAX_ASDU_SIZE;

    private final int serverApduSize;
    private final int serverAsduSize;
    private final int serverProtocolVersion;
    private final String serverModelVersion;

    public AssociateNegotiateHandler() {
        this(MAX_APDU_SIZE, MAX_ASDU_SIZE, 1, "1.0");
    }

    public AssociateNegotiateHandler(int serverApduSize, int serverAsduSize,
                                     int serverProtocolVersion, String serverModelVersion) {
        super(ServiceName.ASSOCIATE_NEGOTIATE, CmsAssociateNegotiate::new, false);
        if (serverApduSize <= 0 || serverApduSize > MAX_APDU_SIZE) {
            throw new IllegalArgumentException(
                "serverApduSize must be between 1 and " + MAX_APDU_SIZE + ", got " + serverApduSize);
        }
        if (serverAsduSize <= 0 || serverAsduSize > MAX_ASDU_SIZE) {
            throw new IllegalArgumentException(
                "serverAsduSize must be between 1 and " + MAX_ASDU_SIZE + ", got " + serverAsduSize);
        }
        this.serverApduSize = serverApduSize;
        this.serverAsduSize = serverAsduSize;
        this.serverProtocolVersion = serverProtocolVersion;
        this.serverModelVersion = serverModelVersion;
    }

    @Override
    protected CmsApdu doServerHandle() {

        int clientApduSize = asdu.apduSize.get();
        long clientAsduSize = asdu.asduSize.get();
        long clientProtocolVersion = asdu.protocolVersion.get();

        log.debug("[Server] Negotiate request: apduSize={}, asduSize={}, protocolVersion={}",
                clientApduSize, clientAsduSize, clientProtocolVersion);

        if (clientApduSize <= 0 || clientApduSize > MAX_APDU_SIZE) {
            log.warn("[Server] Client apduSize out of range: {}", clientApduSize);
            return buildNegativeResponse(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
        if (clientAsduSize <= 0 || clientAsduSize > MAX_ASDU_SIZE) {
            log.warn("[Server] Client asduSize out of range: {}", clientAsduSize);
            return buildNegativeResponse(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
        if (clientProtocolVersion != serverProtocolVersion) {
            log.warn("[Server] Protocol version mismatch: client={}, server={}",
                    clientProtocolVersion, serverProtocolVersion);
            return buildNegativeResponse(CmsServiceError.TYPE_CONFLICT);
        }

        int negotiatedApduSize = Math.min(clientApduSize, serverApduSize);

        serverSession.setNegotiated(true);
        serverSession.setNegotiatedApduSize(negotiatedApduSize);
        serverSession.setPeerAsduSize((int) clientAsduSize);
        serverSession.setPeerProtocolVersion((int) clientProtocolVersion);

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
}

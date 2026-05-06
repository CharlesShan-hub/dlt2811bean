package com.ysh.dlt2811bean.transport.protocol.association;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.AssociationIdGenerator;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.session.SessionState;

/**
 * Handler for Associate service (SC=1).
 *
 * <p>Generates a 64-byte association ID for the client and returns it
 * in a positive response.
 */
public class AssociateHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.ASSOCIATE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsAssociate asdu = (CmsAssociate) request.getAsdu();

        // Generate association ID
        byte[] assocId = AssociationIdGenerator.generate();
        session.setAssociationId(assocId);
        session.setState(SessionState.ASSOCIATED);

        // Build positive response
        CmsAssociate response = new CmsAssociate(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .associationId(assocId)
                .serviceError(CmsServiceError.NO_ERROR);

        System.out.println("[Server] Association accepted, assocId=" + hex(assocId));
        return new CmsApdu(response);
    }

    private String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(bytes.length, 8); i++) {
            sb.append(String.format("%02X", bytes[i]));
        }
        if (bytes.length > 8) sb.append("...");
        return sb.toString();
    }
}
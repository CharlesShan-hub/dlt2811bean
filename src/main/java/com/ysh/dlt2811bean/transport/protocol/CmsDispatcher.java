package com.ysh.dlt2811bean.transport.protocol;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Routes incoming APDUs to registered service handlers.
 *
 * <p>The dispatcher maintains a map from {@link ServiceName} to
 * {@link CmsServiceHandler}. When an APDU arrives, it looks up the
 * handler and delegates to it.
 *
 * <p>If no handler is registered for a service code, an error response
 * is returned ({@link CmsServiceError}).
 */
public class CmsDispatcher {

    private final Map<ServiceName, CmsServiceHandler> handlers = new HashMap<>();

    /**
     * Registers a handler for a service code.
     *
     * @param handler the handler to register
     * @throws IllegalArgumentException if a handler is already registered for the service code
     */
    public void registerHandler(CmsServiceHandler handler) {
        ServiceName sn = handler.getServiceName();
        if (handlers.put(sn, handler) != null) {
            throw new IllegalArgumentException("Handler already registered for " + sn);
        }
    }

    /**
     * Dispatches an incoming APDU to the appropriate handler.
     *
     * @param session the server session
     * @param apdu    the incoming APDU
     * @return the response APDU, or null if the service is one-way
     */
    public CmsApdu dispatch(CmsServerSession session, CmsApdu apdu) {
        ServiceName sc = apdu.getAsdu().getServiceName();

        // ASSOCIATE, TEST, ABORT do not need association
        // - ASSOCIATE: establishes association
        // - TEST: connection keep-alive
        // - ABORT: one-way, can abort any connection
        if (sc != ServiceName.ASSOCIATE && sc != ServiceName.TEST && sc != ServiceName.ABORT) {
            if (!session.isAssociated()) {
                return createErrorResponse(apdu, CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE);
            }
        }

        CmsServiceHandler handler = handlers.get(sc);

        if (handler == null) {
            return createErrorResponse(apdu, CmsServiceError.CLASS_NOT_SUPPORTED);
        }

        try {
            return handler.handleRequest(session, apdu);
        } catch (Exception e) {
            return createErrorResponse(apdu, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    /**
     * Creates an error response with the given service error.
     */
    private CmsApdu createErrorResponse(CmsApdu request, int error) {
        CmsAsdu<?> asdu = request.getAsdu();
        // Copy request ASDU and set MessageType
        CmsAsdu<?> errorAsdu = asdu.copy();
        errorAsdu.messageType(MessageType.RESPONSE_NEGATIVE);
        return new CmsApdu(errorAsdu);
    }

    /**
     * @return the number of registered handlers
     */
    public int handlerCount() {
        return handlers.size();
    }
}

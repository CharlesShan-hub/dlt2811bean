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

    public void registerHandler(CmsServiceHandler handler) {
        handlers.put(handler.getServiceName(), handler);
    }

    public void registerDefaultHandler(CmsServiceHandler handler) {
        ServiceName sn = handler.getServiceName();
        if (!handlers.containsKey(sn)) {
            handlers.put(sn, handler);
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

        // ASSOCIATE, ASSOCIATE_NEGOTIATE, TEST, ABORT do not need association
        if (sc != ServiceName.ASSOCIATE && sc != ServiceName.ASSOCIATE_NEGOTIATE
                && sc != ServiceName.TEST && sc != ServiceName.ABORT) {
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

    private CmsApdu createErrorResponse(CmsApdu request, int error) {
        CmsAsdu<?> asdu = request.getAsdu();
        CmsAsdu<?> errorAsdu = asdu.copy();
        errorAsdu.messageType(MessageType.RESPONSE_NEGATIVE);
        try {
            java.lang.reflect.Field f = errorAsdu.getClass().getField("serviceError");
            CmsServiceError se = (CmsServiceError) f.get(errorAsdu);
            se.set(error);
        } catch (Exception ignored) {
        }
        return new CmsApdu(errorAsdu);
    }

    public int handlerCount() {
        return handlers.size();
    }

}

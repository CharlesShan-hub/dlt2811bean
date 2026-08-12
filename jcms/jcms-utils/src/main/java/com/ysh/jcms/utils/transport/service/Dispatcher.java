package com.ysh.jcms.utils.transport.service;

import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Dispatcher — routes incoming frames to registered {@link ServiceHandler}s.
 */
public class Dispatcher {

    private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

    private final Map<CmsServiceInfo, ServiceHandler> handlers = new HashMap<>();

    public void register(ServiceHandler handler) {
        handlers.put(handler.getServiceName(), handler);
    }

    /**
     * Dispatch an incoming request frame to the appropriate handler.
     *
     * @param session
     *            the session
     * @param request
     *            the incoming request frame
     * @return a DispatchResult containing the outcome and optional response
     */
    public DispatchOutcome dispatch(Session session, Frame request) {
        CmsServiceInfo sc = request.header().serviceCode();
        if (sc == null) {
            return new DispatchOutcome(DispatchResult.NOT_REGISTERED, null);
        }

        ServiceHandler handler = handlers.get(sc);
        if (handler == null) {
            return new DispatchOutcome(DispatchResult.NOT_REGISTERED, null);
        }

        try {
            Frame response = handler.handleRequest(session, request);
            return new DispatchOutcome(DispatchResult.HANDLED, response);
        } catch (Exception e) {
            log.error("Handler {} threw unexpected exception for service {}", handler.getClass().getSimpleName(), sc, e);
            return new DispatchOutcome(DispatchResult.ERROR_OCCURRED, null);
        }
    }

    /** Outcome of a dispatch operation. */
    @Getter
    @AllArgsConstructor
    public static class DispatchOutcome {
        private final DispatchResult result;
        private final Frame response;
    }
}

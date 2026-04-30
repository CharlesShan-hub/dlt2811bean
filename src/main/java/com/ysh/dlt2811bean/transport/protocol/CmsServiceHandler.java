package com.ysh.dlt2811bean.transport.protocol;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler interface for CMS service requests.
 *
 * <p>Each service type (Associate, Release, Read, Write, etc.) has a dedicated
 * handler that processes incoming requests and generates responses.
 *
 * <p>Thread safety: a handler may be shared across multiple sessions.
 * Implementations should be stateless or use proper synchronization.
 */
public interface CmsServiceHandler {

    /**
     * Returns the service name this handler handles.
     *
     * @return the service name
     */
    ServiceName getServiceName();

    /**
     * Handles an incoming service request from a client.
     *
     * @param session the server session
     * @param request the request APDU
     * @return the response APDU, or null if no response should be sent
     *         (e.g., Abort is one-way)
     */
    CmsApdu handleRequest(CmsServerSession session, CmsApdu request);
}

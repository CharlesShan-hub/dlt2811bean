package com.ysh.jcms.utils.transport.service;

import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

/**
 * Handler for a single CMS service type.
 */
public interface ServiceHandler {

    ServiceName getServiceName();

    /**
     * Handle an incoming request.
     *
     * @param session
     *            the session context
     * @param request
     *            the incoming request frame
     * @return response frame, or null if no response is expected
     */
    Frame handleRequest(Session session, Frame request);
}

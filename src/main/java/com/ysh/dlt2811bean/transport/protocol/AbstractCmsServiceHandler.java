package com.ysh.dlt2811bean.transport.protocol;

import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCmsServiceHandler implements CmsServiceHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling {}: {}", getServiceName(), e.getMessage(), e);
            return buildErrorResponse(request);
        }
    }

    protected abstract CmsApdu doHandle(CmsSession session, CmsApdu request) throws Exception;

    protected abstract CmsApdu buildErrorResponse(CmsApdu request);
}

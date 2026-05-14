package com.ysh.dlt2811bean.transport.protocol;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.function.Supplier;

public abstract class AbstractCmsServiceHandler<T extends CmsAsdu<T>> implements CmsServiceHandler {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final ServiceName serviceName;
    private final Supplier<T> factory;
    protected SclIED.SclAccessPoint accessPoint;
    protected SclIED.SclServer server;
    private final boolean needAccessPoint;

    protected AbstractCmsServiceHandler(ServiceName serviceName, Supplier<T> factory) {
        this(serviceName, factory, true);
    }

    protected AbstractCmsServiceHandler(ServiceName serviceName, Supplier<T> factory, boolean needAccessPoint) {
        this.serviceName = serviceName;
        this.factory = factory;
        this.needAccessPoint = needAccessPoint;
    }

    @Override
    public ServiceName getServiceName() {
        return serviceName;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            if (session instanceof CmsServerSession && needAccessPoint) {
                accessPoint = ((CmsServerSession) session).getSclAccessPoint();
                if (accessPoint == null) {
                    log.warn("[Server] No SCL model for session: accessPoint is null");
                    return buildNegativeResponse(request, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
                }
                server = accessPoint.getServer();
                if (server == null) {
                    log.warn("[Server] No SCL model for session: server is null");
                    return buildNegativeResponse(request, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
                }
            }
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling {}: {}", serviceName, e.getMessage(), e);
            return buildNegativeResponse(request);
        }
    }

    protected SclIED.SclServer getServer() {
        return server;
    }

    protected abstract CmsApdu doHandle(CmsSession session, CmsApdu request) throws Exception;

    protected CmsApdu buildNegativeResponse(CmsApdu request) {
        return buildNegativeResponse(request, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
    }

    protected CmsApdu buildNegativeResponse(CmsApdu request, int errorCode) {
        T asdu = factory.get();
        asdu.messageType(MessageType.RESPONSE_NEGATIVE);
        asdu.reqId(request.getReqId());
        asdu.serviceError(errorCode);
        return new CmsApdu(asdu);
    }
}

package com.ysh.dlt2811bean.transport.protocol;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.SclAccessPoint;
import com.ysh.dlt2811bean.scl2.model.SclDocument;
import com.ysh.dlt2811bean.scl2.model.SclServer;
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
    protected SclAccessPoint accessPoint;
    protected SclServer server;
    protected SclDocument sclDocument;
    private final boolean needAccessPoint;
    protected CmsServerSession serverSession;
    protected CmsApdu request;
    protected T asdu;

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
        this.request = request;
        try {
            // build SCL model
            if (session instanceof CmsServerSession && needAccessPoint) {
                serverSession = (CmsServerSession) session;
                accessPoint = serverSession.getSclAccessPoint();
                if (accessPoint == null) {
                    log.warn("[Server] No SCL model for session: accessPoint is null");
                    return buildNegativeResponse(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
                }
                server = accessPoint.getServer();
                if (server == null) {
                    log.warn("[Server] No SCL model for session: server is null");
                    return buildNegativeResponse(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
                }
                sclDocument = serverSession.getSclDocument();
            } else if (session instanceof CmsServerSession) {
                serverSession = (CmsServerSession) session;
                sclDocument = serverSession.getSclDocument();
            }
            
            if (session instanceof CmsServerSession) {
                @SuppressWarnings("unchecked")
                T typedAsdu = (T) request.getAsdu();
                asdu = typedAsdu;
                return doServerHandle();
            } else {
                return doHandle(session, request);
            }
        } catch (Exception e) {
            log.error("[Server] Error handling {}: {}", serviceName, e.getMessage(), e);
            return buildNegativeResponse();
        }
    }

    protected SclServer getServer() {
        return server;
    }

    protected SclDocument getSclDocument() {
        return sclDocument;
    }

    protected CmsApdu doHandle(CmsSession session, CmsApdu request) throws Exception {
        log.error("No implementation for {}: {}", serviceName, request);
        return buildNegativeResponse(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
    }

    protected CmsApdu doServerHandle() throws Exception {
        log.error("No implementation for {}: {}", serviceName, request);
        return buildNegativeResponse(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
    }

    protected CmsApdu buildNegativeResponse() {
        return buildNegativeResponse(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
    }

    protected CmsApdu buildNegativeResponse(int errorCode) {
        T asdu = factory.get();
        asdu.messageType(MessageType.RESPONSE_NEGATIVE);
        asdu.reqId(request.getReqId());
        asdu.serviceError(errorCode);
        return new CmsApdu(asdu);
    }
}
package com.ysh.dlt2811bean.transport.protocol.log;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsQueryLogByTime;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsLogEntry;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryLogByTimeHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(QueryLogByTimeHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.QUERY_LOG_BY_TIME;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling QueryLogByTime: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsQueryLogByTime) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsQueryLogByTime asdu = (CmsQueryLogByTime) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String logRef = asdu.logReference.get();
        if (logRef == null || logRef.isEmpty()) {
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        if (!validateLogReference(accessPoint, logRef)) {
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        log.debug("[Server] QueryLogByTime: logRef={}, startPresent={}, stopPresent={}, entryAfterPresent={}",
                logRef, asdu.startTime != null, asdu.stopTime != null, asdu.entryAfter != null);

        CmsQueryLogByTime response = new CmsQueryLogByTime(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.logEntry = new CmsArray<>(CmsLogEntry::new).capacity(100);
        response.moreFollows.set(false);

        return new CmsApdu(response);
    }

    private boolean validateLogReference(SclIED.SclAccessPoint accessPoint, String logRef) {
        for (SclIED.SclLDevice ld : accessPoint.getServer().getLDevices()) {
            if (ld.getLn0() == null) continue;
            for (SclIED.SclLogControl lc : ld.getLn0().getLogControls()) {
                String lcRef = ld.getInst() + "/LLN0." + lc.getName();
                if (lcRef.equals(logRef)) {
                    return true;
                }
            }
        }
        return false;
    }

    private CmsApdu buildNegativeResponse(CmsQueryLogByTime request, int errorCode) {
        CmsQueryLogByTime response = new CmsQueryLogByTime(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}

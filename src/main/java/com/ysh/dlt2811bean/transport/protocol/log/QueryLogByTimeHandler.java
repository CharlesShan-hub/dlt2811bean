package com.ysh.dlt2811bean.transport.protocol.log;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsQueryLogByTime;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsLogEntry;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class QueryLogByTimeHandler extends AbstractCmsServiceHandler<CmsQueryLogByTime> {

    public QueryLogByTimeHandler() {
        super(ServiceName.QUERY_LOG_BY_TIME, CmsQueryLogByTime::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsQueryLogByTime asdu = (CmsQueryLogByTime) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String logRef = asdu.logReference.get();
        if (logRef == null || logRef.isEmpty()) {
            return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        if (!validateLogReference(accessPoint, logRef)) {
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        log.debug("[Server] QueryLogByTime: logRef={}, startPresent={}, stopPresent={}, entryAfterPresent={}",
                logRef, asdu.startTime != null, asdu.stopTime != null, asdu.entryAfter != null);

        CmsQueryLogByTime response = new CmsQueryLogByTime(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.logEntry = new CmsArray<>(CmsLogEntry::new);
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
}

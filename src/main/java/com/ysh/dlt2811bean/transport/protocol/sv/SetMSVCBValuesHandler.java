package com.ysh.dlt2811bean.transport.protocol.sv;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.sv.CmsSetMSVCBValues;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesResultEntry;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetMSVCBValuesHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(SetMSVCBValuesHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_MSVCB_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling SetMSVCBValues: {}", e.getMessage(), e);
            int reqId = request != null ? ((CmsSetMSVCBValues) request.getAsdu()).reqId().get() : 0;
            return buildNegativeResponse(reqId,
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsSetMSVCBValues asdu = (CmsSetMSVCBValues) request.getAsdu();

        if (asdu.msvcb == null || asdu.msvcb.size() == 0) {
            return buildNegativeResponse(asdu.reqId().get(),
                    CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        CmsArray<CmsSetMSVCBValuesResultEntry> results = new CmsArray<>(CmsSetMSVCBValuesResultEntry::new).capacity(100);
        for (int i = 0; i < asdu.msvcb.size(); i++) {
            CmsSetMSVCBValuesResultEntry result = new CmsSetMSVCBValuesResultEntry();
            // Accept all fields as successful
            results.add(result);
        }

        if (hasAnyError(results)) {
            CmsSetMSVCBValues response = new CmsSetMSVCBValues(MessageType.RESPONSE_NEGATIVE)
                    .reqId(asdu.reqId().get());
            response.result = results;
            log.debug("[Server] SetMSVCBValues: {} entries with errors", results.size());
            return new CmsApdu(response);
        }

        // Success — response PDU is NULL (only ReqID)
        CmsSetMSVCBValues response = new CmsSetMSVCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        log.debug("[Server] SetMSVCBValues: {} entries accepted", results.size());
        return new CmsApdu(response);
    }

    private boolean hasAnyError(CmsArray<CmsSetMSVCBValuesResultEntry> results) {
        return false; // Accept all for now
    }

    private CmsApdu buildNegativeResponse(int reqId, int errorCode) {
        CmsSetMSVCBValues response = new CmsSetMSVCBValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(reqId);
        CmsSetMSVCBValuesResultEntry entry = new CmsSetMSVCBValuesResultEntry();
        entry.error.set(errorCode);
        response.result = new CmsArray<>(CmsSetMSVCBValuesResultEntry::new).capacity(1);
        response.result.add(entry);
        return new CmsApdu(response);
    }
}

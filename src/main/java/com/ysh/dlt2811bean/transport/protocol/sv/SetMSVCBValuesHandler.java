package com.ysh.dlt2811bean.transport.protocol.sv;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.sv.CmsSetMSVCBValues;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesResultEntry;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class SetMSVCBValuesHandler extends AbstractCmsServiceHandler<CmsSetMSVCBValues> {

    public SetMSVCBValuesHandler() {
        super(ServiceName.SET_MSVCB_VALUES, CmsSetMSVCBValues::new);
       }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsSetMSVCBValues asdu = (CmsSetMSVCBValues) request.getAsdu();

        if (asdu.msvcb == null || asdu.msvcb.size() == 0) {
            return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
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

    @Override
    protected CmsApdu buildNegativeResponse(CmsApdu request, int errorCode) {
        CmsSetMSVCBValues response = new CmsSetMSVCBValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.getReqId());
        CmsSetMSVCBValuesResultEntry entry = new CmsSetMSVCBValuesResultEntry();
        entry.error.set(errorCode);
        response.result = new CmsArray<>(CmsSetMSVCBValuesResultEntry::new).capacity(1);
        response.result.add(entry);
        return new CmsApdu(response);
    }
}

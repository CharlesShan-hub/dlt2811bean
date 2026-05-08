package com.ysh.dlt2811bean.transport.protocol.log;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsLCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetLCBValues;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorLcbChoice;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetLCBValuesHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(GetLCBValuesHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_LCB_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetLCBValues: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetLCBValues) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsGetLCBValues asdu = (CmsGetLCBValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsArray<CmsErrorLcbChoice> choices = new CmsArray<>(CmsErrorLcbChoice::new).capacity(100);

        for (int i = 0; i < asdu.reference.size(); i++) {
            String ref = asdu.reference.get(i).get();
            CmsErrorLcbChoice choice = buildLcbChoice(accessPoint, ref);
            choices.add(choice);
        }

        CmsGetLCBValues response = new CmsGetLCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.lcb = choices;
        response.moreFollows.set(false);

        log.debug("[Server] GetLCBValues: {} references", asdu.reference.size());
        return new CmsApdu(response);
    }

    private CmsErrorLcbChoice buildLcbChoice(SclIED.SclAccessPoint accessPoint, String ref) {
        CmsErrorLcbChoice choice = new CmsErrorLcbChoice();

        if (ref == null || ref.isEmpty()) {
            choice.selectError().error.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            return choice;
        }

        for (SclIED.SclLDevice ld : accessPoint.getServer().getLDevices()) {
            if (ld.getLn0() == null) continue;
            for (SclIED.SclLogControl lc : ld.getLn0().getLogControls()) {
                String lcRef = ld.getInst() + "/LLN0." + lc.getName();
                if (lcRef.equals(ref)) {
                    CmsLCB lcb = new CmsLCB();
                    lcb.lcbName.set(lc.getName());
                    lcb.lcbRef.set(lcRef);
                    if (lc.getDatSet() != null) {
                        lcb.datSet.set(lc.getDatSet());
                    }
                    if (lc.getLogName() != null) {
                        lcb.logRef.set(lc.getLogName());
                    }
                    lcb.logEna.set(false);
                    choice.selectValue().value = lcb;
                    return choice;
                }
            }
        }

        choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        return choice;
    }

    private CmsApdu buildNegativeResponse(CmsGetLCBValues request, int errorCode) {
        CmsGetLCBValues response = new CmsGetLCBValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}

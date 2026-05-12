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
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetLCBValuesHandler extends AbstractCmsServiceHandler<CmsGetLCBValues> {

    static final java.util.concurrent.ConcurrentHashMap<String, Boolean> logEnaState = new java.util.concurrent.ConcurrentHashMap<>();

    public GetLCBValuesHandler() {
        super(ServiceName.GET_LCB_VALUES, CmsGetLCBValues::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetLCBValues asdu = (CmsGetLCBValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsArray<CmsErrorLcbChoice> choices = new CmsArray<>(CmsErrorLcbChoice::new);

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
                    lcb.logEna.set(logEnaState.getOrDefault(lcRef, false));
                    choice.selectValue().value = lcb;
                    return choice;
                }
            }
        }

        choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        return choice;
    }
}

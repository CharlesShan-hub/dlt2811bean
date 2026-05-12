package com.ysh.dlt2811bean.transport.protocol.log;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetLogStatusValues;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorLogStatusChoice;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetLogStatusValuesHandler extends AbstractCmsServiceHandler<CmsGetLogStatusValues> {

    public GetLogStatusValuesHandler() {
        super(ServiceName.GET_LOG_STATUS_VALUES, CmsGetLogStatusValues::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetLogStatusValues asdu = (CmsGetLogStatusValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsArray<CmsErrorLogStatusChoice> choices = new CmsArray<>(CmsErrorLogStatusChoice::new);

        for (int i = 0; i < asdu.logReference.size(); i++) {
            String ref = asdu.logReference.get(i).get();
            CmsErrorLogStatusChoice choice = buildStatusChoice(accessPoint, ref);
            choices.add(choice);
        }

        CmsGetLogStatusValues response = new CmsGetLogStatusValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.log = choices;
        response.moreFollows.set(false);

        log.debug("[Server] GetLogStatusValues: {} references", asdu.logReference.size());
        return new CmsApdu(response);
    }

    private CmsErrorLogStatusChoice buildStatusChoice(SclIED.SclAccessPoint accessPoint, String ref) {
        CmsErrorLogStatusChoice choice = new CmsErrorLogStatusChoice();

        if (ref == null || ref.isEmpty()) {
            choice.selectError().error.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            return choice;
        }

        for (SclIED.SclLDevice ld : accessPoint.getServer().getLDevices()) {
            if (ld.getLn0() == null) continue;
            for (SclIED.SclLogControl lc : ld.getLn0().getLogControls()) {
                String lcRef = ld.getInst() + "/LLN0." + lc.getName();
                if (lcRef.equals(ref)) {
                    choice.selectValue();
                    return choice;
                }
            }
        }

        choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        return choice;
    }

}

package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsURCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsGetURCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorUrcbChoice;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetURCBValuesHandler extends AbstractCmsServiceHandler<CmsGetURCBValues> {

    public GetURCBValuesHandler() {
        super(ServiceName.GET_URCB_VALUES, CmsGetURCBValues::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetURCBValues asdu = (CmsGetURCBValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsArray<CmsErrorUrcbChoice> choices = new CmsArray<>(CmsErrorUrcbChoice::new);

        for (int i = 0; i < asdu.reference.size(); i++) {
            String ref = asdu.reference.get(i).get();
            CmsErrorUrcbChoice choice = buildUrcbChoice(accessPoint, ref);
            choices.add(choice);
        }

        CmsGetURCBValues response = new CmsGetURCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.urcb = choices;
        response.moreFollows.set(false);

        log.debug("[Server] GetURCBValues: {} references", asdu.reference.size());
        return new CmsApdu(response);
    }

    private CmsErrorUrcbChoice buildUrcbChoice(SclIED.SclAccessPoint accessPoint, String ref) {
        CmsErrorUrcbChoice choice = new CmsErrorUrcbChoice();

        if (ref == null || ref.isEmpty()) {
            choice.selectError().error.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            return choice;
        }

        for (SclIED.SclLDevice ld : accessPoint.getServer().getLDevices()) {
            if (ld.getLn0() == null) continue;
            for (SclIED.SclReportControl rc : ld.getLn0().getReportControls()) {
                if (rc.isBuffered()) continue;
                String rcRef = ld.getInst() + "/LLN0." + rc.getName();
                if (rcRef.equals(ref)) {
                    CmsURCB urcb = new CmsURCB();
                    urcb.urcbName.set(rc.getName());
                    urcb.urcbRef.set(rcRef);
                    if (rc.getRptID() != null) {
                        urcb.rptID.set(rc.getRptID());
                    }
                    if (rc.getDatSet() != null) {
                        urcb.datSet.set(rc.getDatSet());
                    }
                    if (rc.getConfRev() != null) {
                        urcb.confRev.set(Long.parseLong(rc.getConfRev()));
                    }
                    urcb.rptEna.set(false);
                    urcb.resv.set(false);
                    urcb.gi.set(false);
                    choice.selectValue().value = urcb;
                    return choice;
                }
            }
        }

        choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        return choice;
    }
}

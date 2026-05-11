package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsBRCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsGetBRCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorBrcbChoice;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetBRCBValuesHandler extends AbstractCmsServiceHandler<CmsGetBRCBValues> {

    public GetBRCBValuesHandler() {
        super(ServiceName.GET_BRCB_VALUES, CmsGetBRCBValues::new);
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetBRCBValues: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetBRCBValues) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetBRCBValues asdu = (CmsGetBRCBValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsArray<CmsErrorBrcbChoice> choices = new CmsArray<>(CmsErrorBrcbChoice::new).capacity(100);

        for (int i = 0; i < asdu.brcbReference.size(); i++) {
            String ref = asdu.brcbReference.get(i).get();
            CmsErrorBrcbChoice choice = buildBrcbChoice(accessPoint, ref);
            choices.add(choice);
        }

        CmsGetBRCBValues response = new CmsGetBRCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.errorBrcb = choices;
        response.moreFollows.set(false);

        log.debug("[Server] GetBRCBValues: {} references", asdu.brcbReference.size());
        return new CmsApdu(response);
    }

    private CmsErrorBrcbChoice buildBrcbChoice(SclIED.SclAccessPoint accessPoint, String ref) {
        CmsErrorBrcbChoice choice = new CmsErrorBrcbChoice();

        if (ref == null || ref.isEmpty()) {
            choice.selectError().error.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            return choice;
        }

        for (SclIED.SclLDevice ld : accessPoint.getServer().getLDevices()) {
            if (ld.getLn0() == null) continue;
            for (SclIED.SclReportControl rc : ld.getLn0().getReportControls()) {
                if (!rc.isBuffered()) continue;
                String rcRef = ld.getInst() + "/LLN0." + rc.getName();
                if (rcRef.equals(ref)) {
                    CmsBRCB brcb = new CmsBRCB();
                    brcb.brcbName.set(rc.getName());
                    brcb.brcbRef.set(rcRef);
                    if (rc.getRptID() != null) {
                        brcb.rptID.set(rc.getRptID());
                    }
                    if (rc.getDatSet() != null) {
                        brcb.datSet.set(rc.getDatSet());
                    }
                    if (rc.getConfRev() != null) {
                        brcb.confRev.set(Long.parseLong(rc.getConfRev()));
                    }
                    brcb.rptEna.set(false);
                    brcb.purgeBuf.set(false);
                    brcb.gi.set(false);
                    choice.selectBrcb().brcb = brcb;
                    return choice;
                }
            }
        }

        choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        return choice;
    }

    private CmsApdu buildNegativeResponse(CmsGetBRCBValues request, int errorCode) {
        CmsGetBRCBValues response = new CmsGetBRCBValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}

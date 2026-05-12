package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsSGCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetSGCBValues;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorSgcbChoice;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetSGCBValuesHandler extends AbstractCmsServiceHandler<CmsGetSGCBValues> {

    public GetSGCBValuesHandler() {
        super(ServiceName.GET_SGCB_VALUES, CmsGetSGCBValues::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetSGCBValues asdu = (CmsGetSGCBValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsArray<CmsErrorSgcbChoice> choices = new CmsArray<>(CmsErrorSgcbChoice::new);

        for (int i = 0; i < asdu.sgcbReference.size(); i++) {
            String ref = asdu.sgcbReference.get(i).get();
            CmsErrorSgcbChoice choice = buildSgcbChoice(ref);
            choices.add(choice);
        }

        CmsGetSGCBValues response = new CmsGetSGCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.errorSgcb = choices;

        log.debug("[Server] GetSGCBValues: {} references", asdu.sgcbReference.size());
        return new CmsApdu(response);
    }

    private CmsErrorSgcbChoice buildSgcbChoice(String ref) {
        CmsErrorSgcbChoice choice = new CmsErrorSgcbChoice();

        if (ref == null || ref.isEmpty()) {
            choice.selectError().error.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            return choice;
        }

        CmsSGCB sgcb = new CmsSGCB();
        sgcb.sgcbName.set("SGCB");
        sgcb.sgcbRef.set(ref);
        sgcb.numOfSG.set(4);
        sgcb.actSG.set(1);
        sgcb.editSG.set(1);
        sgcb.cnfEdit.set(false);
        choice.selectSgcb().sgcb = sgcb;
        return choice;
    }
}

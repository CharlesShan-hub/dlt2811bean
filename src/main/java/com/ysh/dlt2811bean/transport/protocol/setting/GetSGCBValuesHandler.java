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
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetSGCBValuesHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(GetSGCBValuesHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_SGCB_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetSGCBValues: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetSGCBValues) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsGetSGCBValues asdu = (CmsGetSGCBValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsArray<CmsErrorSgcbChoice> choices = new CmsArray<>(CmsErrorSgcbChoice::new).capacity(100);

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

    private CmsApdu buildNegativeResponse(CmsGetSGCBValues request, int errorCode) {
        CmsGetSGCBValues response = new CmsGetSGCBValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}

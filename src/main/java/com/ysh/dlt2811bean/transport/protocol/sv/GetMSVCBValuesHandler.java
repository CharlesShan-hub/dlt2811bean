package com.ysh.dlt2811bean.transport.protocol.sv;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsMSVCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsPhyComAddr;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsSmpMod;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.sv.CmsGetMSVCBValues;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsErrorMsvcbChoice;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetMSVCBValuesHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(GetMSVCBValuesHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_MSVCB_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetMSVCBValues: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetMSVCBValues) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsGetMSVCBValues asdu = (CmsGetMSVCBValues) request.getAsdu();

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();
        if (accessPoint == null || accessPoint.getServer() == null) {
            log.warn("[Server] No SCL model for session");
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsArray<CmsErrorMsvcbChoice> choices = new CmsArray<>(CmsErrorMsvcbChoice::new).capacity(100);

        for (int i = 0; i < asdu.reference.size(); i++) {
            String ref = asdu.reference.get(i).get();
            CmsErrorMsvcbChoice choice = buildMsvcbChoice(accessPoint, ref);
            choices.add(choice);
        }

        CmsGetMSVCBValues response = new CmsGetMSVCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.errorMsvcb = choices;
        response.moreFollows.set(false);

        log.debug("[Server] GetMSVCBValues: {} references", asdu.reference.size());
        return new CmsApdu(response);
    }

    private CmsErrorMsvcbChoice buildMsvcbChoice(SclIED.SclAccessPoint accessPoint, String ref) {
        CmsErrorMsvcbChoice choice = new CmsErrorMsvcbChoice();

        if (ref == null || ref.isEmpty()) {
            choice.selectError().error.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            return choice;
        }

        for (SclIED.SclLDevice ld : accessPoint.getServer().getLDevices()) {
            if (ld.getLn0() == null) continue;
            for (SclIED.SclSampledValueControl svc : ld.getLn0().getSampledValueControls()) {
                String svcRef = ld.getInst() + "/LLN0." + svc.getName();
                if (svcRef.equals(ref)) {
                    CmsMSVCB msvcb = new CmsMSVCB();
                    msvcb.msvCBName.set(svc.getName());
                    msvcb.msvCBRef.set(svcRef);
                    msvcb.msvID.set(svc.getSmvID() != null ? svc.getSmvID() : "");
                    msvcb.datSet.set(svc.getDatSet() != null ? svc.getDatSet() : "");
                    msvcb.confRev.set(svc.getConfRev() != null ? Long.parseLong(svc.getConfRev()) : 1L);
                    msvcb.smpRate.set(svc.getSmpRate());
                    msvcb.svEna.set(false);
                    msvcb.smpMod.set(CmsSmpMod.SAMPLES_PER_NOMINAL_PERIOD);
                    msvcb.dstAddress.addr(new byte[]{0x01, 0x0C, (byte)0xCD, 0x01, 0x00, 0x01})
                            .priority(4).vid(0).appid(0x0001);
                    choice.selectMsvcb().msvcb = msvcb;
                    return choice;
                }
            }
        }

        choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        return choice;
    }

    private CmsApdu buildNegativeResponse(CmsGetMSVCBValues request, int errorCode) {
        CmsGetMSVCBValues response = new CmsGetMSVCBValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}

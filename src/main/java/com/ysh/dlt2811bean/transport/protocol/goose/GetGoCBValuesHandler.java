package com.ysh.dlt2811bean.transport.protocol.goose;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsGoCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.goose.CmsGetGoCBValues;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsErrorGocbChoice;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetGoCBValuesHandler extends AbstractCmsServiceHandler<CmsGetGoCBValues> {

    static final java.util.concurrent.ConcurrentHashMap<String, Boolean> goEnaState = new java.util.concurrent.ConcurrentHashMap<>();

    public GetGoCBValuesHandler() {
        super(ServiceName.GET_GOCB_VALUES, CmsGetGoCBValues::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsGetGoCBValues asdu = (CmsGetGoCBValues) request.getAsdu();

        CmsArray<CmsErrorGocbChoice> choices = new CmsArray<>(CmsErrorGocbChoice::new);

        for (int i = 0; i < asdu.gocbReference.size(); i++) {
            String ref = asdu.gocbReference.get(i).get();
            CmsErrorGocbChoice choice = buildGocbChoice(ref);
            choices.add(choice);
        }

        CmsGetGoCBValues response = new CmsGetGoCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.errorGocb = choices;
        response.moreFollows.set(false);

        log.debug("[Server] GetGoCBValues: {} references", asdu.gocbReference.size());
        return new CmsApdu(response);
    }

    private CmsErrorGocbChoice buildGocbChoice(String ref) {
        CmsErrorGocbChoice choice = new CmsErrorGocbChoice();

        if (ref == null || ref.isEmpty()) {
            choice.selectError().error.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            return choice;
        }

        for (SclIED.SclLDevice ld : server.getLDevices()) {
            if (ld.getLn0() == null) continue;
            for (SclIED.SclGSEControl gse : ld.getLn0().getGseControls()) {
                String gseRef = ld.getInst() + "/LLN0." + gse.getName();
                if (gseRef.equals(ref)) {
                    CmsGoCB gocb = new CmsGoCB();
                    gocb.goCBName.set(gse.getName());
                    gocb.goCBRef.set(gseRef);
                    gocb.goID.set(gse.getAppID() != null ? gse.getAppID() : "");
                    gocb.datSet.set(gse.getDatSet() != null ? gse.getDatSet() : "");
                    if (gse.getConfRev() != null) {
                        gocb.confRev.set(Long.parseLong(gse.getConfRev()));
                    }
                    gocb.goEna.set(goEnaState.getOrDefault(gseRef, false));
                    gocb.ndsCom.set(false);
                    gocb.dstAddress.addr(new byte[]{0x01, 0x0C, (byte)0xCD, 0x01, 0x00, 0x01})
                            .priority(4).vid(0).appid(0x0001);
                    choice.selectGocb().gocb = gocb;
                    return choice;
                }
            }
        }

        choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        return choice;
    }
}

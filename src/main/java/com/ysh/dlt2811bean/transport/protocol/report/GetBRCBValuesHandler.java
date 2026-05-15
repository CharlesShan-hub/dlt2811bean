package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsBRCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsGetBRCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorBrcbChoice;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetBRCBValuesHandler extends AbstractCmsServiceHandler<CmsGetBRCBValues> {

    static final java.util.concurrent.ConcurrentHashMap<String, Boolean> rptEnaState = new java.util.concurrent.ConcurrentHashMap<>();

    public GetBRCBValuesHandler() {
        super(ServiceName.GET_BRCB_VALUES, CmsGetBRCBValues::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        CmsArray<CmsErrorBrcbChoice> choices = new CmsArray<>(CmsErrorBrcbChoice::new);

        for (int i = 0; i < asdu.brcbReference.size(); i++) {
            String ref = asdu.brcbReference.get(i).get();
            CmsErrorBrcbChoice choice = buildBrcbChoice(ref);
            choices.add(choice);
        }

        CmsGetBRCBValues response = new CmsGetBRCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.errorBrcb = choices;
        response.moreFollows.set(false);

        log.debug("[Server] GetBRCBValues: {} references", asdu.brcbReference.size());
        return new CmsApdu(response);
    }

    private CmsErrorBrcbChoice buildBrcbChoice(String ref) {
        CmsErrorBrcbChoice choice = new CmsErrorBrcbChoice();

        if (ref == null || ref.isEmpty()) {
            choice.selectError().error.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            return choice;
        }

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) {
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }
        String lnName = rest.substring(0, dotIdx);
        String rcName = rest.substring(dotIdx + 1);

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) {
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }

        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) {
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }

        SclReportControl rc = ln.findReportControlByName(rcName);
        if (rc == null || !rc.isBuffered()) {
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }

        CmsBRCB brcb = new CmsBRCB();
        brcb.brcbName.set(rc.getName());
        brcb.brcbRef.set(ref);
        if (rc.getRptID() != null) {
            brcb.rptID.set(rc.getRptID());
        }
        if (rc.getDatSet() != null) {
            brcb.datSet.set(rc.getDatSet());
        }
        if (rc.getConfRev() != null) {
            brcb.confRev.set(Long.parseLong(rc.getConfRev()));
        }
        brcb.rptEna.set(rptEnaState.getOrDefault(ref, false));
        brcb.purgeBuf.set(false);
        brcb.gi.set(false);
        choice.selectBrcb().brcb = brcb;
        return choice;
    }
}
package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsURCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsGetURCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorUrcbChoice;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.Map;

public class GetURCBValuesHandler extends AbstractCmsServiceHandler<CmsGetURCBValues> {

    public GetURCBValuesHandler() {
        super(ServiceName.GET_URCB_VALUES, CmsGetURCBValues::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        CmsArray<CmsErrorUrcbChoice> choices = new CmsArray<>(CmsErrorUrcbChoice::new);

        for (int i = 0; i < asdu.reference.size(); i++) {
            String ref = asdu.reference.get(i).get();
            CmsErrorUrcbChoice choice = buildUrcbChoice(ref);
            choices.add(choice);
        }

        CmsGetURCBValues response = new CmsGetURCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.urcb = choices;
        response.moreFollows.set(false);

        log.debug("[Server] GetURCBValues: {} references", asdu.reference.size());
        return new CmsApdu(response);
    }

    private CmsErrorUrcbChoice buildUrcbChoice(String ref) {
        CmsErrorUrcbChoice choice = new CmsErrorUrcbChoice();

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
        if (rc == null || rc.isBuffered()) {
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }

        Map<String, SclRCBState> rcStates = SclRCBState.getOrCreateSessionState(serverSession);
        SclRCBState rcState = rcStates.get(ref);

        CmsURCB urcb = new CmsURCB();
        urcb.urcbName.set(rc.getName());
        urcb.urcbRef.set(ref);
        if (rc.getRptID() != null) {
            urcb.rptID.set(rc.getRptID());
        }
        if (rc.getDatSet() != null) {
            urcb.datSet.set(rc.getDatSet());
        }
        if (rc.getConfRev() != null) {
            urcb.confRev.set(Long.parseLong(rc.getConfRev()));
        }
        if (rc.getOptFields() != null) {
            urcb.optFlds.set(Long.parseLong(rc.getOptFields()));
        }
        if (rc.getBufTime() != null) {
            urcb.bufTm.set(Long.parseLong(rc.getBufTime()));
        }
        if (rc.getTrgOps() != null) {
            urcb.trgOps.set(Long.parseLong(rc.getTrgOps()));
        }
        if (rc.getIntgPd() != null) {
            urcb.intgPd.set(Long.parseLong(rc.getIntgPd()));
        }
        urcb.rptEna.set(rcState != null ? rcState.isRptEna() : false);
        urcb.resv.set(false);
        urcb.gi.set(rcState != null ? rcState.isGi() : false);
        choice.selectValue().value = urcb;
        return choice;
    }
}

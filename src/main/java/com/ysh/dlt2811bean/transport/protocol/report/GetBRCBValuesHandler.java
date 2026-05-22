package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsBRCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.control.SclRCBState;
import com.ysh.dlt2811bean.scl.model.control.SclReportControl;
import com.ysh.dlt2811bean.scl.model.ied.SclLDevice;
import com.ysh.dlt2811bean.scl.model.ied.SclLN;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsGetBRCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorBrcbChoice;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

import java.util.Map;

public class GetBRCBValuesHandler extends AbstractCmsServiceHandler<CmsGetBRCBValues> {

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
        System.out.println("  [DEBUG] GetBRCBValues: ref=" + ref);
        CmsErrorBrcbChoice choice = new CmsErrorBrcbChoice();

        if (ref == null || ref.isEmpty()) {
            System.out.println("  [DEBUG] GetBRCBValues: ref is null/empty");
            choice.selectError().error.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            return choice;
        }

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            System.out.println("  [DEBUG] GetBRCBValues: no slash in ref '" + ref + "'");
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) {
            System.out.println("  [DEBUG] GetBRCBValues: no dot in '" + rest + "'");
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }
        String lnName = rest.substring(0, dotIdx);
        String rcName = rest.substring(dotIdx + 1);

        System.out.println("  [DEBUG] GetBRCBValues: ldName=" + ldName + ", lnName=" + lnName + ", rcName=" + rcName);
        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) {
            System.out.println("  [DEBUG] GetBRCBValues: LDevice '" + ldName + "' not found (available: " + server.getLDeviceNames() + ")");
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }

        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) {
            System.out.println("  [DEBUG] GetBRCBValues: LN '" + lnName + "' not found in LDevice '" + ldName + "'");
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }

        SclReportControl rc = ln.findReportControlByName(rcName);
        if (rc == null || !rc.isBuffered()) {
            System.out.println("  [DEBUG] GetBRCBValues: report control '" + rcName + "' found=" + (rc != null) + " buffered=" + (rc != null ? rc.isBuffered() : "N/A"));
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }

        Map<String, SclRCBState> rcStates = SclRCBState.getOrCreateSessionState(serverSession);
        SclRCBState rcState = rcStates.get(ref);
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
        if (rc.getOptFields() != null) {
            brcb.optFlds.set(Long.parseLong(rc.getOptFields()));
        }
        if (rc.getBufTime() != null) {
            brcb.bufTm.set(Long.parseLong(rc.getBufTime()));
        }
        if (rc.getTrgOps() != null) {
            brcb.trgOps.set(Long.parseLong(rc.getTrgOps()));
        }
        if (rc.getIntgPd() != null) {
            brcb.intgPd.set(Long.parseLong(rc.getIntgPd()));
        }
        brcb.rptEna.set(rcState != null ? rcState.isRptEna() : false);
        brcb.purgeBuf.set(rcState != null ? rcState.isPurgeBuf() : false);
        brcb.gi.set(rcState != null ? rcState.isGi() : false);
        if (rcState != null && rcState.getEntryID() != null) {
            brcb.entryID.set(rcState.getEntryID());
        }
        if (rcState != null) {
            brcb.resvTms.set(rcState.getResvTms());
        }
        choice.selectBrcb().brcb = brcb;
        return choice;
    }
}

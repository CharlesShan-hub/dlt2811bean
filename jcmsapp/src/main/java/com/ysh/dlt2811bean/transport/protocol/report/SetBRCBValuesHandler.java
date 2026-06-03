package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.datatypes.code.CmsRcbOptFlds;
import com.ysh.dlt2811bean.datatypes.code.CmsTriggerConditions;
import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.control.SclRCBState;
import com.ysh.dlt2811bean.scl.model.control.SclReportControl;
import com.ysh.dlt2811bean.scl.model.ied.SclLDevice;
import com.ysh.dlt2811bean.scl.model.ied.SclLN;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsSetBRCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetBRCBValuesEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import com.ysh.dlt2811bean.transport.report.ReportEngine;
import java.util.Map;

public class SetBRCBValuesHandler extends AbstractCmsServiceHandler<CmsSetBRCBValues> {

    public SetBRCBValuesHandler() {
        super(ServiceName.SET_BRCB_VALUES, CmsSetBRCBValues::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        if (asdu.brcb == null || asdu.brcb.isEmpty()) {
            log.debug("[Server] SetBRCBValues: empty sequence, returning Response+");
            return new CmsApdu(new CmsSetBRCBValues(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get()));
        }

        Map<String, SclRCBState> rcStates = SclRCBState.getOrCreateSessionState(serverSession);
        CmsArray<CmsServiceError> results = new CmsArray<>(CmsServiceError::new);
        boolean hasAnyError = false;

        for (CmsSetBRCBValuesEntry entry : asdu.brcb) {
            int error = processEntry(entry, rcStates);
            results.add(new CmsServiceError(error));
            if (error != CmsServiceError.NO_ERROR) {
                hasAnyError = true;
            }
        }

        if (hasAnyError) {
            CmsSetBRCBValues response = new CmsSetBRCBValues(MessageType.RESPONSE_NEGATIVE)
                    .reqId(asdu.reqId().get());
            response.result = results;
            log.debug("[Server] SetBRCBValues: {} entries with errors", results.size());
            return new CmsApdu(response);
        }

        CmsSetBRCBValues response = new CmsSetBRCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        log.debug("[Server] SetBRCBValues: {} entries accepted", results.size());
        return new CmsApdu(response);
    }

    private int processEntry(CmsSetBRCBValuesEntry entry, Map<String, SclRCBState> rcStates) {
        String ref = entry.reference.get();

        int refError = validateBrcbRef(ref);
        if (refError != CmsServiceError.NO_ERROR) {
            return refError;
        }

        SclReportControl rc = findBrcbByRef(ref);
        if (rc == null) {
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }

        SclRCBState rcState = rcStates.computeIfAbsent(ref, k -> new SclRCBState());

        boolean hasRptEna = entry.isFieldPresent("rptEna");
        boolean rptEnaVal = hasRptEna && entry.rptEna.get();

        if (hasRptEna && !rptEnaVal) {
            rcState.setRptEna(false);
            setOtherFields(entry, rcState, rc);
            notifyReportEngine(false, ref, rc, entry);
            return CmsServiceError.NO_ERROR;
        } else if (hasRptEna && rptEnaVal) {
            setOtherFields(entry, rcState, rc);
            rcState.setRptEna(true);
            notifyReportEngine(true, ref, rc, entry);
            return CmsServiceError.NO_ERROR;
        } else {
            setOtherFields(entry, rcState, rc);
            return CmsServiceError.NO_ERROR;
        }
    }

    private void notifyReportEngine(boolean enable, String ref, SclReportControl rc, CmsSetBRCBValuesEntry entry) {
        if (serverSession == null) return;
        ReportEngine engine = (ReportEngine) serverSession.getAttribute("reportEngine");
        if (engine == null) return;

        if (enable) {
            CmsRcbOptFlds optFlds = entry.isFieldPresent("optFlds") ?
                    new CmsRcbOptFlds(entry.optFlds.get()) : new CmsRcbOptFlds();
            CmsTriggerConditions trgOps = entry.isFieldPresent("trgOps") ?
                    new CmsTriggerConditions(entry.trgOps.get()) : new CmsTriggerConditions();
            long bufTm = entry.isFieldPresent("bufTm") ? entry.bufTm.get() : 0;
            long intgPd = entry.isFieldPresent("intgPd") ? entry.intgPd.get() : 0;
            engine.enableReport(serverSession, ref, rc, optFlds, trgOps, bufTm, intgPd, 0);
        } else {
            engine.disableReport(ref, serverSession);
        }
    }

    private SclReportControl findBrcbByRef(String ref) {
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return null;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) return null;
        String lnName = rest.substring(0, dotIdx);
        String rcName = rest.substring(dotIdx + 1);

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) return null;

        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) return null;

        return ln.findReportControlByName(rcName);
    }

    private void setOtherFields(CmsSetBRCBValuesEntry entry, SclRCBState rcState, SclReportControl rc) {
        if (entry.isFieldPresent("rptID")) {
            rc.setRptID(entry.rptID.get());
        }
        if (entry.isFieldPresent("datSet")) {
            rc.setDatSet(entry.datSet.get());
        }
        if (entry.isFieldPresent("optFlds")) {
            rc.setOptFields(String.valueOf(entry.optFlds.get()));
        }
        if (entry.isFieldPresent("bufTm")) {
            rc.setBufTime(String.valueOf(entry.bufTm.get()));
        }
        if (entry.isFieldPresent("trgOps")) {
            rc.setTrgOps(String.valueOf(entry.trgOps.get()));
        }
        if (entry.isFieldPresent("intgPd")) {
            rc.setIntgPd(String.valueOf(entry.intgPd.get()));
        }
        if (entry.isFieldPresent("gi")) {
            rcState.setGi(entry.gi.get());
        }
        if (entry.isFieldPresent("purgeBuf")) {
            rcState.setPurgeBuf(entry.purgeBuf.get());
        }
        if (entry.isFieldPresent("entryID")) {
            rcState.setEntryID(entry.entryID.get());
        }
        if (entry.isFieldPresent("resvTms")) {
            rcState.setResvTms(entry.resvTms.get());
        }
    }

    private int validateBrcbRef(String ref) {
        if (ref == null || ref.isEmpty()) {
            return CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE;
        }

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        String lnName = rest.substring(0, dotIdx);
        String rcName = rest.substring(dotIdx + 1);

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) return CmsServiceError.INSTANCE_NOT_AVAILABLE;

        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) return CmsServiceError.INSTANCE_NOT_AVAILABLE;

        SclReportControl rc = ln.findReportControlByName(rcName);
        if (rc == null || !rc.isBuffered()) return CmsServiceError.INSTANCE_NOT_AVAILABLE;

        return CmsServiceError.NO_ERROR;
    }
}

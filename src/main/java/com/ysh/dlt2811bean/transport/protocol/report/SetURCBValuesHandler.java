package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.datatypes.code.CmsRcbOptFlds;
import com.ysh.dlt2811bean.datatypes.code.CmsTriggerConditions;
import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsSetURCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesResultEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import com.ysh.dlt2811bean.transport.report.ReportEngine;
import java.util.Map;

public class SetURCBValuesHandler extends AbstractCmsServiceHandler<CmsSetURCBValues> {

    public SetURCBValuesHandler() {
        super(ServiceName.SET_URCB_VALUES, CmsSetURCBValues::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        if (asdu.urcb == null || asdu.urcb.isEmpty()) {
            log.debug("[Server] SetURCBValues: empty sequence, returning Response+");
            return new CmsApdu(new CmsSetURCBValues(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get()));
        }

        Map<String, SclRCBState> rcStates = SclRCBState.getOrCreateSessionState(serverSession);
        CmsArray<CmsSetURCBValuesResultEntry> results = new CmsArray<>(CmsSetURCBValuesResultEntry::new);
        boolean hasAnyError = false;

        for (CmsSetURCBValuesEntry entry : asdu.urcb) {
            String ref = entry.reference.get();

            int refError = validateUrcbRef(ref);
            if (refError != CmsServiceError.NO_ERROR) {
                CmsSetURCBValuesResultEntry result = new CmsSetURCBValuesResultEntry();
                result.error.set(refError);
                results.add(result);
                hasAnyError = true;
                continue;
            }

            CmsSetURCBValuesResultEntry result = processEntry(entry, ref, rcStates);
            results.add(result);
            if (hasPerFieldError(result)) {
                hasAnyError = true;
            }
        }

        if (hasAnyError) {
            CmsSetURCBValues response = new CmsSetURCBValues(MessageType.RESPONSE_NEGATIVE)
                    .reqId(asdu.reqId().get());
            response.result = results;
            log.debug("[Server] SetURCBValues: {} entries with errors", results.size());
            return new CmsApdu(response);
        }

        CmsSetURCBValues response = new CmsSetURCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        log.debug("[Server] SetURCBValues: {} entries accepted", results.size());
        return new CmsApdu(response);
    }

    private CmsSetURCBValuesResultEntry processEntry(CmsSetURCBValuesEntry entry, String ref, Map<String, SclRCBState> rcStates) {
        CmsSetURCBValuesResultEntry result = new CmsSetURCBValuesResultEntry();

        SclReportControl rc = findUrcbByRef(ref);
        if (rc == null) {
            result.error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        SclRCBState rcState = rcStates.computeIfAbsent(ref, k -> new SclRCBState());

        boolean hasRptEna = entry.isFieldPresent("rptEna");
        boolean rptEnaVal = hasRptEna && entry.rptEna.get();

        if (hasRptEna && !rptEnaVal) {
            rcState.setRptEna(false);
            result.rptEna.set(CmsServiceError.NO_ERROR);
            setOtherFields(result, entry, rcState, rc);
            notifyReportEngine(false, ref, rc, entry);
        } else if (hasRptEna && rptEnaVal) {
            setOtherFields(result, entry, rcState, rc);
            result.rptEna.set(CmsServiceError.NO_ERROR);
            rcState.setRptEna(true);
            notifyReportEngine(true, ref, rc, entry);
        } else {
            setOtherFields(result, entry, rcState, rc);
        }

        return result;
    }

    private void notifyReportEngine(boolean enable, String ref, SclReportControl rc, CmsSetURCBValuesEntry entry) {
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

    private boolean setOtherFields(CmsSetURCBValuesResultEntry result, CmsSetURCBValuesEntry entry, SclRCBState rcState, SclReportControl rc) {
        boolean allOk = true;

        if (entry.isFieldPresent("rptID")) {
            if (entry.rptID.get() != null && !entry.rptID.get().isEmpty()) {
                rc.setRptID(entry.rptID.get());
                result.rptID.set(CmsServiceError.NO_ERROR);
            } else {
                result.rptID.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
                allOk = false;
            }
        }
        if (entry.isFieldPresent("datSet")) {
            if (entry.datSet.get() != null && !entry.datSet.get().isEmpty()) {
                rc.setDatSet(entry.datSet.get());
                result.datSet.set(CmsServiceError.NO_ERROR);
            } else {
                result.datSet.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
                allOk = false;
            }
        }
        if (entry.isFieldPresent("optFlds")) {
            rc.setOptFields(String.valueOf(entry.optFlds.get()));
            result.optFlds.set(CmsServiceError.NO_ERROR);
        }
        if (entry.isFieldPresent("bufTm")) {
            rc.setBufTime(String.valueOf(entry.bufTm.get()));
            result.bufTm.set(CmsServiceError.NO_ERROR);
        }
        if (entry.isFieldPresent("trgOps")) {
            rc.setTrgOps(String.valueOf(entry.trgOps.get()));
            result.trgOps.set(CmsServiceError.NO_ERROR);
        }
        if (entry.isFieldPresent("intgPd")) {
            rc.setIntgPd(String.valueOf(entry.intgPd.get()));
            result.intgPd.set(CmsServiceError.NO_ERROR);
        }
        if (entry.isFieldPresent("gi")) {
            rcState.setGi(entry.gi.get());
            result.gi.set(CmsServiceError.NO_ERROR);
        }
        if (entry.isFieldPresent("resv")) {
            rc.setRptEnabled(String.valueOf(entry.resv.get()));
            result.resv.set(CmsServiceError.NO_ERROR);
        }

        return allOk;
    }

    private boolean hasPerFieldError(CmsSetURCBValuesResultEntry result) {
        return result.error.get() != CmsServiceError.NO_ERROR
                || result.rptID.get() != CmsServiceError.NO_ERROR
                || result.rptEna.get() != CmsServiceError.NO_ERROR
                || result.datSet.get() != CmsServiceError.NO_ERROR
                || result.optFlds.get() != CmsServiceError.NO_ERROR
                || result.bufTm.get() != CmsServiceError.NO_ERROR
                || result.trgOps.get() != CmsServiceError.NO_ERROR
                || result.intgPd.get() != CmsServiceError.NO_ERROR
                || result.gi.get() != CmsServiceError.NO_ERROR
                || result.resv.get() != CmsServiceError.NO_ERROR;
    }

    private int validateUrcbRef(String ref) {
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
        if (rc == null || rc.isBuffered()) return CmsServiceError.INSTANCE_NOT_AVAILABLE;

        return CmsServiceError.NO_ERROR;
    }

    private SclReportControl findUrcbByRef(String ref) {
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
}

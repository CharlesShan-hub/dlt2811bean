package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.SclLDevice;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.scl2.model.SclReportControl;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsSetURCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesResultEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import static com.ysh.dlt2811bean.transport.protocol.report.GetURCBValuesHandler.rptEnaState;

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

            CmsSetURCBValuesResultEntry result = processEntry(entry, ref);
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

    private CmsSetURCBValuesResultEntry processEntry(CmsSetURCBValuesEntry entry, String ref) {
        CmsSetURCBValuesResultEntry result = new CmsSetURCBValuesResultEntry();

        boolean hasRptEna = entry.isFieldPresent("rptEna");
        boolean rptEnaVal = hasRptEna && entry.rptEna.get();

        if (hasRptEna && !rptEnaVal) {
            rptEnaState.put(ref, false);
            result.rptEna.set(CmsServiceError.NO_ERROR);
            setOtherFields(result, entry);
        } else if (hasRptEna && rptEnaVal) {
            boolean othersOk = setOtherFields(result, entry);
            result.rptEna.set(othersOk ? CmsServiceError.NO_ERROR : CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE);
            if (othersOk) {
                rptEnaState.put(ref, true);
            }
        } else {
            setOtherFields(result, entry);
        }

        return result;
    }

    private boolean setOtherFields(CmsSetURCBValuesResultEntry result, CmsSetURCBValuesEntry entry) {
        boolean allOk = true;

        if (entry.isFieldPresent("rptID")) {
            if (entry.rptID.get() != null && !entry.rptID.get().isEmpty()) {
                result.rptID.set(CmsServiceError.NO_ERROR);
            } else {
                result.rptID.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
                allOk = false;
            }
        }
        if (entry.isFieldPresent("datSet")) {
            if (entry.datSet.get() != null && !entry.datSet.get().isEmpty()) {
                result.datSet.set(CmsServiceError.NO_ERROR);
            } else {
                result.datSet.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
                allOk = false;
            }
        }
        if (entry.isFieldPresent("optFlds")) {
            result.optFlds.set(CmsServiceError.NO_ERROR);
        }
        if (entry.isFieldPresent("bufTm")) {
            result.bufTm.set(CmsServiceError.NO_ERROR);
        }
        if (entry.isFieldPresent("trgOps")) {
            result.trgOps.set(CmsServiceError.NO_ERROR);
        }
        if (entry.isFieldPresent("intgPd")) {
            result.intgPd.set(CmsServiceError.NO_ERROR);
        }
        if (entry.isFieldPresent("gi")) {
            result.gi.set(CmsServiceError.NO_ERROR);
        }
        if (entry.isFieldPresent("resv")) {
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
}

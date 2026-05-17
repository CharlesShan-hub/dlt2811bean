package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.SclLDevice;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.scl2.model.SclReportControl;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsSetBRCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetBRCBValuesEntry;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import static com.ysh.dlt2811bean.transport.protocol.report.GetBRCBValuesHandler.rptEnaState;

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

        CmsArray<CmsServiceError> results = new CmsArray<>(CmsServiceError::new);
        boolean hasAnyError = false;

        for (CmsSetBRCBValuesEntry entry : asdu.brcb) {
            int error = processEntry(entry);
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

    private int processEntry(CmsSetBRCBValuesEntry entry) {
        String ref = entry.reference.get();

        int refError = validateBrcbRef(ref);
        if (refError != CmsServiceError.NO_ERROR) {
            return refError;
        }

        boolean hasRptEna = entry.isFieldPresent("rptEna");
        boolean rptEnaVal = hasRptEna && entry.rptEna.get();

        if (hasRptEna && !rptEnaVal) {
            rptEnaState.put(ref, false);
            boolean otherOk = setOtherFields(entry);
            return otherOk ? CmsServiceError.NO_ERROR : CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE;
        } else if (hasRptEna && rptEnaVal) {
            boolean otherOk = setOtherFields(entry);
            if (otherOk) {
                rptEnaState.put(ref, true);
                return CmsServiceError.NO_ERROR;
            }
            return CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE;
        } else {
            return setOtherFields(entry) ? CmsServiceError.NO_ERROR : CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE;
        }
    }

    private boolean setOtherFields(CmsSetBRCBValuesEntry entry) {
        return true;
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
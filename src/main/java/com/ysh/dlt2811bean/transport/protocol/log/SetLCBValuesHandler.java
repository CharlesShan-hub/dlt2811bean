package com.ysh.dlt2811bean.transport.protocol.log;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSetLCBValues;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsSetLCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsSetLCBValuesResultEntry;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class SetLCBValuesHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(SetLCBValuesHandler.class);

    private enum LcbField {
        LOG_ENA, DAT_SET, TRG_OPS, INTG_PD, LOG_REF, OPT_FLDS, BUF_TM
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_LCB_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling SetLCBValues: {}", e.getMessage(), e);
            int reqId = request != null ? ((CmsSetLCBValues) request.getAsdu()).reqId().get() : 0;
            return buildNegativeAllError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsSetLCBValues asdu = (CmsSetLCBValues) request.getAsdu();

        if (asdu.lcb == null || asdu.lcb.size() == 0) {
            log.debug("[Server] SetLCBValues: empty sequence, returning Response+");
            return new CmsApdu(new CmsSetLCBValues(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get()));
        }

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();

        CmsArray<CmsSetLCBValuesResultEntry> results = new CmsArray<>(CmsSetLCBValuesResultEntry::new).capacity(100);
        boolean hasAnyError = false;

        for (int i = 0; i < asdu.lcb.size(); i++) {
            CmsSetLCBValuesEntry entry = asdu.lcb.get(i);

            // Validate reference exists
            int refError = validateReference(accessPoint, entry.reference.get());
            if (refError != CmsServiceError.NO_ERROR) {
                CmsSetLCBValuesResultEntry result = new CmsSetLCBValuesResultEntry();
                result.error.set(refError);
                results.add(result);
                hasAnyError = true;
                continue;
            }

            CmsSetLCBValuesResultEntry result = processEntry(entry);
            results.add(result);
            if (hasPerFieldError(result)) {
                hasAnyError = true;
            }
        }

        if (hasAnyError) {
            CmsSetLCBValues response = new CmsSetLCBValues(MessageType.RESPONSE_NEGATIVE)
                    .reqId(asdu.reqId().get());
            response.result = results;
            log.debug("[Server] SetLCBValues: {} entries with errors", results.size());
            return new CmsApdu(response);
        }

        CmsSetLCBValues response = new CmsSetLCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        log.debug("[Server] SetLCBValues: {} entries accepted", results.size());
        return new CmsApdu(response);
    }

    /**
     * Processes a single SetLCBValuesEntry according to §8.8.3.2:
     * <ul>
     *   <li>(b) logEna=False → set logEna first, then other properties</li>
     *   <li>(b) logEna=True → set other properties first, then logEna.
     *          If other properties fail, do not set logEna=True.</li>
     *   <li>(a) Other properties are independent — one failure does not block others.</li>
     * </ul>
     */
    private CmsSetLCBValuesResultEntry processEntry(CmsSetLCBValuesEntry entry) {
        CmsSetLCBValuesResultEntry result = new CmsSetLCBValuesResultEntry();

        Set<LcbField> requested = getRequestedFields(entry);

        if (requested.contains(LcbField.LOG_ENA) && !entry.logEna.get()) {
            // logEna=False: set logEna first
            result.logEna.set(CmsServiceError.NO_ERROR);
            requested.remove(LcbField.LOG_ENA);
            setOtherFields(result, entry, requested);
        } else if (requested.contains(LcbField.LOG_ENA) && entry.logEna.get()) {
            // logEna=True: set other properties first
            requested.remove(LcbField.LOG_ENA);
            boolean othersOk = setOtherFields(result, entry, requested);
            // Only set logEna=True if all others succeeded
            result.logEna.set(othersOk ? CmsServiceError.NO_ERROR : CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE);
        } else {
            setOtherFields(result, entry, requested);
        }

        return result;
    }

    private boolean setOtherFields(CmsSetLCBValuesResultEntry result, CmsSetLCBValuesEntry entry, Set<LcbField> fields) {
        boolean allOk = true;

        if (fields.contains(LcbField.DAT_SET)) {
            if (entry.datSet.get() != null && !entry.datSet.get().isEmpty()) {
                result.datSet.set(CmsServiceError.NO_ERROR);
            } else {
                result.datSet.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
                allOk = false;
            }
        }
        if (fields.contains(LcbField.TRG_OPS)) {
            result.trgOps.set(CmsServiceError.NO_ERROR);
        }
        if (fields.contains(LcbField.INTG_PD)) {
            result.intgPd.set(CmsServiceError.NO_ERROR);
        }
        if (fields.contains(LcbField.LOG_REF)) {
            if (entry.logRef.get() != null && !entry.logRef.get().isEmpty()) {
                result.logRef.set(CmsServiceError.NO_ERROR);
            } else {
                result.logRef.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
                allOk = false;
            }
        }
        if (fields.contains(LcbField.OPT_FLDS)) {
            result.optFlds.set(CmsServiceError.NO_ERROR);
        }
        if (fields.contains(LcbField.BUF_TM)) {
            result.bufTm.set(CmsServiceError.NO_ERROR);
        }

        return allOk;
    }

    private Set<LcbField> getRequestedFields(CmsSetLCBValuesEntry entry) {
        Set<LcbField> fields = EnumSet.noneOf(LcbField.class);
        if (entry.isFieldPresent("logEna")) fields.add(LcbField.LOG_ENA);
        if (entry.isFieldPresent("datSet")) fields.add(LcbField.DAT_SET);
        if (entry.isFieldPresent("trgOps")) fields.add(LcbField.TRG_OPS);
        if (entry.isFieldPresent("intgPd")) fields.add(LcbField.INTG_PD);
        if (entry.isFieldPresent("logRef")) fields.add(LcbField.LOG_REF);
        if (entry.isFieldPresent("optFlds")) fields.add(LcbField.OPT_FLDS);
        if (entry.isFieldPresent("bufTm")) fields.add(LcbField.BUF_TM);
        return fields;
    }

    private boolean hasPerFieldError(CmsSetLCBValuesResultEntry result) {
        return result.error.get() != CmsServiceError.NO_ERROR
                || result.logEna.get() != CmsServiceError.NO_ERROR
                || result.datSet.get() != CmsServiceError.NO_ERROR
                || result.trgOps.get() != CmsServiceError.NO_ERROR
                || result.intgPd.get() != CmsServiceError.NO_ERROR
                || result.logRef.get() != CmsServiceError.NO_ERROR
                || result.optFlds.get() != CmsServiceError.NO_ERROR
                || result.bufTm.get() != CmsServiceError.NO_ERROR;
    }

    private int validateReference(SclIED.SclAccessPoint accessPoint, String ref) {
        if (ref == null || ref.isEmpty()) {
            return CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE;
        }
        if (accessPoint == null || accessPoint.getServer() == null) {
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }
        for (SclIED.SclLDevice ld : accessPoint.getServer().getLDevices()) {
            if (ld.getLn0() == null) continue;
            for (SclIED.SclLogControl lc : ld.getLn0().getLogControls()) {
                String lcRef = ld.getInst() + "/LLN0." + lc.getName();
                if (lcRef.equals(ref)) {
                    return CmsServiceError.NO_ERROR;
                }
            }
        }
        return CmsServiceError.INSTANCE_NOT_AVAILABLE;
    }

    private CmsApdu buildNegativeAllError(int reqId, int errorCode) {
        CmsSetLCBValues response = new CmsSetLCBValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(reqId);
        CmsSetLCBValuesResultEntry entry = new CmsSetLCBValuesResultEntry();
        entry.error.set(errorCode);
        response.result = new CmsArray<>(CmsSetLCBValuesResultEntry::new).capacity(1);
        response.result.add(entry);
        return new CmsApdu(response);
    }
}

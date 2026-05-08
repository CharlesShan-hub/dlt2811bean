package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsSetBRCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetBRCBValuesEntry;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.Set;

public class SetBRCBValuesHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(SetBRCBValuesHandler.class);

    private enum BrcbField {
        RPT_ID, RPT_ENA, DAT_SET, OPT_FLDS, BUF_TM, TRG_OPS, INTG_PD, GI, PURGE_BUF, ENTRY_ID, RESV_TMS
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_BRCB_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling SetBRCBValues: {}", e.getMessage(), e);
            int reqId = request != null ? ((CmsSetBRCBValues) request.getAsdu()).reqId().get() : 0;
            return buildNegativeResponse(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsSetBRCBValues asdu = (CmsSetBRCBValues) request.getAsdu();

        if (asdu.brcb == null || asdu.brcb.size() == 0) {
            log.debug("[Server] SetBRCBValues: empty sequence, returning Response+");
            return new CmsApdu(new CmsSetBRCBValues(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get()));
        }

        SclIED.SclAccessPoint accessPoint = session.getSclAccessPoint();

        CmsArray<CmsServiceError> results = new CmsArray<>(CmsServiceError::new).capacity(100);
        boolean hasAnyError = false;

        for (int i = 0; i < asdu.brcb.size(); i++) {
            CmsSetBRCBValuesEntry entry = asdu.brcb.get(i);

            int refError = validateReference(accessPoint, entry.reference.get());
            if (refError != CmsServiceError.NO_ERROR) {
                results.add(new CmsServiceError(refError));
                hasAnyError = true;
                continue;
            }

            int entryError = processEntry(entry);
            results.add(new CmsServiceError(entryError));
            if (entryError != CmsServiceError.NO_ERROR) {
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
        Set<BrcbField> requested = getRequestedFields(entry);

        if (requested.contains(BrcbField.RPT_ENA) && !entry.rptEna.get()) {
            requested.remove(BrcbField.RPT_ENA);
            return setOtherFields(entry, requested) ? CmsServiceError.NO_ERROR : CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE;
        } else if (requested.contains(BrcbField.RPT_ENA) && entry.rptEna.get()) {
            requested.remove(BrcbField.RPT_ENA);
            boolean othersOk = setOtherFields(entry, requested);
            return othersOk ? CmsServiceError.NO_ERROR : CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE;
        } else {
            return setOtherFields(entry, requested) ? CmsServiceError.NO_ERROR : CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE;
        }
    }

    private boolean setOtherFields(CmsSetBRCBValuesEntry entry, Set<BrcbField> fields) {
        boolean allOk = true;

        if (fields.contains(BrcbField.RPT_ID)) {
            if (entry.rptID.get() != null && !entry.rptID.get().isEmpty()) {
                // accepted
            } else {
                allOk = false;
            }
        }
        if (fields.contains(BrcbField.DAT_SET)) {
            if (entry.datSet.get() != null && !entry.datSet.get().isEmpty()) {
                // accepted
            } else {
                allOk = false;
            }
        }
        if (fields.contains(BrcbField.OPT_FLDS)) {
            // accepted
        }
        if (fields.contains(BrcbField.BUF_TM)) {
            // accepted
        }
        if (fields.contains(BrcbField.TRG_OPS)) {
            // accepted
        }
        if (fields.contains(BrcbField.INTG_PD)) {
            // accepted
        }
        if (fields.contains(BrcbField.GI)) {
            // accepted
        }
        if (fields.contains(BrcbField.PURGE_BUF)) {
            // accepted
        }
        if (fields.contains(BrcbField.ENTRY_ID)) {
            // accepted
        }
        if (fields.contains(BrcbField.RESV_TMS)) {
            // accepted
        }

        return allOk;
    }

    private Set<BrcbField> getRequestedFields(CmsSetBRCBValuesEntry entry) {
        Set<BrcbField> fields = EnumSet.noneOf(BrcbField.class);
        if (entry.isFieldPresent("rptID")) fields.add(BrcbField.RPT_ID);
        if (entry.isFieldPresent("rptEna")) fields.add(BrcbField.RPT_ENA);
        if (entry.isFieldPresent("datSet")) fields.add(BrcbField.DAT_SET);
        if (entry.isFieldPresent("optFlds")) fields.add(BrcbField.OPT_FLDS);
        if (entry.isFieldPresent("bufTm")) fields.add(BrcbField.BUF_TM);
        if (entry.isFieldPresent("trgOps")) fields.add(BrcbField.TRG_OPS);
        if (entry.isFieldPresent("intgPd")) fields.add(BrcbField.INTG_PD);
        if (entry.isFieldPresent("gi")) fields.add(BrcbField.GI);
        if (entry.isFieldPresent("purgeBuf")) fields.add(BrcbField.PURGE_BUF);
        if (entry.isFieldPresent("entryID")) fields.add(BrcbField.ENTRY_ID);
        if (entry.isFieldPresent("resvTms")) fields.add(BrcbField.RESV_TMS);
        return fields;
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
            for (SclIED.SclReportControl rc : ld.getLn0().getReportControls()) {
                if (!rc.isBuffered()) continue;
                String rcRef = ld.getInst() + "/LLN0." + rc.getName();
                if (rcRef.equals(ref)) {
                    return CmsServiceError.NO_ERROR;
                }
            }
        }
        return CmsServiceError.INSTANCE_NOT_AVAILABLE;
    }

    private CmsApdu buildNegativeResponse(int reqId, int errorCode) {
        CmsSetBRCBValues response = new CmsSetBRCBValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(reqId)
                .addResult(errorCode);
        return new CmsApdu(response);
    }
}

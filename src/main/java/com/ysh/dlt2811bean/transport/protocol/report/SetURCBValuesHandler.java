package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsSetURCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesResultEntry;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.EnumSet;
import java.util.Set;

public class SetURCBValuesHandler extends AbstractCmsServiceHandler<CmsSetURCBValues> {

    private enum UrcbField {
        RPT_ID, RPT_ENA, DAT_SET, OPT_FLDS, BUF_TM, TRG_OPS, INTG_PD, GI, RESV
    }

    public SetURCBValuesHandler() {
        super(ServiceName.SET_URCB_VALUES, CmsSetURCBValues::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsSetURCBValues asdu = (CmsSetURCBValues) request.getAsdu();

        if (asdu.urcb == null || asdu.urcb.size() == 0) {
            log.debug("[Server] SetURCBValues: empty sequence, returning Response+");
            return new CmsApdu(new CmsSetURCBValues(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get()));
        }

        SclIED.SclAccessPoint accessPoint = serverSession.getSclAccessPoint();

        CmsArray<CmsSetURCBValuesResultEntry> results = new CmsArray<>(CmsSetURCBValuesResultEntry::new);
        boolean hasAnyError = false;

        for (int i = 0; i < asdu.urcb.size(); i++) {
            CmsSetURCBValuesEntry entry = asdu.urcb.get(i);

            int refError = validateReference(accessPoint, entry.reference.get());
            if (refError != CmsServiceError.NO_ERROR) {
                CmsSetURCBValuesResultEntry result = new CmsSetURCBValuesResultEntry();
                result.error.set(refError);
                results.add(result);
                hasAnyError = true;
                continue;
            }

            CmsSetURCBValuesResultEntry result = processEntry(entry);
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

    private CmsSetURCBValuesResultEntry processEntry(CmsSetURCBValuesEntry entry) {
        CmsSetURCBValuesResultEntry result = new CmsSetURCBValuesResultEntry();

        Set<UrcbField> requested = getRequestedFields(entry);

        if (requested.contains(UrcbField.RPT_ENA) && !entry.rptEna.get()) {
            result.rptEna.set(CmsServiceError.NO_ERROR);
            requested.remove(UrcbField.RPT_ENA);
            setOtherFields(result, entry, requested);
        } else if (requested.contains(UrcbField.RPT_ENA) && entry.rptEna.get()) {
            requested.remove(UrcbField.RPT_ENA);
            boolean othersOk = setOtherFields(result, entry, requested);
            result.rptEna.set(othersOk ? CmsServiceError.NO_ERROR : CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE);
        } else {
            setOtherFields(result, entry, requested);
        }

        return result;
    }

    private boolean setOtherFields(CmsSetURCBValuesResultEntry result, CmsSetURCBValuesEntry entry, Set<UrcbField> fields) {
        boolean allOk = true;

        if (fields.contains(UrcbField.RPT_ID)) {
            if (entry.rptID.get() != null && !entry.rptID.get().isEmpty()) {
                result.rptID.set(CmsServiceError.NO_ERROR);
            } else {
                result.rptID.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
                allOk = false;
            }
        }
        if (fields.contains(UrcbField.DAT_SET)) {
            if (entry.datSet.get() != null && !entry.datSet.get().isEmpty()) {
                result.datSet.set(CmsServiceError.NO_ERROR);
            } else {
                result.datSet.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
                allOk = false;
            }
        }
        if (fields.contains(UrcbField.OPT_FLDS)) {
            result.optFlds.set(CmsServiceError.NO_ERROR);
        }
        if (fields.contains(UrcbField.BUF_TM)) {
            result.bufTm.set(CmsServiceError.NO_ERROR);
        }
        if (fields.contains(UrcbField.TRG_OPS)) {
            result.trgOps.set(CmsServiceError.NO_ERROR);
        }
        if (fields.contains(UrcbField.INTG_PD)) {
            result.intgPd.set(CmsServiceError.NO_ERROR);
        }
        if (fields.contains(UrcbField.GI)) {
            result.gi.set(CmsServiceError.NO_ERROR);
        }
        if (fields.contains(UrcbField.RESV)) {
            result.resv.set(CmsServiceError.NO_ERROR);
        }

        return allOk;
    }

    private Set<UrcbField> getRequestedFields(CmsSetURCBValuesEntry entry) {
        Set<UrcbField> fields = EnumSet.noneOf(UrcbField.class);
        if (entry.isFieldPresent("rptID")) fields.add(UrcbField.RPT_ID);
        if (entry.isFieldPresent("rptEna")) fields.add(UrcbField.RPT_ENA);
        if (entry.isFieldPresent("datSet")) fields.add(UrcbField.DAT_SET);
        if (entry.isFieldPresent("optFlds")) fields.add(UrcbField.OPT_FLDS);
        if (entry.isFieldPresent("bufTm")) fields.add(UrcbField.BUF_TM);
        if (entry.isFieldPresent("trgOps")) fields.add(UrcbField.TRG_OPS);
        if (entry.isFieldPresent("intgPd")) fields.add(UrcbField.INTG_PD);
        if (entry.isFieldPresent("gi")) fields.add(UrcbField.GI);
        if (entry.isFieldPresent("resv")) fields.add(UrcbField.RESV);
        return fields;
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
                if (rc.isBuffered()) continue;
                String rcRef = ld.getInst() + "/LLN0." + rc.getName();
                if (rcRef.equals(ref)) {
                    return CmsServiceError.NO_ERROR;
                }
            }
        }
        return CmsServiceError.INSTANCE_NOT_AVAILABLE;
    }

    @Override
    protected CmsApdu buildNegativeResponse(CmsApdu request, int errorCode) {
        CmsSetURCBValues response = new CmsSetURCBValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.getReqId());
        CmsSetURCBValuesResultEntry entry = new CmsSetURCBValuesResultEntry();
        entry.error.set(errorCode);
        response.result = new CmsArray<>(CmsSetURCBValuesResultEntry::new).capacity(1);
        response.result.add(entry);
        return new CmsApdu(response);
    }
}

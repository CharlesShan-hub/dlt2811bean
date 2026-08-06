package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.report.report.ReportEngine;
import com.ysh.jcms.data.sequence.block.CmsBrcb;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.report.CmsSetBrcbEntry;
import com.ysh.jcms.pdu.report.CmsSetBrcbValuesError;
import com.ysh.jcms.pdu.report.CmsSetBrcbValuesRequest;
import com.ysh.jcms.pdu.report.CmsSetBrcbValuesResponse;
import com.ysh.jcms.pdu.report.CmsSetBrcbResult;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.state.RcbStateManager;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.ArrayList;
import java.util.List;

public class SetBrcbValuesServer extends BaseServerHandler<CmsSetBrcbValuesRequest, CmsSetBrcbValuesError> {

    public SetBrcbValuesServer() {
        super(ServiceName.SET_BRCB_VALUES, CmsSetBrcbValuesRequest.class, CmsSetBrcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsSetBrcbValuesRequest req, int reqId) {
        log.info("SetBRCBValues from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.brcb.size());

        // 8.7.3.2.c) Empty sequence → Response+
        if (req.brcb.size() == 0) {
            try {
                return buildSuccess(new CmsSetBrcbValuesResponse().encode(), reqId);
            } catch (Exception e) {
                log.error("Failed to encode SetBRCBValuesResponse", e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        SclIED ied = requireIed(session, reqId);

        List<CmsSetBrcbResult> results = new ArrayList<>();
        boolean hasAnyError = false;

        for (int i = 0; i < req.brcb.size(); i++) {
            CmsSetBrcbEntry entry = req.brcb.get(i);
            String ref = entry.reference.value();

            CmsSetBrcbResult result = processEntry(ied, entry, ref, session);
            results.add(result);

            // 8.7.3.2.e) Per-entry: result is empty when all fields succeed,
            // contains only error fields on failure
            if (hasEntryError(result)) {
                hasAnyError = true;
            }
        }

        // 8.7.3.2.d) All succeed → Response+, any failure → Response- with results
        if (hasAnyError) {
            CmsSetBrcbValuesError errResp = new CmsSetBrcbValuesError();
            for (CmsSetBrcbResult r : results) {
                errResp.result.add(r);
            }
            log.warn("SetBRCBValues: {} entries had errors", results.stream().filter(this::hasEntryError).count());
            try {
                return buildError(errResp.encode(), reqId);
            } catch (Exception e) {
                log.error("Failed to encode SetBRCBValuesError", e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        try {
            return buildSuccess(new CmsSetBrcbValuesResponse().encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode SetBRCBValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    /** Check if an entry result has any error. */
    private boolean hasEntryError(CmsSetBrcbResult r) {
        if (r.isPresent("error"))
            return true;
        if (r.isPresent("rptID"))
            return true;
        if (r.isPresent("rptEna"))
            return true;
        if (r.isPresent("datSet"))
            return true;
        if (r.isPresent("optFlds"))
            return true;
        if (r.isPresent("bufTm"))
            return true;
        if (r.isPresent("trgOps"))
            return true;
        if (r.isPresent("intgPd"))
            return true;
        if (r.isPresent("gi"))
            return true;
        if (r.isPresent("purgeBuf"))
            return true;
        if (r.isPresent("entryID"))
            return true;
        if (r.isPresent("resvTms"))
            return true;
        return false;
    }

    private CmsSetBrcbResult processEntry(SclIED ied, CmsSetBrcbEntry entry, String ref, Session session) {
        CmsSetBrcbResult result = new CmsSetBrcbResult();

        // Validate ref format
        if (!SclRefParser.isValid(ref)) {
            log.warn("SetBRCBValues: invalid ref format {}", ref);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();
        String cbName = sclRef.doName();
        if (cbName == null) {
            log.warn("SetBRCBValues: invalid ref format {} (no CB name)", ref);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        // Validate LN exists
        SclLN ln = findLn(ied, ldName, lnName);
        if (ln == null) {
            log.warn("SetBRCBValues: cannot find LN {} in LD {}", lnName, ldName);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        // Validate buffered RC exists
        SclReportControl rc = null;
        for (SclReportControl c : ln.reportControls()) {
            if ("true".equals(c.buffered()) && c.name().equals(cbName)) {
                rc = c;
                break;
            }
        }
        if (rc == null) {
            log.warn("SetBRCBValues: cannot find buffered RC {} in LN {}", cbName, lnName);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        // Get or create runtime state
        CmsBrcb rtState = RcbStateManager.getOrCreate(ref);

        // 8.7.3.2.b) rptEna ordering:
        // - rptEna=false: set rptEna FIRST, then others
        // - rptEna=true: set others FIRST, then rptEna
        boolean hasRptEna = entry.isPresent("rptEna");
        boolean rptEnaVal = hasRptEna && entry.rptEna.value();

        if (hasRptEna && !rptEnaVal) {
            // rptEna=false → set rptEna first
            rtState.rptEna(false);
            result.setPresent("rptEna", false);
            setOtherBrcbFields(result, entry, rtState);
        } else if (hasRptEna && rptEnaVal) {
            // rptEna=true → set others first, then rptEna
            setOtherBrcbFields(result, entry, rtState);
            if (!hasEntryError(result)) {
                rtState.rptEna(true);
                result.setPresent("rptEna", false);
            }
            // If others failed, don't set rptEna=true (protocol requirement)
        } else {
            // No rptEna in entry
            setOtherBrcbFields(result, entry, rtState);
        }

        log.info("SetBRCBValues: applied fields to ref={}", ref);

        // 8.7.1 — ReportEngine hooks
        if (!hasEntryError(result)) {
            ReportEngine engine = ReportEngine.getInstance();
            if (engine != null) {
                if (hasRptEna) {
                    if (rptEnaVal) {
                        engine.subscribe(ref, session);
                        // Start integrity timer if intgPd was set
                        if (entry.isPresent("intgPd") && entry.intgPd.value() > 0) {
                            engine.startIntegrityTimer(ref, entry.intgPd.value());
                        }
                    } else {
                        engine.unsubscribe(ref, session);
                        engine.stopIntegrityTimer(ref);
                    }
                }
                if (entry.isPresent("gi") && entry.gi.value()) {
                    engine.triggerGi(ref);
                }
            }
        }

        return result;
    }

    private void setOtherBrcbFields(CmsSetBrcbResult result, CmsSetBrcbEntry entry, CmsBrcb rtState) {
        // rptID
        if (entry.isPresent("rptID")) {
            String val = entry.rptID.value();
            if (val != null && !val.isEmpty()) {
                rtState.rptID(val);
                result.setPresent("rptID", false);
            } else {
                result.rptID(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        // datSet
        if (entry.isPresent("datSet")) {
            String val = entry.datSet.value();
            if (val != null && !val.isEmpty()) {
                rtState.datSet(val);
                result.setPresent("datSet", false);
            } else {
                result.datSet(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        // optFlds
        if (entry.isPresent("optFlds")) {
            rtState.optFlds(entry.optFlds);
            result.setPresent("optFlds", false);
        }
        // bufTm
        if (entry.isPresent("bufTm")) {
            rtState.bufTm(entry.bufTm.value());
            result.setPresent("bufTm", false);
        }
        // trgOps
        if (entry.isPresent("trgOps")) {
            rtState.trgOps(entry.trgOps);
            result.setPresent("trgOps", false);
        }
        // intgPd
        if (entry.isPresent("intgPd")) {
            rtState.intgPd(entry.intgPd.value());
            result.setPresent("intgPd", false);
        }
        // gi
        if (entry.isPresent("gi")) {
            rtState.gi(entry.gi.value());
            result.setPresent("gi", false);
        }
        // purgeBuf
        if (entry.isPresent("purgeBuf")) {
            rtState.purgeBuf(entry.purgeBuf.value());
            result.setPresent("purgeBuf", false);
        }
        // entryID
        if (entry.isPresent("entryID")) {
            byte[] val = entry.entryID.value();
            if (val != null && val.length > 0) {
                rtState.entryID(val);
                result.setPresent("entryID", false);
            } else {
                result.entryID(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        // resvTms
        if (entry.isPresent("resvTms")) {
            rtState.resvTms(entry.resvTms.value());
            result.setPresent("resvTms", false);
        }
    }

    private static SclLN findLn(SclIED ied, String ldName, String lnName) {
        SclLDevice ld = ied.lDevice(ldName);
        return ld != null ? ld.findLnByFullName(lnName) : null;
    }
}

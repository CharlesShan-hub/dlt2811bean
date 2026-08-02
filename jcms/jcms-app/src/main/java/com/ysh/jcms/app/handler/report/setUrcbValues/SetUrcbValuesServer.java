package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.report.report.ReportEngine;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.block.CmsBrcb;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.report.CmsSetUrcbEntry;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesError;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesRequest;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesResponse;
import com.ysh.jcms.pdu.report.CmsSetUrcbResult;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.state.RcbStateManager;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SetUrcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SetUrcbValuesServer.class);

    public SetUrcbValuesServer() {
        super(ServiceName.SET_URCB_VALUES, CmsSetUrcbValuesRequest.class, CmsSetUrcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsSetUrcbValuesRequest req = (CmsSetUrcbValuesRequest) rawReq;

        log.info("SetURCBValues from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.urcb.size());

        // 8.7.5.2.c) Empty sequence → Response+
        if (req.urcb.size() == 0) {
            try {
                return buildSuccess(new CmsSetUrcbValuesResponse().encode(), reqId);
            } catch (Exception e) {
                log.error("Failed to encode SetURCBValuesResponse", e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        SclIED ied = requireIed(session, reqId);

        List<CmsSetUrcbResult> results = new ArrayList<>();
        boolean hasAnyError = false;

        for (CmsSetUrcbEntry entry : req.urcb) {
            String ref = entry.reference.value();

            CmsSetUrcbResult result = processEntry(ied, entry, ref, session);
            results.add(result);

            if (hasEntryError(result)) {
                hasAnyError = true;
            }
        }

        // 8.7.5.2.d) All succeed → Response+, any failure → Response- with results
        if (hasAnyError) {
            CmsSetUrcbValuesError errResp = new CmsSetUrcbValuesError();
            for (CmsSetUrcbResult r : results) {
                errResp.result.add(r);
            }
            log.warn("SetURCBValues: {} entries had errors", results.stream().filter(this::hasEntryError).count());
            try {
                return buildError(errResp.encode(), reqId);
            } catch (Exception e) {
                log.error("Failed to encode SetURCBValuesError", e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        try {
            return buildSuccess(new CmsSetUrcbValuesResponse().encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode SetURCBValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private boolean hasEntryError(CmsSetUrcbResult r) {
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
        if (r.isPresent("resv"))
            return true;
        return false;
    }

    private CmsSetUrcbResult processEntry(SclIED ied, CmsSetUrcbEntry entry, String ref, Session session) {
        CmsSetUrcbResult result = new CmsSetUrcbResult();

        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) {
            log.warn("SetURCBValues: invalid ref format {}", ref);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);

        SclLN ln = findLn(ied, ldName, lnName);
        if (ln == null) {
            log.warn("SetURCBValues: cannot find LN {} in LD {}", lnName, ldName);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        SclReportControl rc = null;
        for (SclReportControl c : ln.reportControls()) {
            if (!"true".equals(c.buffered()) && c.name().equals(cbName)) {
                rc = c;
                break;
            }
        }
        if (rc == null) {
            log.warn("SetURCBValues: cannot find unbuffered RC {} in LN {}", cbName, lnName);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        CmsBrcb rtState = RcbStateManager.getOrCreate(ref);

        // 8.7.5.2.b) rptEna ordering
        boolean hasRptEna = entry.isPresent("rptEna");
        boolean rptEnaVal = hasRptEna && entry.rptEna.value();

        if (hasRptEna && !rptEnaVal) {
            rtState.rptEna(false);
            result.setPresent("rptEna", false);
            setOtherUrcbFields(result, entry, rtState);
        } else if (hasRptEna && rptEnaVal) {
            setOtherUrcbFields(result, entry, rtState);
            if (!hasEntryError(result)) {
                rtState.rptEna(true);
                result.setPresent("rptEna", false);
            }
        } else {
            setOtherUrcbFields(result, entry, rtState);
        }

        log.info("SetURCBValues: applied fields to ref={}", ref);

        // 8.7.1 — ReportEngine hooks
        if (!hasEntryError(result)) {
            ReportEngine engine = ReportEngine.getInstance();
            if (engine != null) {
                if (hasRptEna) {
                    if (rptEnaVal) {
                        engine.subscribe(ref, session);
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

    private void setOtherUrcbFields(CmsSetUrcbResult result, CmsSetUrcbEntry entry, CmsBrcb rtState) {
        if (entry.isPresent("rptID")) {
            String val = entry.rptID.value();
            if (val != null && !val.isEmpty()) {
                rtState.rptID(val);
                result.setPresent("rptID", false);
            } else {
                result.rptID(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        if (entry.isPresent("datSet")) {
            String val = entry.datSet.value();
            if (val != null && !val.isEmpty()) {
                rtState.datSet(val);
                result.setPresent("datSet", false);
            } else {
                result.datSet(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        if (entry.isPresent("optFlds")) {
            rtState.optFlds(entry.optFlds);
            result.setPresent("optFlds", false);
        }
        if (entry.isPresent("bufTm")) {
            rtState.bufTm(entry.bufTm.value());
            result.setPresent("bufTm", false);
        }
        if (entry.isPresent("trgOps")) {
            rtState.trgOps(entry.trgOps);
            result.setPresent("trgOps", false);
        }
        if (entry.isPresent("intgPd")) {
            rtState.intgPd(entry.intgPd.value());
            result.setPresent("intgPd", false);
        }
        if (entry.isPresent("gi")) {
            rtState.gi(entry.gi.value());
            result.setPresent("gi", false);
        }
        if (entry.isPresent("resv")) {
            if (entry.resv.value()) {
                rtState.resvTms(0);
            } else {
                rtState.setPresent("resvTms", false);
            }
            result.setPresent("resv", false);
        }
    }

    private static SclLN findLn(SclIED ied, String ldName, String lnName) {
        SclLDevice ld = ied.lDevice(ldName);
        return ld != null ? ld.findLnByFullName(lnName) : null;
    }
}

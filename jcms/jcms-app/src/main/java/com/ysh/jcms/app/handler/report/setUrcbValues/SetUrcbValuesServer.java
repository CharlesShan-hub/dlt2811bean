package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.handler.SetCbValuesServer;
import com.ysh.jcms.app.handler.report.report.ReportEngine;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.block.CmsBrcb;
import com.ysh.jcms.data.sequence.report.CmsSetUrcbEntry;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesError;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesRequest;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesResponse;
import com.ysh.jcms.pdu.report.CmsSetUrcbResult;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.state.RcbStateManager;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;

public class SetUrcbValuesServer
        extends
            SetCbValuesServer<CmsSetUrcbValuesRequest, CmsSetUrcbValuesError, CmsSetUrcbEntry, CmsSetUrcbResult> {

    public SetUrcbValuesServer() {
        super(ServiceName.SET_URCB_VALUES, CmsSetUrcbValuesRequest.class, CmsSetUrcbValuesError.class);
    }

    @Override
    protected List<CmsSetUrcbEntry> entries(CmsSetUrcbValuesRequest req) {
        return req.urcb;
    }

    @Override
    protected String entryRef(CmsSetUrcbEntry entry) {
        return entry.reference.value();
    }

    @Override
    protected CmsType successResp() {
        return new CmsSetUrcbValuesResponse();
    }

    @Override
    protected CmsSetUrcbValuesError errorResp() {
        return new CmsSetUrcbValuesError();
    }

    @Override
    protected void addResult(CmsSetUrcbValuesError errResp, CmsSetUrcbResult result) {
        errResp.result.add(result);
    }

    @Override
    protected CmsSetUrcbResult processEntry(SclIED ied, CmsSetUrcbEntry entry, String ref, Session session) {
        CmsSetUrcbResult result = new CmsSetUrcbResult();

        SclRef sclRef = parseRef(ref);
        if (sclRef == null) {
            log.warn("SetURCBValues: invalid ref format {}", ref);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        SclLN ln = findLn(ied, sclRef.ldInst(), sclRef.lnName());
        if (ln == null) {
            log.warn("SetURCBValues: cannot find LN {} in LD {}", sclRef.lnName(), sclRef.ldInst());
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        SclReportControl rc = null;
        for (SclReportControl c : ln.reportControls()) {
            if (!"true".equals(c.buffered()) && c.name().equals(sclRef.doName())) {
                rc = c;
                break;
            }
        }
        if (rc == null) {
            log.warn("SetURCBValues: cannot find unbuffered RC {} in LN {}", sclRef.doName(), sclRef.lnName());
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
            if (!result.hasAnyPresent()) {
                rtState.rptEna(true);
                result.setPresent("rptEna", false);
            }
        } else {
            setOtherUrcbFields(result, entry, rtState);
        }

        log.info("SetURCBValues: applied fields to ref={}", ref);

        // 8.7.1 — ReportEngine hooks
        if (!result.hasAnyPresent()) {
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
}

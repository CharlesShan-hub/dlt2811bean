package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.handler.SetCbValuesServer;
import com.ysh.jcms.app.handler.report.report.ReportEngine;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.block.CmsBrcb;
import com.ysh.jcms.core.data.sequence.report.CmsSetBrcbEntry;
import com.ysh.jcms.core.pdu.report.CmsSetBrcbValuesError;
import com.ysh.jcms.core.pdu.report.CmsSetBrcbValuesRequest;
import com.ysh.jcms.core.pdu.report.CmsSetBrcbValuesResponse;
import com.ysh.jcms.core.pdu.report.CmsSetBrcbResult;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.state.RcbStateManager;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;

public class SetBrcbValuesServer
        extends
            SetCbValuesServer<CmsSetBrcbValuesRequest, CmsSetBrcbValuesError, CmsSetBrcbEntry, CmsSetBrcbResult> {

    public SetBrcbValuesServer() {
        super(CmsServiceInfo.SET_BRCB_VALUES, CmsSetBrcbValuesRequest.class, CmsSetBrcbValuesError.class);
    }

    @Override
    protected List<CmsSetBrcbEntry> entries(CmsSetBrcbValuesRequest req) {
        return req.brcb;
    }

    @Override
    protected String entryRef(CmsSetBrcbEntry entry) {
        return entry.reference.value();
    }

    @Override
    protected CmsType successResp() {
        return new CmsSetBrcbValuesResponse();
    }

    @Override
    protected CmsSetBrcbValuesError errorResp() {
        return new CmsSetBrcbValuesError();
    }

    @Override
    protected void addResult(CmsSetBrcbValuesError errResp, CmsSetBrcbResult result) {
        errResp.result.add(result);
    }

    @Override
    protected CmsSetBrcbResult processEntry(SclIED ied, CmsSetBrcbEntry entry, String ref, Session session) {
        CmsSetBrcbResult result = new CmsSetBrcbResult();

        SclRef sclRef = parseRef(ref);
        if (sclRef == null) {
            log.warn("SetBRCBValues: invalid ref format {}", ref);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        SclLN ln = findLn(ied, sclRef.ldInst(), sclRef.lnName());
        if (ln == null) {
            log.warn("SetBRCBValues: cannot find LN {} in LD {}", sclRef.lnName(), sclRef.ldInst());
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        // Validate buffered RC exists
        SclReportControl rc = null;
        for (SclReportControl c : ln.reportControls()) {
            if ("true".equals(c.buffered()) && c.name().equals(sclRef.doName())) {
                rc = c;
                break;
            }
        }
        if (rc == null) {
            log.warn("SetBRCBValues: cannot find buffered RC {} in LN {}", sclRef.doName(), sclRef.lnName());
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
            if (!result.hasAnyPresent()) {
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
        if (!result.hasAnyPresent()) {
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
}

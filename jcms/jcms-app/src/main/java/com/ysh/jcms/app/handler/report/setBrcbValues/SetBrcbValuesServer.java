package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.app.handler.report.report.ReportEngine;
import com.ysh.jcms.data.block.CmsBrcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.report.CmsSetBrcbValuesError;
import com.ysh.jcms.svc.report.CmsSetBrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsSetBrcbValuesResponse;
import com.ysh.jcms.svc.report.CmsSetBrcbEntry;
import com.ysh.jcms.svc.report.CmsSetBrcbResult;
import com.ysh.jcms.utils.scl.model.control.SclRcbStateManager;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SetBrcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SetBrcbValuesServer.class);

    public SetBrcbValuesServer() {
        super(ServiceName.SET_BRCB_VALUES, CmsSetBrcbValuesRequest.class, CmsSetBrcbValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsSetBrcbValuesRequest req = (CmsSetBrcbValuesRequest) decoded;
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsSetBrcbValuesRequest req = (CmsSetBrcbValuesRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("SetBRCBValues from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.brcb.count);

        // 8.7.3.2.c) Empty sequence → Response+
        if (req.brcb.count == 0) {
            try {
                return buildSuccess(new CmsSetBrcbValuesResponse().reqId(reqId).encode(), reqId);
            } catch (Exception e) {
                log.error("Failed to encode SetBRCBValuesResponse", e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        SclServer server = getSclServer(session);
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        List<CmsSetBrcbResult> results = new ArrayList<>();
        boolean hasAnyError = false;

        for (int i = 0; i < req.brcb.count; i++) {
            CmsSetBrcbEntry entry = req.brcb.items.get(i);
            String ref = new String(entry.reference.value(), StandardCharsets.UTF_8);

            CmsSetBrcbResult result = processEntry(server, entry, ref, session);
            results.add(result);

            // 8.7.3.2.e) Per-entry: result is empty when all fields succeed,
            // contains only error fields on failure
            if (hasEntryError(result)) {
                hasAnyError = true;
            }
        }

        // 8.7.3.2.d) All succeed → Response+, any failure → Response- with results
        if (hasAnyError) {
            CmsSetBrcbValuesError errResp = new CmsSetBrcbValuesError().reqId(reqId);
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
            return buildSuccess(new CmsSetBrcbValuesResponse().reqId(reqId).encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode SetBRCBValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    /** Check if an entry result has any error. */
    private boolean hasEntryError(CmsSetBrcbResult r) {
        if (r.errorPresent.value()) return true;
        if (r.rptIdErrPresent.value()) return true;
        if (r.rptEnaErrPresent.value()) return true;
        if (r.datSetErrPresent.value()) return true;
        if (r.optFldsErrPresent.value()) return true;
        if (r.bufTmErrPresent.value()) return true;
        if (r.trgOpsErrPresent.value()) return true;
        if (r.intgPdErrPresent.value()) return true;
        if (r.giErrPresent.value()) return true;
        if (r.purgeBufErrPresent.value()) return true;
        if (r.entryIdErrPresent.value()) return true;
        if (r.resvTmsErrPresent.value()) return true;
        return false;
    }

    private CmsSetBrcbResult processEntry(SclServer server, CmsSetBrcbEntry entry, String ref, Session session) {
        CmsSetBrcbResult result = new CmsSetBrcbResult();

        // Validate ref format
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) {
            log.warn("SetBRCBValues: invalid ref format {}", ref);
            result.errorPresent(true).error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);

        // Validate LN exists
        SclLN ln = server.findLnByRef(ldName + "/" + lnName);
        if (ln == null) {
            log.warn("SetBRCBValues: cannot find LN {} in LD {}", lnName, ldName);
            result.errorPresent(true).error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        // Validate buffered RC exists
        SclReportControl rc = null;
        for (SclReportControl c : ln.getReportControls()) {
            if (c.isBuffered() && c.getName().equals(cbName)) {
                rc = c;
                break;
            }
        }
        if (rc == null) {
            log.warn("SetBRCBValues: cannot find buffered RC {} in LN {}", cbName, lnName);
            result.errorPresent(true).error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        // Get or create runtime state
        CmsBrcb rtState = SclRcbStateManager.getOrCreate(ref);

        // 8.7.3.2.b) rptEna ordering:
        //   - rptEna=false: set rptEna FIRST, then others
        //   - rptEna=true: set others FIRST, then rptEna
        boolean hasRptEna = entry.rptEnaPresent.value();
        boolean rptEnaVal = hasRptEna && entry.rptEna.value();

        if (hasRptEna && !rptEnaVal) {
            // rptEna=false → set rptEna first
            rtState.rptEna(false);
            result.rptEnaErrPresent(false);
            setOtherBrcbFields(result, entry, rtState);
        } else if (hasRptEna && rptEnaVal) {
            // rptEna=true → set others first, then rptEna
            setOtherBrcbFields(result, entry, rtState);
            if (!hasEntryError(result)) {
                rtState.rptEna(true);
                result.rptEnaErrPresent(false);
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
                        if (entry.intgPdPresent.value() && entry.intgPd.value() > 0) {
                            engine.startIntegrityTimer(ref, entry.intgPd.value());
                        }
                    } else {
                        engine.unsubscribe(ref, session);
                        engine.stopIntegrityTimer(ref);
                    }
                }
                if (entry.giPresent.value() && entry.gi.value()) {
                    engine.triggerGi(ref);
                }
            }
        }

        return result;
    }

    private void setOtherBrcbFields(CmsSetBrcbResult result, CmsSetBrcbEntry entry, CmsBrcb rtState) {
        // rptID
        if (entry.rptIdPresent.value()) {
            byte[] val = entry.rptId.value();
            if (val != null && val.length > 0) {
                rtState.rptID(val);
                result.rptIdErrPresent(false);
            } else {
                result.rptIdErrPresent(true).rptIdErr(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        // datSet
        if (entry.datSetPresent.value()) {
            byte[] val = entry.datSet.value();
            if (val != null && val.length > 0) {
                rtState.datSet(val);
                result.datSetErrPresent(false);
            } else {
                result.datSetErrPresent(true).datSetErr(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        // optFlds
        if (entry.optFldsPresent.value()) {
            rtState.optFlds = entry.optFlds;
            result.optFldsErrPresent(false);
        }
        // bufTm
        if (entry.bufTmPresent.value()) {
            rtState.bufTm(entry.bufTm.value());
            result.bufTmErrPresent(false);
        }
        // trgOps
        if (entry.trgOpsPresent.value()) {
            rtState.trgOps = entry.trgOps;
            result.trgOpsErrPresent(false);
        }
        // intgPd
        if (entry.intgPdPresent.value()) {
            rtState.intgPd(entry.intgPd.value());
            result.intgPdErrPresent(false);
        }
        // gi
        if (entry.giPresent.value()) {
            rtState.gi(entry.gi.value());
            result.giErrPresent(false);
        }
        // purgeBuf
        if (entry.purgeBufPresent.value()) {
            rtState.purgeBuf(entry.purgeBuf.value());
            result.purgeBufErrPresent(false);
        }
        // entryID
        if (entry.entryIdPresent.value()) {
            byte[] val = entry.entryId.value();
            if (val != null && val.length > 0) {
                rtState.entryID(val);
                result.entryIdErrPresent(false);
            } else {
                result.entryIdErrPresent(true).entryIdErr(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        // resvTms
        if (entry.resvTmsPresent.value()) {
            rtState.resvTms_present(true);
            rtState.resvTms(entry.resvTms.value());
            result.resvTmsErrPresent(false);
        }
    }
}

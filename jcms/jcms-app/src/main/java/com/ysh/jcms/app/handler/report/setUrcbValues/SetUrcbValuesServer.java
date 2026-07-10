package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.report.report.ReportEngine;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsBrcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.report.CmsSetUrcbValuesError;
import com.ysh.jcms.svc.report.CmsSetUrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsSetUrcbValuesResponse;
import com.ysh.jcms.svc.report.CmsSetUrcbEntry;
import com.ysh.jcms.svc.report.CmsSetUrcbResult;
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

import java.nio.charset.StandardCharsets;
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

        log.info("SetURCBValues from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.urcb.count);

        // 8.7.5.2.c) Empty sequence → Response+
        if (req.urcb.count == 0) {
            try {
                return buildSuccess(new CmsSetUrcbValuesResponse().reqId(reqId).encode(), reqId);
            } catch (Exception e) {
                log.error("Failed to encode SetURCBValuesResponse", e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        SclIED ied = requireIed(session, reqId);

        List<CmsSetUrcbResult> results = new ArrayList<>();
        boolean hasAnyError = false;

        for (int i = 0; i < req.urcb.count; i++) {
            CmsSetUrcbEntry entry = req.urcb.items.get(i);
            String ref = new String(entry.reference.value(), StandardCharsets.UTF_8);

            CmsSetUrcbResult result = processEntry(ied, entry, ref, session);
            results.add(result);

            if (hasEntryError(result)) {
                hasAnyError = true;
            }
        }

        // 8.7.5.2.d) All succeed → Response+, any failure → Response- with results
        if (hasAnyError) {
            CmsSetUrcbValuesError errResp = new CmsSetUrcbValuesError().reqId(reqId);
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
            return buildSuccess(new CmsSetUrcbValuesResponse().reqId(reqId).encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode SetURCBValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private boolean hasEntryError(CmsSetUrcbResult r) {
        if (r.errorPresent.value())
            return true;
        if (r.rptIdErrPresent.value())
            return true;
        if (r.rptEnaErrPresent.value())
            return true;
        if (r.datSetErrPresent.value())
            return true;
        if (r.optFldsErrPresent.value())
            return true;
        if (r.bufTmErrPresent.value())
            return true;
        if (r.trgOpsErrPresent.value())
            return true;
        if (r.intgPdErrPresent.value())
            return true;
        if (r.giErrPresent.value())
            return true;
        if (r.resvErrPresent.value())
            return true;
        return false;
    }

    private CmsSetUrcbResult processEntry(SclIED ied, CmsSetUrcbEntry entry, String ref, Session session) {
        CmsSetUrcbResult result = new CmsSetUrcbResult();

        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) {
            log.warn("SetURCBValues: invalid ref format {}", ref);
            result.errorPresent(true).error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);

        SclLN ln = findLn(ied, ldName, lnName);
        if (ln == null) {
            log.warn("SetURCBValues: cannot find LN {} in LD {}", lnName, ldName);
            result.errorPresent(true).error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
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
            result.errorPresent(true).error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        CmsBrcb rtState = RcbStateManager.getOrCreate(ref);

        // 8.7.5.2.b) rptEna ordering
        boolean hasRptEna = entry.rptEnaPresent.value();
        boolean rptEnaVal = hasRptEna && entry.rptEna.value();

        if (hasRptEna && !rptEnaVal) {
            rtState.rptEna(false);
            result.rptEnaErrPresent(false);
            setOtherUrcbFields(result, entry, rtState);
        } else if (hasRptEna && rptEnaVal) {
            setOtherUrcbFields(result, entry, rtState);
            if (!hasEntryError(result)) {
                rtState.rptEna(true);
                result.rptEnaErrPresent(false);
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

    private void setOtherUrcbFields(CmsSetUrcbResult result, CmsSetUrcbEntry entry, CmsBrcb rtState) {
        if (entry.rptIdPresent.value()) {
            byte[] val = entry.rptId.value();
            if (val != null && val.length > 0) {
                rtState.rptID(val);
                result.rptIdErrPresent(false);
            } else {
                result.rptIdErrPresent(true).rptIdErr(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        if (entry.datSetPresent.value()) {
            byte[] val = entry.datSet.value();
            if (val != null && val.length > 0) {
                rtState.datSet(val);
                result.datSetErrPresent(false);
            } else {
                result.datSetErrPresent(true).datSetErr(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        if (entry.optFldsPresent.value()) {
            rtState.optFlds = entry.optFlds;
            result.optFldsErrPresent(false);
        }
        if (entry.bufTmPresent.value()) {
            rtState.bufTm(entry.bufTm.value());
            result.bufTmErrPresent(false);
        }
        if (entry.trgOpsPresent.value()) {
            rtState.trgOps = entry.trgOps;
            result.trgOpsErrPresent(false);
        }
        if (entry.intgPdPresent.value()) {
            rtState.intgPd(entry.intgPd.value());
            result.intgPdErrPresent(false);
        }
        if (entry.giPresent.value()) {
            rtState.gi(entry.gi.value());
            result.giErrPresent(false);
        }
        if (entry.resvPresent.value()) {
            rtState.resvTms_present(entry.resv.value());
            result.resvErrPresent(false);
        }
    }

    private static SclLN findLn(SclIED ied, String ldName, String lnName) {
        SclLDevice ld = ied.lDevice(ldName);
        return ld != null ? ld.findLnByFullName(lnName) : null;
    }
}

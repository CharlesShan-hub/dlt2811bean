package com.ysh.jcms.app.handler.log.setLcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.log.CmsSetLcbValuesError;
import com.ysh.jcms.svc.log.CmsSetLcbValuesRequest;
import com.ysh.jcms.svc.log.CmsSetLcbValuesResponse;
import com.ysh.jcms.svc.log.CmsSetLcbEntry;
import com.ysh.jcms.svc.log.CmsSetLcbResult;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SetLcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SetLcbValuesServer.class);

    public SetLcbValuesServer() {
        super(ServiceName.SET_LCB_VALUES, CmsSetLcbValuesRequest.class, CmsSetLcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsSetLcbValuesRequest req = (CmsSetLcbValuesRequest) rawReq;

        log.info("SetLCBValues from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.lcb.count);

        // 8.8.3.2.c) Empty sequence → Response+
        if (req.lcb.count == 0) {
            try {
                return buildSuccess(new CmsSetLcbValuesResponse().reqId(reqId).encode(), reqId);
            } catch (Exception e) {
                log.error("Failed to encode SetLCBValuesResponse", e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        SclIED ied = requireIed(session, reqId);

        List<CmsSetLcbResult> results = new ArrayList<>();
        boolean hasAnyError = false;

        for (int i = 0; i < req.lcb.count; i++) {
            CmsSetLcbEntry entry = req.lcb.items.get(i);
            String ref = new String(entry.reference.value(), StandardCharsets.UTF_8);

            CmsSetLcbResult result = processEntry(ied, entry, ref);
            results.add(result);

            if (hasEntryError(result)) {
                hasAnyError = true;
            }
        }

        if (hasAnyError) {
            CmsSetLcbValuesError errResp = new CmsSetLcbValuesError().reqId(reqId);
            for (CmsSetLcbResult r : results) {
                errResp.result.add(r);
            }
            log.warn("SetLCBValues: {} entries had errors", results.stream().filter(this::hasEntryError).count());
            try {
                return buildError(errResp.encode(), reqId);
            } catch (Exception e) {
                log.error("Failed to encode SetLCBValuesError", e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        try {
            return buildSuccess(new CmsSetLcbValuesResponse().reqId(reqId).encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode SetLCBValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private boolean hasEntryError(CmsSetLcbResult r) {
        if (r.errorPresent.value())
            return true;
        if (r.logEnaErrPresent.value())
            return true;
        if (r.datSetErrPresent.value())
            return true;
        if (r.trgOpsErrPresent.value())
            return true;
        if (r.intgPdErrPresent.value())
            return true;
        if (r.logRefErrPresent.value())
            return true;
        if (r.optFldsErrPresent.value())
            return true;
        if (r.bufTmErrPresent.value())
            return true;
        return false;
    }

    private CmsSetLcbResult processEntry(SclIED ied, CmsSetLcbEntry entry, String ref) {
        CmsSetLcbResult result = new CmsSetLcbResult();

        // Validate ref format
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) {
            log.warn("SetLCBValues: invalid ref format {}", ref);
            result.errorPresent(true).error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);

        // Validate LN exists
        SclLN ln = findLn(ied, ldName, lnName);
        if (ln == null) {
            log.warn("SetLCBValues: cannot find LN {} in LD {}", lnName, ldName);
            result.errorPresent(true).error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        // Validate LC exists
        SclLogControl lc = null;
        for (SclLogControl c : ln.logControls()) {
            if (c.name().equals(cbName)) {
                lc = c;
                break;
            }
        }
        if (lc == null) {
            log.warn("SetLCBValues: cannot find LC {} in LN {}", cbName, lnName);
            result.errorPresent(true).error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        // 8.8.3.2.b) logEna ordering:
        // - logEna=false: set logEna FIRST, then others
        // - logEna=true: set others FIRST, then logEna
        boolean hasLogEna = entry.logEnaPresent.value();
        boolean logEnaVal = hasLogEna && entry.logEna.value();

        if (hasLogEna && !logEnaVal) {
            // logEna=false → set logEna first
            lc.logEna("false");
            result.logEnaErrPresent(false);
            setOtherLcbFields(result, entry, lc);
        } else if (hasLogEna && logEnaVal) {
            // logEna=true → set others first, then logEna
            setOtherLcbFields(result, entry, lc);
            if (!hasEntryError(result)) {
                lc.logEna("true");
                result.logEnaErrPresent(false);
            }
        } else {
            // No logEna in entry
            setOtherLcbFields(result, entry, lc);
        }

        log.info("SetLCBValues: applied fields to ref={}", ref);
        return result;
    }

    private void setOtherLcbFields(CmsSetLcbResult result, CmsSetLcbEntry entry, SclLogControl lc) {
        // datSet
        if (entry.datSetPresent.value()) {
            byte[] val = entry.datSet.value();
            if (val != null && val.length > 0) {
                lc.datSet(new String(val, StandardCharsets.UTF_8));
                result.datSetErrPresent(false);
            } else {
                result.datSetErrPresent(true).datSetErr(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        // trgOps
        if (entry.trgOpsPresent.value()) {
            lc.trgOps(entry.trgOps.data_change.value() + "," + entry.trgOps.quality_change.value() + "," + entry.trgOps.data_update.value()
                    + "," + entry.trgOps.integrity.value());
            result.trgOpsErrPresent(false);
        }
        // intgPd
        if (entry.intgPdPresent.value()) {
            lc.intgPd(String.valueOf(entry.intgPd.value()));
            result.intgPdErrPresent(false);
        }
        // logRef
        if (entry.logRefPresent.value()) {
            byte[] val = entry.logRef.value();
            if (val != null && val.length > 0) {
                lc.logName(new String(val, StandardCharsets.UTF_8));
                result.logRefErrPresent(false);
            } else {
                result.logRefErrPresent(true).logRefErr(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        // optFlds
        if (entry.optFldsPresent.value()) {
            result.optFldsErrPresent(false);
        }
        // bufTm
        if (entry.bufTmPresent.value()) {
            result.bufTmErrPresent(false);
        }
    }

    private static SclLN findLn(SclIED ied, String ldName, String lnName) {
        SclLDevice ld = ied.lDevice(ldName);
        return ld != null ? ld.findLnByFullName(lnName) : null;
    }
}

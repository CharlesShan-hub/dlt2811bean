package com.ysh.jcms.app.handler.log.setLcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.log.CmsSetLcbEntry;
import com.ysh.jcms.data.sequence.log.CmsSetLcbResult;
import com.ysh.jcms.pdu.log.CmsSetLcbValuesError;
import com.ysh.jcms.pdu.log.CmsSetLcbValuesRequest;
import com.ysh.jcms.pdu.log.CmsSetLcbValuesResponse;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.ArrayList;
import java.util.List;

public class SetLcbValuesServer extends BaseServerHandler {

    public SetLcbValuesServer() {
        super(ServiceName.SET_LCB_VALUES, CmsSetLcbValuesRequest.class, CmsSetLcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsSetLcbValuesRequest req = (CmsSetLcbValuesRequest) rawReq;

        log.info("SetLCBValues from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.lcb.size());

        // 8.8.3.2.c) Empty sequence → Response+
        if (req.lcb.size() == 0) {
            try {
                return buildSuccess(new CmsSetLcbValuesResponse().encode(), reqId);
            } catch (Exception e) {
                log.error("Failed to encode SetLCBValuesResponse", e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        SclIED ied = requireIed(session, reqId);

        List<CmsSetLcbResult> results = new ArrayList<>();
        boolean hasAnyError = false;

        for (int i = 0; i < req.lcb.size(); i++) {
            CmsSetLcbEntry entry = req.lcb.get(i);
            String ref = entry.reference.value();

            CmsSetLcbResult result = processEntry(ied, entry, ref);
            results.add(result);

            if (hasEntryError(result)) {
                hasAnyError = true;
            }
        }

        if (hasAnyError) {
            CmsSetLcbValuesError errResp = new CmsSetLcbValuesError();
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
            return buildSuccess(new CmsSetLcbValuesResponse().encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode SetLCBValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private boolean hasEntryError(CmsSetLcbResult r) {
        if (r.isPresent("error"))
            return true;
        if (r.isPresent("logEna"))
            return true;
        if (r.isPresent("datSet"))
            return true;
        if (r.isPresent("trgOps"))
            return true;
        if (r.isPresent("intgPd"))
            return true;
        if (r.isPresent("logRef"))
            return true;
        if (r.isPresent("optFlds"))
            return true;
        if (r.isPresent("bufTm"))
            return true;
        return false;
    }

    private CmsSetLcbResult processEntry(SclIED ied, CmsSetLcbEntry entry, String ref) {
        CmsSetLcbResult result = new CmsSetLcbResult();

        // Validate ref format
        if (!SclRefParser.isValid(ref)) {
            log.warn("SetLCBValues: invalid ref format {}", ref);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();
        String cbName = sclRef.doName();
        if (cbName == null) {
            log.warn("SetLCBValues: invalid ref format {} (no CB name)", ref);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        // Validate LN exists
        SclLN ln = findLn(ied, ldName, lnName);
        if (ln == null) {
            log.warn("SetLCBValues: cannot find LN {} in LD {}", lnName, ldName);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
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
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        // 8.8.3.2.b) logEna ordering:
        // - logEna=false: set logEna FIRST, then others
        // - logEna=true: set others FIRST, then logEna
        boolean hasLogEna = entry.isPresent("logEna");
        boolean logEnaVal = hasLogEna && entry.logEna.value();

        if (hasLogEna && !logEnaVal) {
            // logEna=false → set logEna first
            lc.logEna("false");
            result.setPresent("logEna", false);
            setOtherLcbFields(result, entry, lc);
        } else if (hasLogEna && logEnaVal) {
            // logEna=true → set others first, then logEna
            setOtherLcbFields(result, entry, lc);
            if (!hasEntryError(result)) {
                lc.logEna("true");
                result.setPresent("logEna", false);
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
        if (entry.isPresent("datSet")) {
            String val = entry.datSet.value();
            if (val != null && val.length() > 0) {
                lc.datSet(val);
                result.setPresent("datSet", false);
            } else {
                result.datSet(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        // trgOps
        if (entry.isPresent("trgOps")) {
            lc.trgOps(entry.trgOps.data_change() + "," + entry.trgOps.quality_change() + "," + entry.trgOps.data_update() + ","
                    + entry.trgOps.integrity());
            result.setPresent("trgOps", false);
        }
        // intgPd
        if (entry.isPresent("intgPd")) {
            lc.intgPd(String.valueOf(entry.intgPd.value()));
            result.setPresent("intgPd", false);
        }
        // logRef
        if (entry.isPresent("logRef")) {
            String val = entry.logRef.value();
            if (val != null && val.length() > 0) {
                lc.logName(val);
                result.setPresent("logRef", false);
            } else {
                result.logRef(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }
        // optFlds
        if (entry.isPresent("optFlds")) {
            result.setPresent("optFlds", false);
        }
        // bufTm
        if (entry.isPresent("bufTm")) {
            result.setPresent("bufTm", false);
        }
    }

    private static SclLN findLn(SclIED ied, String ldName, String lnName) {
        SclLDevice ld = ied.lDevice(ldName);
        return ld != null ? ld.findLnByFullName(lnName) : null;
    }
}

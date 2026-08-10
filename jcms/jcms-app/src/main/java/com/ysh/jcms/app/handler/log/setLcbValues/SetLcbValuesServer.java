package com.ysh.jcms.app.handler.log.setLcbValues;

import com.ysh.jcms.app.handler.SetCbValuesServer;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.log.CmsSetLcbEntry;
import com.ysh.jcms.data.sequence.log.CmsSetLcbResult;
import com.ysh.jcms.pdu.log.CmsSetLcbValuesError;
import com.ysh.jcms.pdu.log.CmsSetLcbValuesRequest;
import com.ysh.jcms.pdu.log.CmsSetLcbValuesResponse;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;

public class SetLcbValuesServer extends SetCbValuesServer<CmsSetLcbValuesRequest, CmsSetLcbValuesError, CmsSetLcbEntry, CmsSetLcbResult> {

    public SetLcbValuesServer() {
        super(ServiceName.SET_LCB_VALUES, CmsSetLcbValuesRequest.class, CmsSetLcbValuesError.class);
    }

    @Override
    protected List<CmsSetLcbEntry> entries(CmsSetLcbValuesRequest req) {
        return req.lcb;
    }

    @Override
    protected String entryRef(CmsSetLcbEntry entry) {
        return entry.reference.value();
    }

    @Override
    protected CmsType successResp() {
        return new CmsSetLcbValuesResponse();
    }

    @Override
    protected CmsSetLcbValuesError errorResp() {
        return new CmsSetLcbValuesError();
    }

    @Override
    protected void addResult(CmsSetLcbValuesError errResp, CmsSetLcbResult result) {
        errResp.result.add(result);
    }

    @Override
    protected CmsSetLcbResult processEntry(SclIED ied, CmsSetLcbEntry entry, String ref, Session session) {
        CmsSetLcbResult result = new CmsSetLcbResult();

        SclRef sclRef = parseRef(ref);
        if (sclRef == null) {
            log.warn("SetLCBValues: invalid ref format {}", ref);
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        SclLN ln = findLn(ied, sclRef.ldInst(), sclRef.lnName());
        if (ln == null) {
            log.warn("SetLCBValues: cannot find LN {} in LD {}", sclRef.lnName(), sclRef.ldInst());
            result.error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        SclLogControl lc = null;
        for (SclLogControl c : ln.logControls()) {
            if (c.name().equals(sclRef.doName())) {
                lc = c;
                break;
            }
        }
        if (lc == null) {
            log.warn("SetLCBValues: cannot find LC {} in LN {}", sclRef.doName(), sclRef.lnName());
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
            if (!result.hasAnyPresent()) {
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
}

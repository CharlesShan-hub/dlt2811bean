package com.ysh.jcms.app.handler.log.setLcbValues;

import com.ysh.jcms.app.handler.support.SetCbValuesServer;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.block.CmsLcb;
import com.ysh.jcms.core.data.sequence.log.CmsSetLcbEntry;
import com.ysh.jcms.core.data.sequence.log.CmsSetLcbResult;
import com.ysh.jcms.core.pdu.log.CmsSetLcbValuesError;
import com.ysh.jcms.core.pdu.log.CmsSetLcbValuesRequest;
import com.ysh.jcms.core.pdu.log.CmsSetLcbValuesResponse;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.service.SclControlBlockService;
import com.ysh.jcms.utils.scl.state.CbStateManager;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;

public class SetLcbValuesServer extends SetCbValuesServer<CmsSetLcbValuesRequest, CmsSetLcbValuesError, CmsSetLcbEntry, CmsSetLcbResult> {

    public SetLcbValuesServer() {
        super(CmsServiceInfo.SET_LCB_VALUES, CmsSetLcbValuesRequest.class, CmsSetLcbValuesError.class);
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

        // Runtime state layer: SCL baseline first, then runtime overlay.
        // Never mutate the SCL model (static configuration stays clean).
        CmsLcb rtState = CbStateManager.LCB.get(ref);
        if (rtState == null) {
            rtState = SclControlBlockService.resolveLcb(ied, ref);
            if (rtState == null) {
                rtState = new CmsLcb();
            }
        }

        // 8.8.3.2.b) logEna ordering:
        // - logEna=false: set logEna FIRST, then others
        // - logEna=true: set others FIRST, then logEna
        boolean hasLogEna = entry.isPresent("logEna");
        boolean logEnaVal = hasLogEna && entry.logEna.value();

        if (hasLogEna && !logEnaVal) {
            // logEna=false → set logEna first
            rtState.logEna(false);
            result.setPresent("logEna", false);
            setOtherLcbFields(result, entry, rtState);
        } else if (hasLogEna && logEnaVal) {
            // logEna=true → set others first, then logEna
            setOtherLcbFields(result, entry, rtState);
            if (!result.hasAnyPresent()) {
                rtState.logEna(true);
                result.setPresent("logEna", false);
            }
        } else {
            // No logEna in entry
            setOtherLcbFields(result, entry, rtState);
        }

        CbStateManager.LCB.put(ref, rtState);
        log.info("SetLCBValues: applied fields to ref={}", ref);
        return result;
    }

    private void setOtherLcbFields(CmsSetLcbResult result, CmsSetLcbEntry entry, CmsLcb rtState) {
        // datSet
        if (entry.isPresent("datSet")) {
            String val = entry.datSet.value();
            if (val != null && val.length() > 0) {
                rtState.datSet(val);
                result.setPresent("datSet", false);
            } else {
                result.datSet(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
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
        // logRef
        if (entry.isPresent("logRef")) {
            String val = entry.logRef.value();
            if (val != null && val.length() > 0) {
                rtState.logRef(val);
                result.setPresent("logRef", false);
            } else {
                result.logRef(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
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
    }
}

package com.ysh.jcms.data.sequence.log;

import com.ysh.jcms.data.InnerAnonymousSetLCBValuesErrorPDUResult;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * (inline SEQUENCE within SetLCBValues-ErrorPDU result) ::= SEQUENCE {
 *     error   [0] IMPLICIT ServiceError OPTIONAL,
 *     logEna  [1] IMPLICIT ServiceError OPTIONAL,
 *     datSet  [2] IMPLICIT ServiceError OPTIONAL,
 *     trgOps  [3] IMPLICIT ServiceError OPTIONAL,
 *     intgPd  [4] IMPLICIT ServiceError OPTIONAL,
 *     logRef  [5] IMPLICIT ServiceError OPTIONAL,
 *     optFlds [6] IMPLICIT ServiceError OPTIONAL,
 *     bufTm   [7] IMPLICIT ServiceError OPTIONAL
 * } — 8.8.3
 *
 * <p>Element of SetLCBValues-ErrorPDU result.
 */
public class CmsSetLcbResult extends CmsSequence {

    @CmsField(optional = true) public CmsServiceError error;
    @CmsField(optional = true) public CmsServiceError logEna;
    @CmsField(optional = true) public CmsServiceError datSet;
    @CmsField(optional = true) public CmsServiceError trgOps;
    @CmsField(optional = true) public CmsServiceError intgPd;
    @CmsField(optional = true) public CmsServiceError logRef;
    @CmsField(optional = true) public CmsServiceError optFlds;
    @CmsField(optional = true) public CmsServiceError bufTm;

    public CmsSetLcbResult() { super(new InnerAnonymousSetLCBValuesErrorPDUResult()); }

    public CmsSetLcbResult error(int v) { this.error.value(v); setPresent("error", true); return this; }
    public CmsSetLcbResult logEna(int v) { this.logEna.value(v); setPresent("logEna", true); return this; }
    public CmsSetLcbResult datSet(int v) { this.datSet.value(v); setPresent("datSet", true); return this; }
    public CmsSetLcbResult trgOps(int v) { this.trgOps.value(v); setPresent("trgOps", true); return this; }
    public CmsSetLcbResult intgPd(int v) { this.intgPd.value(v); setPresent("intgPd", true); return this; }
    public CmsSetLcbResult logRef(int v) { this.logRef.value(v); setPresent("logRef", true); return this; }
    public CmsSetLcbResult optFlds(int v) { this.optFlds.value(v); setPresent("optFlds", true); return this; }
    public CmsSetLcbResult bufTm(int v) { this.bufTm.value(v); setPresent("bufTm", true); return this; }

    /** Copy all field values from another CmsSetLcbResult (fluent). */
    public CmsSetLcbResult value(CmsSetLcbResult v) {
        if (v.isPresent("error")) { this.error.value(v.error.value()); setPresent("error", true); }
        else { setPresent("error", false); }
        if (v.isPresent("logEna")) { this.logEna.value(v.logEna.value()); setPresent("logEna", true); }
        else { setPresent("logEna", false); }
        if (v.isPresent("datSet")) { this.datSet.value(v.datSet.value()); setPresent("datSet", true); }
        else { setPresent("datSet", false); }
        if (v.isPresent("trgOps")) { this.trgOps.value(v.trgOps.value()); setPresent("trgOps", true); }
        else { setPresent("trgOps", false); }
        if (v.isPresent("intgPd")) { this.intgPd.value(v.intgPd.value()); setPresent("intgPd", true); }
        else { setPresent("intgPd", false); }
        if (v.isPresent("logRef")) { this.logRef.value(v.logRef.value()); setPresent("logRef", true); }
        else { setPresent("logRef", false); }
        if (v.isPresent("optFlds")) { this.optFlds.value(v.optFlds.value()); setPresent("optFlds", true); }
        else { setPresent("optFlds", false); }
        if (v.isPresent("bufTm")) { this.bufTm.value(v.bufTm.value()); setPresent("bufTm", true); }
        else { setPresent("bufTm", false); }
        return this;
    }
}

package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * SetLCBResult ::= SEQUENCE {
 *     error       [0] IMPLICIT ServiceError OPTIONAL,
 *     logEna      [1] IMPLICIT ServiceError OPTIONAL,
 *     datSet      [2] IMPLICIT ServiceError OPTIONAL,
 *     trgOps      [3] IMPLICIT ServiceError OPTIONAL,
 *     intgPd      [4] IMPLICIT ServiceError OPTIONAL,
 *     logRef      [5] IMPLICIT ServiceError OPTIONAL,
 *     optFlds     [6] IMPLICIT ServiceError OPTIONAL,
 *     bufTm       [7] IMPLICIT ServiceError OPTIONAL
 * }  —  8.8.3
 *
 * Used by SetLCBValues error (SEQUENCE OF SetLCBResult).
 */
public class CmsSetLcbResult extends CmsType {

    public CmsBoolean       errorPresent;
    public CmsServiceError  error;
    public CmsBoolean       logEnaErrPresent;
    public CmsServiceError  logEnaErr;
    public CmsBoolean       datSetErrPresent;
    public CmsServiceError  datSetErr;
    public CmsBoolean       trgOpsErrPresent;
    public CmsServiceError  trgOpsErr;
    public CmsBoolean       intgPdErrPresent;
    public CmsServiceError  intgPdErr;
    public CmsBoolean       logRefErrPresent;
    public CmsServiceError  logRefErr;
    public CmsBoolean       optFldsErrPresent;
    public CmsServiceError  optFldsErr;
    public CmsBoolean       bufTmErrPresent;
    public CmsServiceError  bufTmErr;

    public CmsSetLcbResult() {
        this.errorPresent    = new CmsBoolean();
        this.error           = new CmsServiceError();
        this.logEnaErrPresent = new CmsBoolean();
        this.logEnaErr       = new CmsServiceError();
        this.datSetErrPresent = new CmsBoolean();
        this.datSetErr       = new CmsServiceError();
        this.trgOpsErrPresent = new CmsBoolean();
        this.trgOpsErr       = new CmsServiceError();
        this.intgPdErrPresent = new CmsBoolean();
        this.intgPdErr       = new CmsServiceError();
        this.logRefErrPresent = new CmsBoolean();
        this.logRefErr       = new CmsServiceError();
        this.optFldsErrPresent = new CmsBoolean();
        this.optFldsErr      = new CmsServiceError();
        this.bufTmErrPresent = new CmsBoolean();
        this.bufTmErr        = new CmsServiceError();
    }
    
    // -- chain setters --
    public CmsSetLcbResult errorPresent(boolean v) { this.errorPresent.value(v); return this; }
    public CmsSetLcbResult error(int v) { this.error.value(v); return this; }
    public CmsSetLcbResult logEnaErrPresent(boolean v) { this.logEnaErrPresent.value(v); return this; }
    public CmsSetLcbResult logEnaErr(int v) { this.logEnaErr.value(v); return this; }
    public CmsSetLcbResult datSetErrPresent(boolean v) { this.datSetErrPresent.value(v); return this; }
    public CmsSetLcbResult datSetErr(int v) { this.datSetErr.value(v); return this; }
    public CmsSetLcbResult trgOpsErrPresent(boolean v) { this.trgOpsErrPresent.value(v); return this; }
    public CmsSetLcbResult trgOpsErr(int v) { this.trgOpsErr.value(v); return this; }
    public CmsSetLcbResult intgPdErrPresent(boolean v) { this.intgPdErrPresent.value(v); return this; }
    public CmsSetLcbResult intgPdErr(int v) { this.intgPdErr.value(v); return this; }
    public CmsSetLcbResult logRefErrPresent(boolean v) { this.logRefErrPresent.value(v); return this; }
    public CmsSetLcbResult logRefErr(int v) { this.logRefErr.value(v); return this; }
    public CmsSetLcbResult optFldsErrPresent(boolean v) { this.optFldsErrPresent.value(v); return this; }
    public CmsSetLcbResult optFldsErr(int v) { this.optFldsErr.value(v); return this; }
    public CmsSetLcbResult bufTmErrPresent(boolean v) { this.bufTmErrPresent.value(v); return this; }
    public CmsSetLcbResult bufTmErr(int v) { this.bufTmErr.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(
            errorPresent, error,
            logEnaErrPresent, logEnaErr,
            datSetErrPresent, datSetErr,
            trgOpsErrPresent, trgOpsErr,
            intgPdErrPresent, intgPdErr,
            logRefErrPresent, logRefErr,
            optFldsErrPresent, optFldsErr,
            bufTmErrPresent, bufTmErr);
    }
}
package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * SetURCBResult ::= SEQUENCE {
 *     error       [0] IMPLICIT ServiceError OPTIONAL,
 *     rptID       [1] IMPLICIT ServiceError OPTIONAL,
 *     rptEna      [2] IMPLICIT ServiceError OPTIONAL,
 *     datSet      [3] IMPLICIT ServiceError OPTIONAL,
 *     optFlds     [5] IMPLICIT ServiceError OPTIONAL,
 *     bufTm       [6] IMPLICIT ServiceError OPTIONAL,
 *     trgOps      [8] IMPLICIT ServiceError OPTIONAL,
 *     intgPd      [9] IMPLICIT ServiceError OPTIONAL,
 *     gi          [10] IMPLICIT ServiceError OPTIONAL,
 *     resv        [13] IMPLICIT ServiceError OPTIONAL
 * }  —  8.7.5
 */
public class CmsSetUrcbResult extends CmsType {

    public CmsBoolean        errorPresent;
    public CmsServiceError   error;            /* OPTIONAL */
    public CmsBoolean        rptIdErrPresent;
    public CmsServiceError   rptIdErr;         /* OPTIONAL */
    public CmsBoolean        rptEnaErrPresent;
    public CmsServiceError   rptEnaErr;        /* OPTIONAL */
    public CmsBoolean        datSetErrPresent;
    public CmsServiceError   datSetErr;        /* OPTIONAL */
    public CmsBoolean        optFldsErrPresent;
    public CmsServiceError   optFldsErr;       /* OPTIONAL */
    public CmsBoolean        bufTmErrPresent;
    public CmsServiceError   bufTmErr;         /* OPTIONAL */
    public CmsBoolean        trgOpsErrPresent;
    public CmsServiceError   trgOpsErr;        /* OPTIONAL */
    public CmsBoolean        intgPdErrPresent;
    public CmsServiceError   intgPdErr;        /* OPTIONAL */
    public CmsBoolean        giErrPresent;
    public CmsServiceError   giErr;            /* OPTIONAL */
    public CmsBoolean        resvErrPresent;
    public CmsServiceError   resvErr;          /* OPTIONAL */

    public CmsSetUrcbResult() {
        this.errorPresent      = new CmsBoolean();
        this.error             = new CmsServiceError();
        this.rptIdErrPresent   = new CmsBoolean();
        this.rptIdErr          = new CmsServiceError();
        this.rptEnaErrPresent  = new CmsBoolean();
        this.rptEnaErr         = new CmsServiceError();
        this.datSetErrPresent  = new CmsBoolean();
        this.datSetErr         = new CmsServiceError();
        this.optFldsErrPresent = new CmsBoolean();
        this.optFldsErr        = new CmsServiceError();
        this.bufTmErrPresent   = new CmsBoolean();
        this.bufTmErr          = new CmsServiceError();
        this.trgOpsErrPresent  = new CmsBoolean();
        this.trgOpsErr         = new CmsServiceError();
        this.intgPdErrPresent  = new CmsBoolean();
        this.intgPdErr         = new CmsServiceError();
        this.giErrPresent      = new CmsBoolean();
        this.giErr             = new CmsServiceError();
        this.resvErrPresent    = new CmsBoolean();
        this.resvErr           = new CmsServiceError();
    }
    
    // -- chain setters --
    public CmsSetUrcbResult errorPresent(boolean v) { this.errorPresent.value(v); return this; }
    public CmsSetUrcbResult error(int v) { this.error.value(v); return this; }
    public CmsSetUrcbResult rptIdErrPresent(boolean v) { this.rptIdErrPresent.value(v); return this; }
    public CmsSetUrcbResult rptIdErr(int v) { this.rptIdErr.value(v); return this; }
    public CmsSetUrcbResult rptEnaErrPresent(boolean v) { this.rptEnaErrPresent.value(v); return this; }
    public CmsSetUrcbResult rptEnaErr(int v) { this.rptEnaErr.value(v); return this; }
    public CmsSetUrcbResult datSetErrPresent(boolean v) { this.datSetErrPresent.value(v); return this; }
    public CmsSetUrcbResult datSetErr(int v) { this.datSetErr.value(v); return this; }
    public CmsSetUrcbResult optFldsErrPresent(boolean v) { this.optFldsErrPresent.value(v); return this; }
    public CmsSetUrcbResult optFldsErr(int v) { this.optFldsErr.value(v); return this; }
    public CmsSetUrcbResult bufTmErrPresent(boolean v) { this.bufTmErrPresent.value(v); return this; }
    public CmsSetUrcbResult bufTmErr(int v) { this.bufTmErr.value(v); return this; }
    public CmsSetUrcbResult trgOpsErrPresent(boolean v) { this.trgOpsErrPresent.value(v); return this; }
    public CmsSetUrcbResult trgOpsErr(int v) { this.trgOpsErr.value(v); return this; }
    public CmsSetUrcbResult intgPdErrPresent(boolean v) { this.intgPdErrPresent.value(v); return this; }
    public CmsSetUrcbResult intgPdErr(int v) { this.intgPdErr.value(v); return this; }
    public CmsSetUrcbResult giErrPresent(boolean v) { this.giErrPresent.value(v); return this; }
    public CmsSetUrcbResult giErr(int v) { this.giErr.value(v); return this; }
    public CmsSetUrcbResult resvErrPresent(boolean v) { this.resvErrPresent.value(v); return this; }
    public CmsSetUrcbResult resvErr(int v) { this.resvErr.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(errorPresent, error,
            rptIdErrPresent, rptIdErr,
            rptEnaErrPresent, rptEnaErr,
            datSetErrPresent, datSetErr,
            optFldsErrPresent, optFldsErr,
            bufTmErrPresent, bufTmErr,
            trgOpsErrPresent, trgOpsErr,
            intgPdErrPresent, intgPdErr,
            giErrPresent, giErr,
            resvErrPresent, resvErr);
    }
}
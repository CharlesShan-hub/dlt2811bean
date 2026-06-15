package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * SetBRCBResult ::= SEQUENCE {
 *     error       [0] IMPLICIT ServiceError OPTIONAL,
 *     rptID       [1] IMPLICIT ServiceError OPTIONAL,
 *     rptEna      [2] IMPLICIT ServiceError OPTIONAL,
 *     datSet      [3] IMPLICIT ServiceError OPTIONAL,
 *     optFlds     [5] IMPLICIT ServiceError OPTIONAL,
 *     bufTm       [6] IMPLICIT ServiceError OPTIONAL,
 *     trgOps      [8] IMPLICIT ServiceError OPTIONAL,
 *     intgPd      [9] IMPLICIT ServiceError OPTIONAL,
 *     gi          [10] IMPLICIT ServiceError OPTIONAL,
 *     purgeBuf    [11] IMPLICIT ServiceError OPTIONAL,
 *     entryID     [12] IMPLICIT ServiceError OPTIONAL,
 *     resvTms     [14] IMPLICIT ServiceError OPTIONAL
 * }  —  8.7.3
 */
public class CmsSetBrcbResult extends CmsType {

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
    public CmsBoolean        purgeBufErrPresent;
    public CmsServiceError   purgeBufErr;      /* OPTIONAL */
    public CmsBoolean        entryIdErrPresent;
    public CmsServiceError   entryIdErr;       /* OPTIONAL */
    public CmsBoolean        resvTmsErrPresent;
    public CmsServiceError   resvTmsErr;       /* OPTIONAL */

    public CmsSetBrcbResult() {
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
        this.purgeBufErrPresent = new CmsBoolean();
        this.purgeBufErr       = new CmsServiceError();
        this.entryIdErrPresent = new CmsBoolean();
        this.entryIdErr        = new CmsServiceError();
        this.resvTmsErrPresent = new CmsBoolean();
        this.resvTmsErr        = new CmsServiceError();
    }

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
            purgeBufErrPresent, purgeBufErr,
            entryIdErrPresent, entryIdErr,
            resvTmsErrPresent, resvTmsErr);
    }
}

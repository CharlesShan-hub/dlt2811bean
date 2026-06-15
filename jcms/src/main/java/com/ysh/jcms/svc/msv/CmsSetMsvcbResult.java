package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * SetMSVCBResult ::= SEQUENCE {
 *     error       [0] IMPLICIT ServiceError OPTIONAL,
 *     svEna       [1] IMPLICIT ServiceError OPTIONAL,
 *     msvID       [2] IMPLICIT ServiceError OPTIONAL,
 *     datSet      [3] IMPLICIT ServiceError OPTIONAL,
 *     smpMod      [5] IMPLICIT ServiceError OPTIONAL,
 *     smpRate     [6] IMPLICIT ServiceError OPTIONAL,
 *     optFlds     [7] IMPLICIT ServiceError OPTIONAL
 * }  —  8.10.3
 *
 * Used by SetMSVCBValues error.
 */
public class CmsSetMsvcbResult extends CmsType {

    public CmsBoolean       errorPresent;
    public CmsServiceError  error;
    public CmsBoolean       svEnaErrPresent;
    public CmsServiceError  svEnaErr;
    public CmsBoolean       msvIdErrPresent;
    public CmsServiceError  msvIdErr;
    public CmsBoolean       datSetErrPresent;
    public CmsServiceError  datSetErr;
    public CmsBoolean       smpModErrPresent;
    public CmsServiceError  smpModErr;
    public CmsBoolean       smpRateErrPresent;
    public CmsServiceError  smpRateErr;
    public CmsBoolean       optFldsErrPresent;
    public CmsServiceError  optFldsErr;

    public CmsSetMsvcbResult() {
        this.errorPresent      = new CmsBoolean();
        this.error             = new CmsServiceError();
        this.svEnaErrPresent   = new CmsBoolean();
        this.svEnaErr          = new CmsServiceError();
        this.msvIdErrPresent   = new CmsBoolean();
        this.msvIdErr          = new CmsServiceError();
        this.datSetErrPresent  = new CmsBoolean();
        this.datSetErr         = new CmsServiceError();
        this.smpModErrPresent  = new CmsBoolean();
        this.smpModErr         = new CmsServiceError();
        this.smpRateErrPresent = new CmsBoolean();
        this.smpRateErr        = new CmsServiceError();
        this.optFldsErrPresent = new CmsBoolean();
        this.optFldsErr        = new CmsServiceError();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(
            errorPresent, error,
            svEnaErrPresent, svEnaErr,
            msvIdErrPresent, msvIdErr,
            datSetErrPresent, datSetErr,
            smpModErrPresent, smpModErr,
            smpRateErrPresent, smpRateErr,
            optFldsErrPresent, optFldsErr);
    }
}

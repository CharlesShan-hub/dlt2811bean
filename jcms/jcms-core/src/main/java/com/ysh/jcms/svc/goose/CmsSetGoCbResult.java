package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * SetGoCBResult ::= SEQUENCE { error [0] IMPLICIT ServiceError OPTIONAL, goEna
 * [1] IMPLICIT ServiceError OPTIONAL, goID [2] IMPLICIT ServiceError OPTIONAL,
 * datSet [3] IMPLICIT ServiceError OPTIONAL } — 8.9.5
 *
 * Used by SetGoCBValues error.
 */
public class CmsSetGoCbResult extends CmsType {

    public CmsBoolean errorPresent;
    public CmsServiceError error;
    public CmsBoolean goEnaErrPresent;
    public CmsServiceError goEnaErr;
    public CmsBoolean goIdErrPresent;
    public CmsServiceError goIdErr;
    public CmsBoolean datSetErrPresent;
    public CmsServiceError datSetErr;

    public CmsSetGoCbResult() {
        this.errorPresent = new CmsBoolean();
        this.error = new CmsServiceError();
        this.goEnaErrPresent = new CmsBoolean();
        this.goEnaErr = new CmsServiceError();
        this.goIdErrPresent = new CmsBoolean();
        this.goIdErr = new CmsServiceError();
        this.datSetErrPresent = new CmsBoolean();
        this.datSetErr = new CmsServiceError();
    }

    public CmsSetGoCbResult errorPresent(boolean v) {
        this.errorPresent.value(v);
        return this;
    }
    public CmsSetGoCbResult error(int v) {
        this.error.value(v);
        return this;
    }
    public CmsSetGoCbResult goEnaErrPresent(boolean v) {
        this.goEnaErrPresent.value(v);
        return this;
    }
    public CmsSetGoCbResult goEnaErr(int v) {
        this.goEnaErr.value(v);
        return this;
    }
    public CmsSetGoCbResult goIdErrPresent(boolean v) {
        this.goIdErrPresent.value(v);
        return this;
    }
    public CmsSetGoCbResult goIdErr(int v) {
        this.goIdErr.value(v);
        return this;
    }
    public CmsSetGoCbResult datSetErrPresent(boolean v) {
        this.datSetErrPresent.value(v);
        return this;
    }
    public CmsSetGoCbResult datSetErr(int v) {
        this.datSetErr.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(errorPresent, error, goEnaErrPresent, goEnaErr, goIdErrPresent, goIdErr, datSetErrPresent, datSetErr);
    }
}

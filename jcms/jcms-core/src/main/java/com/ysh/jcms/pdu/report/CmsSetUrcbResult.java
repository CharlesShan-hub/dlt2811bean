package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerAnonymousSetURCBValuesErrorPDUResult;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * SetURCBResult ::= SEQUENCE { error [0] IMPLICIT ServiceError OPTIONAL, rptID
 * [1] IMPLICIT ServiceError OPTIONAL, rptEna [2] IMPLICIT ServiceError
 * OPTIONAL, datSet [3] IMPLICIT ServiceError OPTIONAL, optFlds [5] IMPLICIT
 * ServiceError OPTIONAL, bufTm [6] IMPLICIT ServiceError OPTIONAL, trgOps [8]
 * IMPLICIT ServiceError OPTIONAL, intgPd [9] IMPLICIT ServiceError OPTIONAL, gi
 * [10] IMPLICIT ServiceError OPTIONAL, resv [13] IMPLICIT ServiceError OPTIONAL
 * } — 8.7.5 (inline within SetURCBValues-ErrorPDU)
 */
public class CmsSetUrcbResult extends CmsSequence {

    @CmsField(optional = true)
    public CmsServiceError error;
    @CmsField(optional = true)
    public CmsServiceError rptID;
    @CmsField(optional = true)
    public CmsServiceError rptEna;
    @CmsField(optional = true)
    public CmsServiceError datSet;
    @CmsField(optional = true)
    public CmsServiceError optFlds;
    @CmsField(optional = true)
    public CmsServiceError bufTm;
    @CmsField(optional = true)
    public CmsServiceError trgOps;
    @CmsField(optional = true)
    public CmsServiceError intgPd;
    @CmsField(optional = true)
    public CmsServiceError gi;
    @CmsField(optional = true)
    public CmsServiceError resv;

    public CmsSetUrcbResult() {
        super(new InnerAnonymousSetURCBValuesErrorPDUResult());
    }

    public CmsSetUrcbResult error(int v) {
        this.error.value(v);
        setPresent("error", true);
        return this;
    }
    public CmsSetUrcbResult rptID(int v) {
        this.rptID.value(v);
        setPresent("rptID", true);
        return this;
    }
    public CmsSetUrcbResult rptEna(int v) {
        this.rptEna.value(v);
        setPresent("rptEna", true);
        return this;
    }
    public CmsSetUrcbResult datSet(int v) {
        this.datSet.value(v);
        setPresent("datSet", true);
        return this;
    }
    public CmsSetUrcbResult optFlds(int v) {
        this.optFlds.value(v);
        setPresent("optFlds", true);
        return this;
    }
    public CmsSetUrcbResult bufTm(int v) {
        this.bufTm.value(v);
        setPresent("bufTm", true);
        return this;
    }
    public CmsSetUrcbResult trgOps(int v) {
        this.trgOps.value(v);
        setPresent("trgOps", true);
        return this;
    }
    public CmsSetUrcbResult intgPd(int v) {
        this.intgPd.value(v);
        setPresent("intgPd", true);
        return this;
    }
    public CmsSetUrcbResult gi(int v) {
        this.gi.value(v);
        setPresent("gi", true);
        return this;
    }
    public CmsSetUrcbResult resv(int v) {
        this.resv.value(v);
        setPresent("resv", true);
        return this;
    }

    public CmsSetUrcbResult value(CmsSetUrcbResult v) {
        if (v.isPresent("error")) {
            this.error.value(v.error.value());
            setPresent("error", true);
        } else {
            setPresent("error", false);
        }
        if (v.isPresent("rptID")) {
            this.rptID.value(v.rptID.value());
            setPresent("rptID", true);
        } else {
            setPresent("rptID", false);
        }
        if (v.isPresent("rptEna")) {
            this.rptEna.value(v.rptEna.value());
            setPresent("rptEna", true);
        } else {
            setPresent("rptEna", false);
        }
        if (v.isPresent("datSet")) {
            this.datSet.value(v.datSet.value());
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
        }
        if (v.isPresent("optFlds")) {
            this.optFlds.value(v.optFlds.value());
            setPresent("optFlds", true);
        } else {
            setPresent("optFlds", false);
        }
        if (v.isPresent("bufTm")) {
            this.bufTm.value(v.bufTm.value());
            setPresent("bufTm", true);
        } else {
            setPresent("bufTm", false);
        }
        if (v.isPresent("trgOps")) {
            this.trgOps.value(v.trgOps.value());
            setPresent("trgOps", true);
        } else {
            setPresent("trgOps", false);
        }
        if (v.isPresent("intgPd")) {
            this.intgPd.value(v.intgPd.value());
            setPresent("intgPd", true);
        } else {
            setPresent("intgPd", false);
        }
        if (v.isPresent("gi")) {
            this.gi.value(v.gi.value());
            setPresent("gi", true);
        } else {
            setPresent("gi", false);
        }
        if (v.isPresent("resv")) {
            this.resv.value(v.resv.value());
            setPresent("resv", true);
        } else {
            setPresent("resv", false);
        }
        return this;
    }
}

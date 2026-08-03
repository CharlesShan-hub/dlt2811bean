package com.ysh.jcms.data.sequence.msv;

import com.ysh.jcms.data.InnerAnonymousSetMSVCBValuesErrorPDUResult;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * SetMSVCBValues-ErrorPDU result entry ::= SEQUENCE {
 *     error           [0] IMPLICIT ServiceError OPTIONAL,
 *     svEna           [1] IMPLICIT ServiceError OPTIONAL,
 *     msvID           [2] IMPLICIT ServiceError OPTIONAL,
 *     datSet          [3] IMPLICIT ServiceError OPTIONAL,
 *     smpMod          [5] IMPLICIT ServiceError OPTIONAL,
 *     smpRate         [6] IMPLICIT ServiceError OPTIONAL,
 *     optFlds         [7] IMPLICIT ServiceError OPTIONAL
 * } — 8.10.3
 * }
 * </pre>
 */
public class CmsSetMsvcbResult extends CmsSequence {

    @CmsField(optional = true)
    public CmsServiceError error;
    @CmsField(optional = true)
    public CmsServiceError svEna;
    @CmsField(optional = true)
    public CmsServiceError msvID;
    @CmsField(optional = true)
    public CmsServiceError datSet;
    @CmsField(optional = true)
    public CmsServiceError smpMod;
    @CmsField(optional = true)
    public CmsServiceError smpRate;
    @CmsField(optional = true)
    public CmsServiceError optFlds;

    public CmsSetMsvcbResult() {
        super(new InnerAnonymousSetMSVCBValuesErrorPDUResult());
    }

    public CmsSetMsvcbResult error(int v) {
        this.error.value(v);
        setPresent("error", true);
        return this;
    }
    public CmsSetMsvcbResult svEna(int v) {
        this.svEna.value(v);
        setPresent("svEna", true);
        return this;
    }
    public CmsSetMsvcbResult msvID(int v) {
        this.msvID.value(v);
        setPresent("msvID", true);
        return this;
    }
    public CmsSetMsvcbResult datSet(int v) {
        this.datSet.value(v);
        setPresent("datSet", true);
        return this;
    }
    public CmsSetMsvcbResult smpMod(int v) {
        this.smpMod.value(v);
        setPresent("smpMod", true);
        return this;
    }
    public CmsSetMsvcbResult smpRate(int v) {
        this.smpRate.value(v);
        setPresent("smpRate", true);
        return this;
    }
    public CmsSetMsvcbResult optFlds(int v) {
        this.optFlds.value(v);
        setPresent("optFlds", true);
        return this;
    }

    /** Copy all field values from another result entry (fluent). */
    public CmsSetMsvcbResult value(CmsSetMsvcbResult v) {
        if (v.isPresent("error")) {
            this.error.value(v.error.value());
            setPresent("error", true);
        } else {
            setPresent("error", false);
        }
        if (v.isPresent("svEna")) {
            this.svEna.value(v.svEna.value());
            setPresent("svEna", true);
        } else {
            setPresent("svEna", false);
        }
        if (v.isPresent("msvID")) {
            this.msvID.value(v.msvID.value());
            setPresent("msvID", true);
        } else {
            setPresent("msvID", false);
        }
        if (v.isPresent("datSet")) {
            this.datSet.value(v.datSet.value());
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
        }
        if (v.isPresent("smpMod")) {
            this.smpMod.value(v.smpMod.value());
            setPresent("smpMod", true);
        } else {
            setPresent("smpMod", false);
        }
        if (v.isPresent("smpRate")) {
            this.smpRate.value(v.smpRate.value());
            setPresent("smpRate", true);
        } else {
            setPresent("smpRate", false);
        }
        if (v.isPresent("optFlds")) {
            this.optFlds.value(v.optFlds.value());
            setPresent("optFlds", true);
        } else {
            setPresent("optFlds", false);
        }
        return this;
    }
}

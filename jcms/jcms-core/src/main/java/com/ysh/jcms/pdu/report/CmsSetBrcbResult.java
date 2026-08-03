package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerAnonymousSetBRCBValuesErrorPDUResult;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
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
 * } — 8.7.3 (inline within SetBRCBValues-ErrorPDU)
 * }
 * </pre>
 */
public class CmsSetBrcbResult extends CmsSequence {

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
    public CmsServiceError purgeBuf;
    @CmsField(optional = true)
    public CmsServiceError entryID;
    @CmsField(optional = true)
    public CmsServiceError resvTms;

    public CmsSetBrcbResult() {
        super(new InnerAnonymousSetBRCBValuesErrorPDUResult());
    }

    public CmsSetBrcbResult error(int v) {
        this.error.value(v);
        setPresent("error", true);
        return this;
    }
    public CmsSetBrcbResult rptID(int v) {
        this.rptID.value(v);
        setPresent("rptID", true);
        return this;
    }
    public CmsSetBrcbResult rptEna(int v) {
        this.rptEna.value(v);
        setPresent("rptEna", true);
        return this;
    }
    public CmsSetBrcbResult datSet(int v) {
        this.datSet.value(v);
        setPresent("datSet", true);
        return this;
    }
    public CmsSetBrcbResult optFlds(int v) {
        this.optFlds.value(v);
        setPresent("optFlds", true);
        return this;
    }
    public CmsSetBrcbResult bufTm(int v) {
        this.bufTm.value(v);
        setPresent("bufTm", true);
        return this;
    }
    public CmsSetBrcbResult trgOps(int v) {
        this.trgOps.value(v);
        setPresent("trgOps", true);
        return this;
    }
    public CmsSetBrcbResult intgPd(int v) {
        this.intgPd.value(v);
        setPresent("intgPd", true);
        return this;
    }
    public CmsSetBrcbResult gi(int v) {
        this.gi.value(v);
        setPresent("gi", true);
        return this;
    }
    public CmsSetBrcbResult purgeBuf(int v) {
        this.purgeBuf.value(v);
        setPresent("purgeBuf", true);
        return this;
    }
    public CmsSetBrcbResult entryID(int v) {
        this.entryID.value(v);
        setPresent("entryID", true);
        return this;
    }
    public CmsSetBrcbResult resvTms(int v) {
        this.resvTms.value(v);
        setPresent("resvTms", true);
        return this;
    }

    public CmsSetBrcbResult value(CmsSetBrcbResult v) {
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
        if (v.isPresent("purgeBuf")) {
            this.purgeBuf.value(v.purgeBuf.value());
            setPresent("purgeBuf", true);
        } else {
            setPresent("purgeBuf", false);
        }
        if (v.isPresent("entryID")) {
            this.entryID.value(v.entryID.value());
            setPresent("entryID", true);
        } else {
            setPresent("entryID", false);
        }
        if (v.isPresent("resvTms")) {
            this.resvTms.value(v.resvTms.value());
            setPresent("resvTms", true);
        } else {
            setPresent("resvTms", false);
        }
        return this;
    }
}

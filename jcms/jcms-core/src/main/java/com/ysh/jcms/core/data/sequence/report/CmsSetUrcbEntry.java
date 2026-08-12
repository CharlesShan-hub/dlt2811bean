package com.ysh.jcms.core.data.sequence.report;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerAnonymousSetURCBValuesRequestPDUUrcb;
import com.ysh.jcms.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * SetURCBEntry ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     rptID           [1] IMPLICIT VisibleString129 OPTIONAL,
 *     rptEna          [2] IMPLICIT BOOLEAN OPTIONAL,
 *     datSet          [3] IMPLICIT ObjectReference OPTIONAL,
 *     optFlds         [5] IMPLICIT RCBOptFlds OPTIONAL,
 *     bufTm           [6] IMPLICIT INT32U OPTIONAL,
 *     trgOps          [8] IMPLICIT TriggerConditions OPTIONAL,
 *     intgPd          [9] IMPLICIT INT32U OPTIONAL,
 *     gi              [10] IMPLICIT BOOLEAN OPTIONAL,
 *     resv            [13] IMPLICIT BOOLEAN OPTIONAL
 * } — 8.7.5 (inline within SetURCBValues-RequestPDU)
 * }
 * </pre>
 */
public class CmsSetUrcbEntry extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;
    @CmsField(optional = true)
    public CmsString rptID;
    @CmsField(optional = true)
    public CmsBoolean rptEna;
    @CmsField(optional = true)
    public CmsObjectReference datSet;
    @CmsField(optional = true)
    public CmsRcbOptFlds optFlds;
    @CmsField(optional = true)
    public CmsInt32U bufTm;
    @CmsField(optional = true)
    public CmsTriggerConditions trgOps;
    @CmsField(optional = true)
    public CmsInt32U intgPd;
    @CmsField(optional = true)
    public CmsBoolean gi;
    @CmsField(optional = true)
    public CmsBoolean resv;

    public CmsSetUrcbEntry() {
        super(new InnerAnonymousSetURCBValuesRequestPDUUrcb());
    }

    public CmsSetUrcbEntry reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsSetUrcbEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsSetUrcbEntry rptID(String v) {
        if (v != null) {
            this.rptID.value(v);
            setPresent("rptID", true);
        } else {
            setPresent("rptID", false);
        }
        return this;
    }
    public CmsSetUrcbEntry rptID(byte[] v) {
        return rptID(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsSetUrcbEntry rptEna(boolean v) {
        this.rptEna.value(v);
        setPresent("rptEna", true);
        return this;
    }
    public CmsSetUrcbEntry datSet(String v) {
        if (v != null) {
            this.datSet.value(v);
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
        }
        return this;
    }
    public CmsSetUrcbEntry datSet(byte[] v) {
        return datSet(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsSetUrcbEntry optFlds(CmsRcbOptFlds v) {
        if (v != null) {
            this.optFlds.value(v);
            setPresent("optFlds", true);
        } else {
            setPresent("optFlds", false);
        }
        return this;
    }
    public CmsSetUrcbEntry bufTm(long v) {
        this.bufTm.value(v);
        setPresent("bufTm", true);
        return this;
    }
    public CmsSetUrcbEntry trgOps(CmsTriggerConditions v) {
        if (v != null) {
            this.trgOps.value(v);
            setPresent("trgOps", true);
        } else {
            setPresent("trgOps", false);
        }
        return this;
    }
    public CmsSetUrcbEntry intgPd(long v) {
        this.intgPd.value(v);
        setPresent("intgPd", true);
        return this;
    }
    public CmsSetUrcbEntry gi(boolean v) {
        this.gi.value(v);
        setPresent("gi", true);
        return this;
    }
    public CmsSetUrcbEntry resv(boolean v) {
        this.resv.value(v);
        setPresent("resv", true);
        return this;
    }

    public CmsSetUrcbEntry value(CmsSetUrcbEntry v) {
        reference(v.reference.value());
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
            this.optFlds.value(v.optFlds);
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
            this.trgOps.value(v.trgOps);
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

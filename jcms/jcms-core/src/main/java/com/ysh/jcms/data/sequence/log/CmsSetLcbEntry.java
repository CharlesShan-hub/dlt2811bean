package com.ysh.jcms.data.sequence.log;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerAnonymousSetLCBValuesRequestPDULcb;
import com.ysh.jcms.data.bitarray.CmsLcbOptFlds;
import com.ysh.jcms.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * (inline SEQUENCE within SetLCBValues-RequestPDU lcb) ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     logEna          [1] IMPLICIT BOOLEAN OPTIONAL,
 *     datSet          [2] IMPLICIT ObjectReference OPTIONAL,
 *     trgOps          [3] IMPLICIT TriggerConditions OPTIONAL,
 *     intgPd          [4] IMPLICIT INT32U OPTIONAL,
 *     logRef          [5] IMPLICIT ObjectReference OPTIONAL,
 *     optFlds         [6] IMPLICIT LCBOptFlds OPTIONAL,
 *     bufTm           [7] IMPLICIT INT32U OPTIONAL
 * } — 8.8.3
 * }
 * </pre>
 */
public class CmsSetLcbEntry extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;
    @CmsField(optional = true)
    public CmsBoolean logEna;
    @CmsField(optional = true)
    public CmsObjectReference datSet;
    @CmsField(optional = true)
    public CmsTriggerConditions trgOps;
    @CmsField(optional = true)
    public CmsInt32U intgPd;
    @CmsField(optional = true)
    public CmsObjectReference logRef;
    @CmsField(optional = true)
    public CmsLcbOptFlds optFlds;
    @CmsField(optional = true)
    public CmsInt32U bufTm;

    public CmsSetLcbEntry() {
        super(new InnerAnonymousSetLCBValuesRequestPDULcb());
    }

    public CmsSetLcbEntry reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsSetLcbEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsSetLcbEntry logEna(boolean v) {
        this.logEna.value(v);
        setPresent("logEna", true);
        return this;
    }
    public CmsSetLcbEntry datSet(String v) {
        if (v != null) {
            this.datSet.value(v);
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
        }
        return this;
    }
    public CmsSetLcbEntry datSet(byte[] v) {
        return datSet(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsSetLcbEntry trgOps(CmsTriggerConditions v) {
        if (v != null) {
            this.trgOps.value(v);
            setPresent("trgOps", true);
        } else {
            setPresent("trgOps", false);
        }
        return this;
    }
    public CmsSetLcbEntry intgPd(long v) {
        this.intgPd.value(v);
        setPresent("intgPd", true);
        return this;
    }
    public CmsSetLcbEntry logRef(String v) {
        if (v != null) {
            this.logRef.value(v);
            setPresent("logRef", true);
        } else {
            setPresent("logRef", false);
        }
        return this;
    }
    public CmsSetLcbEntry logRef(byte[] v) {
        return logRef(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsSetLcbEntry optFlds(CmsLcbOptFlds v) {
        if (v != null) {
            this.optFlds.value(v);
            setPresent("optFlds", true);
        } else {
            setPresent("optFlds", false);
        }
        return this;
    }
    public CmsSetLcbEntry bufTm(long v) {
        this.bufTm.value(v);
        setPresent("bufTm", true);
        return this;
    }

    /** Copy all field values from another CmsSetLcbEntry (fluent). */
    public CmsSetLcbEntry value(CmsSetLcbEntry v) {
        reference(v.reference.value());
        if (v.isPresent("logEna")) {
            this.logEna.value(v.logEna.value());
            setPresent("logEna", true);
        } else {
            setPresent("logEna", false);
        }
        if (v.isPresent("datSet")) {
            this.datSet.value(v.datSet.value());
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
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
        if (v.isPresent("logRef")) {
            this.logRef.value(v.logRef.value());
            setPresent("logRef", true);
        } else {
            setPresent("logRef", false);
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
        return this;
    }
}

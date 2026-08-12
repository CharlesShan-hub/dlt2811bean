package com.ysh.jcms.core.data.sequence.block;

import com.ysh.jcms.core.data.bitarray.CmsLcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.core.data.scalar.*;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * LCB ::= SEQUENCE {
 *     logEna          [1] IMPLICIT BOOLEAN,
 *     datSet          [2] IMPLICIT ObjectReference,
 *     trgOps          [3] IMPLICIT TriggerConditions,
 *     intgPd          [4] IMPLICIT INT32U,
 *     logRef          [5] IMPLICIT ObjectReference,
 *     optFlds         [6] IMPLICIT LCBOptFlds OPTIONAL,
 *     bufTm           [7] IMPLICIT INT32U OPTIONAL
 * } — 8.8.2
 * }
 * </pre>
 */
public class CmsLcb extends CmsSequence {
    @CmsField
    @CbField(scope = CbFieldScope.RUNTIME)
    public CmsBoolean logEna;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsObjectReference datSet;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsTriggerConditions trgOps;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsInt32U intgPd;
    @CmsField
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsObjectReference logRef;
    @CmsField(optional = true)
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsLcbOptFlds optFlds;
    @CmsField(optional = true)
    @CbField(scope = CbFieldScope.ENGINEERING)
    public CmsInt32U bufTm;

    public CmsLcb() {
        super(new InnerLCB());
    }

    public CmsLcb logEna(boolean v) {
        this.logEna.value(v);
        return this;
    }
    public CmsLcb datSet(String v) {
        this.datSet.value(v);
        return this;
    }
    public CmsLcb trgOps(CmsTriggerConditions v) {
        this.trgOps.value(v);
        return this;
    }
    public CmsLcb intgPd(long v) {
        this.intgPd.value(v);
        return this;
    }
    public CmsLcb logRef(String v) {
        this.logRef.value(v);
        return this;
    }
    public CmsLcb optFlds(CmsLcbOptFlds v) {
        if (v != null) {
            this.optFlds.value(v);
            setPresent("optFlds", true);
        } else {
            setPresent("optFlds", false);
        }
        return this;
    }
    public CmsLcb bufTm(long v) {
        this.bufTm.value(v);
        setPresent("bufTm", true);
        return this;
    }

    /** Copy all field values from another CmsLcb (fluent). */
    public CmsLcb value(CmsLcb v) {
        logEna(v.logEna.value());
        datSet(v.datSet.value());
        trgOps(v.trgOps);
        intgPd(v.intgPd.value());
        logRef(v.logRef.value());
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

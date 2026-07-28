package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;

public class CmsLcb extends CmsSequence {
    @CmsField public CmsBoolean logEna;
    @CmsField public CmsObjectReference datSet;
    @CmsField public CmsTriggerConditions trgOps;
    @CmsField public CmsInt32U intgPd;
    @CmsField public CmsObjectReference logRef;
    @CmsField(optional = true) public CmsLcbOptFlds optFlds;
    @CmsField(optional = true) public CmsInt32U bufTm;

    public CmsLcb() {
        super(new InnerLCB());
    }

    public CmsLcb logEna(boolean v) { this.logEna.value(v); return this; }
    public CmsLcb datSet(String v) { this.datSet.value(v); return this; }
    public CmsLcb trgOps(CmsTriggerConditions v) { this.trgOps.packed(v); return this; }
    public CmsLcb intgPd(long v) { this.intgPd.value(v); return this; }
    public CmsLcb logRef(String v) { this.logRef.value(v); return this; }
    public CmsLcb optFlds(CmsLcbOptFlds v) { this.optFlds.packed(v); setPresent("optFlds", true); return this; }
    public CmsLcb bufTm(long v) { this.bufTm.value(v); setPresent("bufTm", true); return this; }
}

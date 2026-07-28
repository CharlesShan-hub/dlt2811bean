package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;

public class CmsLcb extends CmsType {
    public CmsBoolean logEna;
    public CmsObjectReference datSet;
    public CmsTriggerConditions trgOps;
    public CmsInt32U intgPd;
    public CmsObjectReference logRef;
    public CmsLcbOptFlds optFlds;
    public boolean hasOptFlds;
    public CmsInt32U bufTm;
    public boolean hasBufTm;

    public CmsLcb() {
        super(new InnerLCB());
        this.logEna = new CmsBoolean();
        this.datSet = new CmsObjectReference();
        this.trgOps = new CmsTriggerConditions();
        this.intgPd = new CmsInt32U();
        this.logRef = new CmsObjectReference();
        this.optFlds = new CmsLcbOptFlds();
        this.bufTm = new CmsInt32U();
    }

    public CmsLcb logEna(boolean v) { this.logEna.value(v); return this; }
    public CmsLcb datSet(String v) { this.datSet.value(v); return this; }
    public CmsLcb trgOps(CmsTriggerConditions v) { this.trgOps = v; return this; }
    public CmsLcb intgPd(long v) { this.intgPd.value(v); return this; }
    public CmsLcb logRef(String v) { this.logRef.value(v); return this; }
    public CmsLcb optFlds(CmsLcbOptFlds v) { this.optFlds = v; this.hasOptFlds = true; return this; }
    public CmsLcb bufTm(long v) { this.bufTm.value(v); this.hasBufTm = true; return this; }

    @Override
    public void syncToInner() {
        InnerLCB i = (InnerLCB) inner;
        i.logEna.value = logEna.value() ? 1 : 0;
        i.datSet.value.value = datSet.value();
        trgOps.syncToInner();
        i.trgOps = (InnerTriggerConditions) trgOps.inner;
        i.intgPd.value = (int) intgPd.value();
        i.logRef.value.value = logRef.value();
        if (hasOptFlds) {
            optFlds.syncToInner();
            i.optFlds = (InnerLcbOptFlds) optFlds.inner;
            i._set.add("optFlds");
        }
        if (hasBufTm) {
            i.bufTm.value = (int) bufTm.value();
            i._set.add("bufTm");
        }
    }

    @Override
    public void syncFromInner() {
        InnerLCB i = (InnerLCB) inner;
        logEna.value(i.logEna.value != 0);
        datSet.value(i.datSet.value.value);
        trgOps.inner = i.trgOps;
        trgOps.syncFromInner();
        intgPd.value(i.intgPd.value & 0xFFFFFFFFL);
        logRef.value(i.logRef.value.value);
        hasOptFlds = i._set.contains("optFlds");
        if (hasOptFlds) {
            optFlds.inner = i.optFlds;
            optFlds.syncFromInner();
        }
        hasBufTm = i._set.contains("bufTm");
        if (hasBufTm) {
            bufTm.value(i.bufTm.value & 0xFFFFFFFFL);
        }
    }
}

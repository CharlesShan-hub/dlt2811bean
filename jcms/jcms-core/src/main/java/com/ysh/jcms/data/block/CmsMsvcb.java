package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;

/**
 * MSVCB ::= SEQUENCE { 10 fields } — 8.10.2
 * <p>
 * OPTIONAL fields (smpMod, dstAddress) use has* boolean flags.
 */
public class CmsMsvcb extends CmsType {

    public CmsBoolean svEna;
    public String msvID;
    public CmsObjectReference datSet;
    public CmsInt32U confRev;
    public CmsSmpMod smpMod; /* OPTIONAL */
    public boolean hasSmpMod;
    public CmsInt16U smpRate;
    public CmsMsvcbOptFlds optFlds;
    public CmsPhyComAddr dstAddress; /* OPTIONAL */
    public boolean hasDstAddress;

    public CmsMsvcb() {
        super(new InnerMSVCB());
        this.svEna = new CmsBoolean();
        this.msvID = "";
        this.datSet = new CmsObjectReference();
        this.confRev = new CmsInt32U();
        this.smpMod = new CmsSmpMod();
        this.smpRate = new CmsInt16U();
        this.optFlds = new CmsMsvcbOptFlds();
        this.dstAddress = new CmsPhyComAddr();
    }

    public CmsMsvcb svEna(boolean v) { this.svEna.value(v); return this; }
    public CmsMsvcb msvID(String v) { this.msvID = v; return this; }
    public CmsMsvcb datSet(String v) { this.datSet.value(v); return this; }
    public CmsMsvcb confRev(long v) { this.confRev.value(v); return this; }
    public CmsMsvcb smpMod(int v) { this.smpMod.value(v); this.hasSmpMod = true; return this; }
    public CmsMsvcb smpRate(int v) { this.smpRate.value(v); return this; }
    public CmsMsvcb dstAddress(CmsPhyComAddr v) { this.dstAddress = v; this.hasDstAddress = true; return this; }

    @Override
    public void syncToInner() {
        InnerMSVCB i = (InnerMSVCB) inner;
        i.svEna.value = svEna.value() ? 1 : 0;
        i.msvID = msvID;
        i.datSet.value = datSet.value();
        i.confRev.value = (int) confRev.value();
        if (hasSmpMod) {
            i.smpMod.value = smpMod.value();
            i._set.add("smpMod");
        }
        i.smpRate.value = smpRate.value();
        optFlds.syncToInner();
        i.optFlds.value = ((InnerMsvcbOptFlds) optFlds.inner).value;
        if (hasDstAddress) {
            dstAddress.syncToInner();
            i.dstAddress = (InnerPhyComAddr) dstAddress.inner;
            i._set.add("dstAddress");
        }
    }

    @Override
    public void syncFromInner() {
        InnerMSVCB i = (InnerMSVCB) inner;
        svEna.value(i.svEna.value != 0);
        msvID = i.msvID;
        datSet.value(i.datSet.value);
        confRev.value(i.confRev.value & 0xFFFFFFFFL);
        hasSmpMod = i._set.contains("smpMod");
        if (hasSmpMod) smpMod.value(i.smpMod.value);
        smpRate.value(i.smpRate.value & 0xFFFF);
        ((InnerMsvcbOptFlds) optFlds.inner).value = i.optFlds.value;
        optFlds.syncFromInner();
        hasDstAddress = i._set.contains("dstAddress");
        if (hasDstAddress) {
            dstAddress.inner = i.dstAddress;
            dstAddress.syncFromInner();
        }
    }
}

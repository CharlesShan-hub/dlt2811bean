package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsString;

public class CmsMsvcb extends CmsSequence {
    @CmsField public CmsBoolean svEna;
    @CmsField public CmsString msvID;
    @CmsField public CmsObjectReference datSet;
    @CmsField public CmsInt32U confRev;
    @CmsField(optional = true) public CmsSmpMod smpMod;
    @CmsField public CmsInt16U smpRate;
    @CmsField public CmsMsvcbOptFlds optFlds;
    @CmsField(optional = true) public CmsPhyComAddr dstAddress;

    public CmsMsvcb() { super(new InnerMSVCB()); }

    public CmsMsvcb svEna(boolean v) { this.svEna.value(v); return this; }
    public CmsMsvcb msvID(String v) { this.msvID.value(v); return this; }
    public CmsMsvcb datSet(String v) { this.datSet.value(v); return this; }
    public CmsMsvcb confRev(long v) { this.confRev.value(v); return this; }
    public CmsMsvcb smpMod(int v) { this.smpMod.value(v); setPresent("smpMod", true); return this; }
    public CmsMsvcb smpRate(int v) { this.smpRate.value(v); return this; }
    public CmsMsvcb optFlds(CmsMsvcbOptFlds v) { this.optFlds.packed(v); return this; }
    public CmsMsvcb dstAddress(CmsPhyComAddr v) { this.dstAddress.value(v); setPresent("dstAddress", true); return this; }
}

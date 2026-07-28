package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsOctetString;
import com.ysh.jcms.data.string.CmsString;

public class CmsUrcb extends CmsSequence {
    @CmsField public CmsString rptID;
    @CmsField public CmsBoolean rptEna;
    @CmsField public CmsObjectReference datSet;
    @CmsField public CmsInt32U confRev;
    @CmsField public CmsRcbOptFlds optFlds;
    @CmsField public CmsInt32U bufTm;
    @CmsField public CmsInt16U sqNum;
    @CmsField public CmsTriggerConditions trgOps;
    @CmsField public CmsInt32U intgPd;
    @CmsField public CmsBoolean gi;
    @CmsField public CmsBoolean resv;
    @CmsField(optional = true) public CmsOctetString owner;

    public CmsUrcb() { super(new InnerURCB()); }

    public CmsUrcb rptID(String v) { this.rptID.value(v); return this; }
    public CmsUrcb rptEna(boolean v) { this.rptEna.value(v); return this; }
    public CmsUrcb datSet(String v) { this.datSet.value(v); return this; }
    public CmsUrcb confRev(long v) { this.confRev.value(v); return this; }
    public CmsUrcb optFlds(CmsRcbOptFlds v) { this.optFlds.packed(v); return this; }
    public CmsUrcb bufTm(long v) { this.bufTm.value(v); return this; }
    public CmsUrcb sqNum(int v) { this.sqNum.value(v); return this; }
    public CmsUrcb trgOps(CmsTriggerConditions v) { this.trgOps.packed(v); return this; }
    public CmsUrcb intgPd(long v) { this.intgPd.value(v); return this; }
    public CmsUrcb gi(boolean v) { this.gi.value(v); return this; }
    public CmsUrcb resv(boolean v) { this.resv.value(v); return this; }
    public CmsUrcb owner(byte[] v) { this.owner.value(v); setPresent("owner", true); return this; }
}

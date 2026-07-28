package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.core.InnerField;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsOctetString;
import com.ysh.jcms.data.string.CmsString;
import com.ysh.jcms.data.time.CmsBinaryTime;

public class CmsBrcb extends CmsSequence {
    @InnerField public CmsString rptID;
    @InnerField public CmsBoolean rptEna;
    @InnerField public CmsObjectReference datSet;
    @InnerField public CmsInt32U confRev;
    @InnerField public CmsRcbOptFlds optFlds;
    @InnerField public CmsInt32U bufTm;
    @InnerField public CmsInt16U sqNum;
    @InnerField public CmsTriggerConditions trgOps;
    @InnerField public CmsInt32U intgPd;
    @InnerField public CmsBoolean gi;
    @InnerField public CmsBoolean purgeBuf;
    @InnerField public CmsEntryId entryID;
    @InnerField public CmsBinaryTime timeOfEntry;
    @InnerField(optional = true) public CmsInt16 resvTms;
    @InnerField(optional = true) public CmsOctetString owner;

    public CmsBrcb() { super(new InnerBRCB()); }

    public CmsBrcb rptID(String v) { this.rptID.value(v); return this; }
    public CmsBrcb rptEna(boolean v) { this.rptEna.value(v); return this; }
    public CmsBrcb datSet(String v) { this.datSet.value(v); return this; }
    public CmsBrcb confRev(long v) { this.confRev.value(v); return this; }
    public CmsBrcb optFlds(CmsRcbOptFlds v) { this.optFlds.packed(v); return this; }
    public CmsBrcb bufTm(long v) { this.bufTm.value(v); return this; }
    public CmsBrcb sqNum(int v) { this.sqNum.value(v); return this; }
    public CmsBrcb trgOps(CmsTriggerConditions v) { this.trgOps.packed(v); return this; }
    public CmsBrcb intgPd(long v) { this.intgPd.value(v); return this; }
    public CmsBrcb gi(boolean v) { this.gi.value(v); return this; }
    public CmsBrcb purgeBuf(boolean v) { this.purgeBuf.value(v); return this; }
    public CmsBrcb entryID(byte[] v) { this.entryID.value(v); return this; }
    public CmsBrcb resvTms(int v) { this.resvTms.value(v); setPresent("resvTms", true); return this; }
    public CmsBrcb owner(byte[] v) { this.owner.value(v); setPresent("owner", true); return this; }
}

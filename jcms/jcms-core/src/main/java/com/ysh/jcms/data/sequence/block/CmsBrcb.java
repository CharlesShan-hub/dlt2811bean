package com.ysh.jcms.data.sequence.block;

import com.ysh.jcms.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.sequence.common.CmsEntryId;
import com.ysh.jcms.data.sequence.common.CmsObjectReference;
import com.ysh.jcms.data.core.CmsOctetString;
import com.ysh.jcms.data.core.CmsString;
import com.ysh.jcms.data.sequence.time.CmsBinaryTime;

public class CmsBrcb extends CmsSequence {
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
    @CmsField public CmsBoolean purgeBuf;
    @CmsField public CmsEntryId entryID;
    @CmsField public CmsBinaryTime timeOfEntry;
    @CmsField(optional = true) public CmsInt16 resvTms;
    @CmsField(optional = true) public CmsOctetString owner;

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

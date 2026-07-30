package com.ysh.jcms.data.sequence.block;

import com.ysh.jcms.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.scalar.CmsEntryId;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.core.CmsOctetString;
import com.ysh.jcms.data.scalar.CmsString;
import com.ysh.jcms.data.sequence.common.CmsBinaryTime;

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

    /** Copy all field values from another CmsBrcb (fluent). */
    public CmsBrcb value(CmsBrcb v) {
        rptID(v.rptID.value());
        rptEna(v.rptEna.value());
        datSet(v.datSet.value());
        confRev(v.confRev.value());
        optFlds(v.optFlds);
        bufTm(v.bufTm.value());
        sqNum(v.sqNum.value());
        trgOps(v.trgOps);
        intgPd(v.intgPd.value());
        gi(v.gi.value());
        purgeBuf(v.purgeBuf.value());
        entryID(v.entryID.value());
        if (v.isPresent("resvTms")) this.resvTms.value(v.resvTms.value());
        if (v.isPresent("owner")) this.owner.value(v.owner.value());
        return this;
    }
}

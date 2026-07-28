package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;

public class CmsUrcb extends CmsType {
    public String rptID;
    public CmsBoolean rptEna;
    public CmsObjectReference datSet;
    public CmsInt32U confRev;
    public CmsRcbOptFlds optFlds;
    public CmsInt32U bufTm;
    public CmsInt16U sqNum;
    public CmsTriggerConditions trgOps;
    public CmsInt32U intgPd;
    public CmsBoolean gi;
    public CmsBoolean resv;
    public byte[] owner;
    public boolean hasOwner;

    public CmsUrcb() {
        super(new InnerURCB());
        this.rptID = "";
        this.rptEna = new CmsBoolean();
        this.datSet = new CmsObjectReference();
        this.confRev = new CmsInt32U();
        this.optFlds = new CmsRcbOptFlds();
        this.bufTm = new CmsInt32U();
        this.sqNum = new CmsInt16U();
        this.trgOps = new CmsTriggerConditions();
        this.intgPd = new CmsInt32U();
        this.gi = new CmsBoolean();
        this.resv = new CmsBoolean();
        this.owner = new byte[0];
    }

    public CmsUrcb rptID(String v) { this.rptID = v; return this; }
    public CmsUrcb rptEna(boolean v) { this.rptEna.value(v); return this; }
    public CmsUrcb datSet(String v) { this.datSet.value(v); return this; }
    public CmsUrcb confRev(long v) { this.confRev.value(v); return this; }
    public CmsUrcb optFlds(CmsRcbOptFlds v) { this.optFlds = v; return this; }
    public CmsUrcb bufTm(long v) { this.bufTm.value(v); return this; }
    public CmsUrcb sqNum(int v) { this.sqNum.value(v); return this; }
    public CmsUrcb trgOps(CmsTriggerConditions v) { this.trgOps = v; return this; }
    public CmsUrcb intgPd(long v) { this.intgPd.value(v); return this; }
    public CmsUrcb gi(boolean v) { this.gi.value(v); return this; }
    public CmsUrcb resv(boolean v) { this.resv.value(v); return this; }
    public CmsUrcb owner(byte[] v) { this.owner = v; this.hasOwner = true; return this; }

    @Override
    public void syncToInner() {
        InnerURCB i = (InnerURCB) inner;
        i.rptID.value = rptID;
        i.rptEna.value = rptEna.value() ? 1 : 0;
        i.datSet.value.value = datSet.value();
        i.confRev.value = (int) confRev.value();
        optFlds.syncToInner();
        i.optFlds = (InnerRcbOptFlds) optFlds.inner;
        i.bufTm.value = (int) bufTm.value();
        i.sqNum.value = sqNum.value();
        trgOps.syncToInner();
        i.trgOps = (InnerTriggerConditions) trgOps.inner;
        i.intgPd.value = (int) intgPd.value();
        i.gi.value = gi.value() ? 1 : 0;
        i.resv.value = resv.value() ? 1 : 0;
        if (hasOwner && owner != null) {
            i.owner.value = owner;
            i._set.add("owner");
        }
    }

    @Override
    public void syncFromInner() {
        InnerURCB i = (InnerURCB) inner;
        rptID = i.rptID.value;
        rptEna.value(i.rptEna.value != 0);
        datSet.value(i.datSet.value.value);
        confRev.value(i.confRev.value & 0xFFFFFFFFL);
        optFlds.inner = i.optFlds;
        optFlds.syncFromInner();
        bufTm.value(i.bufTm.value & 0xFFFFFFFFL);
        sqNum.value(i.sqNum.value & 0xFFFF);
        trgOps.inner = i.trgOps;
        trgOps.syncFromInner();
        intgPd.value(i.intgPd.value & 0xFFFFFFFFL);
        gi.value(i.gi.value != 0);
        resv.value(i.resv.value != 0);
        hasOwner = i._set.contains("owner");
        if (hasOwner) {
            owner = i.owner.value;
        }
    }
}

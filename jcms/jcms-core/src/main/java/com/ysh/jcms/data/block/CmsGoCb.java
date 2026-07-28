package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;

public class CmsGoCb extends CmsType {
    public CmsBoolean goEna;
    public String goID;
    public CmsObjectReference datSet;
    public CmsInt32U confRev;
    public CmsBoolean ndsCom;
    public CmsPhyComAddr dstAddress;
    public boolean hasDstAddress;

    public CmsGoCb() {
        super(new InnerGoCB());
        this.goEna = new CmsBoolean();
        this.goID = "";
        this.datSet = new CmsObjectReference();
        this.confRev = new CmsInt32U();
        this.ndsCom = new CmsBoolean();
        this.dstAddress = new CmsPhyComAddr();
    }

    public CmsGoCb goEna(boolean v) { this.goEna.value(v); return this; }
    public CmsGoCb goID(String v) { this.goID = v; return this; }
    public CmsGoCb datSet(String v) { this.datSet.value(v); return this; }
    public CmsGoCb confRev(long v) { this.confRev.value(v); return this; }
    public CmsGoCb ndsCom(boolean v) { this.ndsCom.value(v); return this; }
    public CmsGoCb dstAddress(CmsPhyComAddr v) { this.dstAddress = v; this.hasDstAddress = true; return this; }

    @Override
    public void syncToInner() {
        InnerGoCB i = (InnerGoCB) inner;
        i.goEna.value = goEna.value() ? 1 : 0;
        i.goID.value = goID;
        i.datSet.value.value = datSet.value();
        i.confRev.value = (int) confRev.value();
        i.ndsCom.value = ndsCom.value() ? 1 : 0;
        if (hasDstAddress) {
            dstAddress.syncToInner();
            i.dstAddress = (InnerPhyComAddr) dstAddress.inner;
            i._set.add("dstAddress");
        }
    }

    @Override
    public void syncFromInner() {
        InnerGoCB i = (InnerGoCB) inner;
        goEna.value(i.goEna.value != 0);
        goID = i.goID.value;
        datSet.value(i.datSet.value.value);
        confRev.value(i.confRev.value & 0xFFFFFFFFL);
        ndsCom.value(i.ndsCom.value != 0);
        hasDstAddress = i._set.contains("dstAddress");
        if (hasDstAddress) {
            dstAddress.inner = i.dstAddress;
            dstAddress.syncFromInner();
        }
    }
}

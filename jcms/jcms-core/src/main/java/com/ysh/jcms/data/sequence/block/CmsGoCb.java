package com.ysh.jcms.data.sequence.block;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.sequence.common.CmsObjectReference;
import com.ysh.jcms.data.sequence.common.CmsPhyComAddr;
import com.ysh.jcms.data.core.CmsString;

public class CmsGoCb extends CmsSequence {
    @CmsField public CmsBoolean goEna;
    @CmsField public CmsString goID;
    @CmsField public CmsObjectReference datSet;
    @CmsField public CmsInt32U confRev;
    @CmsField public CmsBoolean ndsCom;
    @CmsField(optional = true) public CmsPhyComAddr dstAddress;

    public CmsGoCb() { super(new InnerGoCB()); }

    public CmsGoCb goEna(boolean v) { this.goEna.value(v); return this; }
    public CmsGoCb goID(String v) { this.goID.value(v); return this; }
    public CmsGoCb datSet(String v) { this.datSet.value(v); return this; }
    public CmsGoCb confRev(long v) { this.confRev.value(v); return this; }
    public CmsGoCb ndsCom(boolean v) { this.ndsCom.value(v); return this; }
    public CmsGoCb dstAddress(CmsPhyComAddr v) {
        this.dstAddress.value(v);
        setPresent("dstAddress", true);
        return this;
    }
}

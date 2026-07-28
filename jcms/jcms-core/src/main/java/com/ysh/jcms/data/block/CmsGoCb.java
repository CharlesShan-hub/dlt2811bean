package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.string.CmsString;

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

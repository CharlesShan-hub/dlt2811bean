package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.scalar.*;
import com.ysh.jcms.data.time.CmsUtcTime;

/** SGCB ::= SEQUENCE { numOfSG, actSG, editSG, tActEdt, resvTms } — 8.7.2 */
public class CmsSgcb extends CmsSequence {
    @CmsField public CmsInt8U numOfSG;
    @CmsField public CmsInt8U actSG;
    @CmsField public CmsInt8U editSG;
    @CmsField public CmsUtcTime tActEdt;       // TimeStamp ::= UtcTime
    @CmsField(optional = true) public CmsInt16U resvTms;

    public CmsSgcb() { super(new InnerSGCB()); }

    public CmsSgcb numOfSG(int v) { this.numOfSG.value(v); return this; }
    public CmsSgcb actSG(int v) { this.actSG.value(v); return this; }
    public CmsSgcb editSG(int v) { this.editSG.value(v); return this; }
    public CmsSgcb resvTms(int v) { this.resvTms.value(v); setPresent("resvTms", true); return this; }
}

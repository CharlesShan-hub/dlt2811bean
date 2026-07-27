package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.*;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;

/** SGCB ::= SEQUENCE { numOfSG, actSG, editSG, tActEdt, resvTms } — 8.7.2 */
public class CmsSgcb extends CmsType {

    public CmsInt8U numOfSG;
    public CmsInt8U actSG;
    public CmsInt8U editSG;
    public byte[] tActEdt; /* InnerTimeStamp.value (8 bytes) */
    public CmsInt16U resvTms; /* OPTIONAL */
    public boolean hasResvTms;

    public CmsSgcb() {
        super(new InnerSGCB());
        this.numOfSG = new CmsInt8U();
        this.actSG = new CmsInt8U();
        this.editSG = new CmsInt8U();
        this.tActEdt = new byte[8];
        this.resvTms = new CmsInt16U();
    }

    public CmsSgcb numOfSG(int v) { this.numOfSG.value(v); return this; }
    public CmsSgcb actSG(int v) { this.actSG.value(v); return this; }
    public CmsSgcb editSG(int v) { this.editSG.value(v); return this; }
    public CmsSgcb tActEdt(byte[] v) { this.tActEdt = v; return this; }
    public CmsSgcb resvTms(int v) { this.resvTms.value(v); this.hasResvTms = true; return this; }

    @Override
    public void syncToInner() {
        InnerSGCB i = (InnerSGCB) inner;
        i.numOfSG.value = numOfSG.value();
        i.actSG.value = actSG.value();
        i.editSG.value = editSG.value();
        i.tActEdt.value = tActEdt;
        if (hasResvTms) {
            i.resvTms.value = resvTms.value();
            i._set.add("resvTms");
        }
    }

    @Override
    public void syncFromInner() {
        InnerSGCB i = (InnerSGCB) inner;
        numOfSG.value(i.numOfSG.value);
        actSG.value(i.actSG.value);
        editSG.value(i.editSG.value);
        tActEdt = i.tActEdt.value;
        hasResvTms = i._set.contains("resvTms");
        if (hasResvTms) resvTms.value(i.resvTms.value);
    }
}

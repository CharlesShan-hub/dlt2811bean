package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;
import java.util.Arrays;
import java.util.List;

/**
 * SGCB ::= SEQUENCE { 6 fields }  —  8.5
 *
 * OPTIONAL field (resvTms) uses a CmsBoolean "present" flag before the value.
 */
public class CmsSgcb extends CmsType {

    public CmsInt8U     numOfSG;
    public CmsInt8U     actSG;
    public CmsInt8U     editSG;
    public CmsTimeStamp tActEdt;
    public CmsBoolean   resvTms_present;
    public CmsInt16U    resvTms;        /* OPTIONAL */

    public CmsSgcb() {
        this.numOfSG  = new CmsInt8U();
        this.actSG    = new CmsInt8U();
        this.editSG   = new CmsInt8U();
        this.tActEdt  = new CmsTimeStamp();
        this.resvTms_present = new CmsBoolean();
        this.resvTms  = new CmsInt16U();
    }
    
    // -- chain setters --
    public CmsSgcb numOfSG(int v) { this.numOfSG.value(v); return this; }
    public CmsSgcb actSG(int v) { this.actSG.value(v); return this; }
    public CmsSgcb editSG(int v) { this.editSG.value(v); return this; }
    public CmsSgcb tActEdt(CmsTimeStamp v) { this.tActEdt = v; return this; }
    public CmsSgcb resvTms_present(boolean v) { this.resvTms_present.value(v); return this; }
    public CmsSgcb resvTms(int v) { this.resvTms.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(numOfSG, actSG, editSG, tActEdt,
                             resvTms_present, resvTms);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSgcb(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSgcb(nativePtr, data); read(); }
}
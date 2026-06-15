package com.ysh.jcms.svc.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Select-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference
 * }  —  8.11.1
 */
public class CmsSelectError extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  reference;

    public CmsSelectError() {
        this.reqId     = new CmsReqId();
        this.reference = new CmsObjectReference();
    }
    
    // -- chain setters --
    public CmsSelectError reqId(int v) { this.reqId.value(v); return this; }
    public CmsSelectError reference(byte[] v) { this.reference.value(v); return this; }
    public CmsSelectError reference(String v) { this.reference.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSelectError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSelectError(nativePtr, data); read(); }
}
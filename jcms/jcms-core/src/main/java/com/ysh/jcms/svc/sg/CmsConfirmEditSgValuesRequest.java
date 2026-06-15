package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * ConfirmEditSGValues-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     sgcbReference       [0] IMPLICIT ObjectReference
 * }  —  8.6.4
 */
public class CmsConfirmEditSgValuesRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  sgcbReference;

    public CmsConfirmEditSgValuesRequest() {
        this.reqId         = new CmsReqId();
        this.sgcbReference = new CmsObjectReference();
    }
    
    // -- chain setters --
    public CmsConfirmEditSgValuesRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsConfirmEditSgValuesRequest sgcbReference(byte[] v) { this.sgcbReference.value(v); return this; }
    public CmsConfirmEditSgValuesRequest sgcbReference(String v) { this.sgcbReference.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, sgcbReference);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeConfirmEditSgValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeConfirmEditSgValuesRequest(nativePtr, data); read(); }
}
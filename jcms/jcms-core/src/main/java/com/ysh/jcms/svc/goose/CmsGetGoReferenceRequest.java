package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetGoReference-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     gocbReference   [0] IMPLICIT ObjectReference,
 *     memberOfs       [1] IMPLICIT SEQUENCE OF INT16U
 * }  —  8.9.2
 */
public class CmsGetGoReferenceRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  gocbReference;
    public CmsArray<CmsInt16U> memberOfs;   /* SEQUENCE OF INT16U */

    public CmsGetGoReferenceRequest() {
        this.reqId         = new CmsReqId();
        this.gocbReference = new CmsObjectReference();
        this.memberOfs     = new CmsArray<>();
    }
    
    // -- chain setters --
    public CmsGetGoReferenceRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetGoReferenceRequest gocbReference(byte[] v) { this.gocbReference.value(v); return this; }
    public CmsGetGoReferenceRequest gocbReference(String v) { this.gocbReference.value(v); return this; }
    public CmsGetGoReferenceRequest memberOfs(CmsArray<CmsInt16U> v) { this.memberOfs = v; return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, gocbReference, memberOfs);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetGoReferenceRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetGoReferenceRequest(nativePtr, data); read(); }
}
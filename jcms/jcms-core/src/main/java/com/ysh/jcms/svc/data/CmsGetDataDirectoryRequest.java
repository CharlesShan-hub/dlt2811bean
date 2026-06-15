package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataDirectory-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     dataReference   [0] IMPLICIT ObjectReference,
 *     referenceAfter  [1] IMPLICIT ObjectReference OPTIONAL
 * }  —  8.4.3
 */
public class CmsGetDataDirectoryRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  dataReference;
    public CmsBoolean          refAfterPresent;
    public CmsObjectReference  refAfter;       /* OPTIONAL */

    public CmsGetDataDirectoryRequest() {
        this.reqId           = new CmsReqId();
        this.dataReference   = new CmsObjectReference();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter        = new CmsObjectReference();
    }
    
    // -- chain setters --
    public CmsGetDataDirectoryRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetDataDirectoryRequest dataReference(byte[] v) { this.dataReference.value(v); return this; }
    public CmsGetDataDirectoryRequest dataReference(String v) { this.dataReference.value(v); return this; }
    public CmsGetDataDirectoryRequest refAfterPresent(boolean v) { this.refAfterPresent.value(v); return this; }
    public CmsGetDataDirectoryRequest refAfter(byte[] v) { this.refAfterPresent.value(v != null && v.length > 0); if (v != null) this.refAfter.value(v); return this; }
    public CmsGetDataDirectoryRequest refAfter(String v) { this.refAfterPresent.value(v != null); if (v != null) this.refAfter.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, dataReference, refAfterPresent, refAfter);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetDataDirectoryRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetDataDirectoryRequest(nativePtr, data); read(); }
}
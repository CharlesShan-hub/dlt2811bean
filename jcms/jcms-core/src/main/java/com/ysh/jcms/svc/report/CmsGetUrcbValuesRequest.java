package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetURCBValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT SEQUENCE OF ObjectReference
 * }  —  8.7.4
 */
public class CmsGetUrcbValuesRequest extends CmsType {

    public CmsReqId                        reqId;
    public CmsArray<CmsObjectReference>    reference;   /* SEQUENCE OF ObjectReference */

    public CmsGetUrcbValuesRequest() {
        this.reqId     = new CmsReqId();
        this.reference = new CmsArray<>(CmsObjectReference.class);
    }
    
    public CmsGetUrcbValuesRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetUrcbValuesRequest reference(CmsArray<CmsObjectReference> v) { this.reference = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetUrcbValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetUrcbValuesRequest(nativePtr, data); read(); }
}
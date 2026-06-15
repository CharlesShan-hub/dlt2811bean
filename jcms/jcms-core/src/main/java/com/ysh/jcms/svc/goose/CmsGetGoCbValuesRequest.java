package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetGoCbValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT SEQUENCE OF ObjectReference
 * }  —  8.9.4
 */
public class CmsGetGoCbValuesRequest extends CmsType {

    public CmsReqId                           reqId;
    public CmsArray<CmsObjectReference>       reference;  /* SEQUENCE OF ObjectReference */

    public CmsGetGoCbValuesRequest() {
        this.reqId     = new CmsReqId();
        this.reference = new CmsArray<>(CmsObjectReference.class);
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetGoCbValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetGoCbValuesRequest(nativePtr, data); read(); }
}

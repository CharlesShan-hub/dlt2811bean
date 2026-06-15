package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetSGCBValues-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     sgcbReference       [0] IMPLICIT SEQUENCE OF ObjectReference
 * }  —  8.6.6
 */
public class CmsGetSgcbValuesRequest extends CmsType {

    public CmsReqId                           reqId;
    public CmsArray<CmsObjectReference>       sgcbReference;  /* SEQUENCE OF ObjectReference */

    public CmsGetSgcbValuesRequest() {
        this.reqId         = new CmsReqId();
        this.sgcbReference = new CmsArray<>();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, sgcbReference);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetSgcbValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetSgcbValuesRequest(nativePtr, data); read(); }
}

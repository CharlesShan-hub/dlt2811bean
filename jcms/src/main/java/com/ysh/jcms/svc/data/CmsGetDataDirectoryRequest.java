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

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, dataReference, refAfterPresent, refAfter);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetDataDirectoryRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetDataDirectoryRequest(nativePtr, data); read(); }
}

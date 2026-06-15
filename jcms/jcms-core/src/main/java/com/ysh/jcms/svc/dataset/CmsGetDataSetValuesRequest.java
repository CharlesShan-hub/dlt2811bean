package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataSetValues-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL
 * }  —  8.5.1
 */
public class CmsGetDataSetValuesRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  datasetReference;
    public CmsBoolean          refAfterPresent;
    public CmsObjectReference  refAfter;       /* OPTIONAL */

    public CmsGetDataSetValuesRequest() {
        this.reqId            = new CmsReqId();
        this.datasetReference = new CmsObjectReference();
        this.refAfterPresent  = new CmsBoolean();
        this.refAfter         = new CmsObjectReference();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, datasetReference, refAfterPresent, refAfter);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetDataSetValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetDataSetValuesRequest(nativePtr, data); read(); }
}

package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
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

    public CmsGetDataSetValuesRequest() { super(Codec.GET_DATA_SET_VALUES_REQUEST);
        this.reqId            = new CmsReqId();
        this.datasetReference = new CmsObjectReference();
        this.refAfterPresent  = new CmsBoolean();
        this.refAfter         = new CmsObjectReference();
    }
    
    public CmsGetDataSetValuesRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetDataSetValuesRequest datasetReference(byte[] v) { this.datasetReference.value(v); return this; }
    public CmsGetDataSetValuesRequest datasetReference(String v) { this.datasetReference.value(v); return this; }
    public CmsGetDataSetValuesRequest refAfterPresent(boolean v) { this.refAfterPresent.value(v); return this; }
    public CmsGetDataSetValuesRequest refAfter(byte[] v) { this.refAfterPresent.value(v != null && v.length > 0); if (v != null) this.refAfter.value(v); return this; }
    public CmsGetDataSetValuesRequest refAfter(String v) { this.refAfterPresent.value(v != null); if (v != null) this.refAfter.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, datasetReference, refAfterPresent, refAfter);
    }
}
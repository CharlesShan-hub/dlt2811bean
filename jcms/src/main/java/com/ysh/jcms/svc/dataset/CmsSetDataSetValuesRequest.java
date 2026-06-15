package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetDataSetValues-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
 *     value               [2] IMPLICIT SEQUENCE OF Data
 * }  —  8.5.2
 */
public class CmsSetDataSetValuesRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  datasetReference;
    public CmsBoolean          refAfterPresent;
    public CmsObjectReference  refAfter;       /* OPTIONAL */
    public CmsArray<CmsData>   value;          /* SEQUENCE OF Data */

    public CmsSetDataSetValuesRequest() {
        this.reqId            = new CmsReqId();
        this.datasetReference = new CmsObjectReference();
        this.refAfterPresent  = new CmsBoolean();
        this.refAfter         = new CmsObjectReference();
        this.value            = new CmsArray<>(CmsData.class);
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, datasetReference, refAfterPresent, refAfter, value);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetDataSetValuesRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetDataSetValuesRequest(nativePtr, data); read(); }
}

package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetDataSetValues-RequestPDU ::= SEQUENCE { reqId Int16U, datasetReference [0]
 * IMPLICIT ObjectReference, referenceAfter [1] IMPLICIT ObjectReference
 * OPTIONAL, value [2] IMPLICIT SEQUENCE OF Data } — 8.5.2
 */
public class CmsSetDataSetValuesRequest extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsObjectReference datasetReference;
    public CmsBoolean refAfterPresent;
    public CmsObjectReference refAfter; /* OPTIONAL */
    public CmsArray<CmsData> value; /* SEQUENCE OF Data */

    public CmsSetDataSetValuesRequest() {
        super(Codec.SET_DATA_SET_VALUES_REQUEST);
        this.reqId = new CmsReqId();
        this.datasetReference = new CmsObjectReference();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter = new CmsObjectReference();
        this.value = new CmsArray<>(CmsData.class);
    }

    public CmsSetDataSetValuesRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsSetDataSetValuesRequest datasetReference(byte[] v) {
        this.datasetReference.value(v);
        return this;
    }
    public CmsSetDataSetValuesRequest datasetReference(String v) {
        this.datasetReference.value(v);
        return this;
    }
    public CmsSetDataSetValuesRequest refAfterPresent(boolean v) {
        this.refAfterPresent.value(v);
        return this;
    }
    public CmsSetDataSetValuesRequest refAfter(byte[] v) {
        this.refAfterPresent.value(v != null && v.length > 0);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }
    public CmsSetDataSetValuesRequest refAfter(String v) {
        this.refAfterPresent.value(v != null);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }
    public CmsSetDataSetValuesRequest value(CmsArray<CmsData> v) {
        this.value = v;
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, datasetReference, refAfterPresent, refAfter, value);
    }
}

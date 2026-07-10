package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * DeleteDataSet-RequestPDU ::= SEQUENCE { reqId Int16U, datasetReference [0]
 * IMPLICIT ObjectReference } — 8.5.4
 */
public class CmsDeleteDataSetRequest extends CmsType {

    public CmsReqId reqId;
    public CmsObjectReference datasetReference;

    public CmsDeleteDataSetRequest() {
        super(Codec.DELETE_DATA_SET_REQUEST);
        this.reqId = new CmsReqId();
        this.datasetReference = new CmsObjectReference();
    }

    public CmsDeleteDataSetRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsDeleteDataSetRequest datasetReference(byte[] v) {
        this.datasetReference.value(v);
        return this;
    }
    public CmsDeleteDataSetRequest datasetReference(String v) {
        this.datasetReference.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, datasetReference);
    }
}

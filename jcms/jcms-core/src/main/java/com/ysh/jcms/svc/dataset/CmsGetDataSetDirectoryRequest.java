package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataSetDirectory-RequestPDU ::= SEQUENCE { reqId Int16U, datasetReference
 * [0] IMPLICIT ObjectReference, referenceAfter [1] IMPLICIT ObjectReference
 * OPTIONAL } — 8.5.5
 */
public class CmsGetDataSetDirectoryRequest extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsObjectReference datasetReference;
    public CmsBoolean refAfterPresent;
    public CmsObjectReference refAfter; /* OPTIONAL */

    public CmsGetDataSetDirectoryRequest() {
        super(Codec.GET_DATA_SET_DIRECTORY_REQUEST);
        this.reqId = new CmsReqId();
        this.datasetReference = new CmsObjectReference();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter = new CmsObjectReference();
    }

    public CmsGetDataSetDirectoryRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetDataSetDirectoryRequest datasetReference(byte[] v) {
        this.datasetReference.value(v);
        return this;
    }
    public CmsGetDataSetDirectoryRequest datasetReference(String v) {
        this.datasetReference.value(v);
        return this;
    }
    public CmsGetDataSetDirectoryRequest refAfterPresent(boolean v) {
        this.refAfterPresent.value(v);
        return this;
    }
    public CmsGetDataSetDirectoryRequest refAfter(byte[] v) {
        this.refAfterPresent.value(v != null && v.length > 0);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }
    public CmsGetDataSetDirectoryRequest refAfter(String v) {
        this.refAfterPresent.value(v != null);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, datasetReference, refAfterPresent, refAfter);
    }
}

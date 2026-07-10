package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetLCBValues-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0] IMPLICIT
 * SEQUENCE OF ObjectReference } — 8.8.2
 */
public class CmsGetLcbValuesRequest extends CmsType {

    public CmsReqId reqId;
    public CmsArray<CmsObjectReference> reference; /* SEQUENCE OF ObjectReference */

    public CmsGetLcbValuesRequest() {
        super(Codec.GET_LCB_VALUES_REQUEST);
        this.reqId = new CmsReqId();
        this.reference = new CmsArray<>(CmsObjectReference.class);
    }

    public CmsGetLcbValuesRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetLcbValuesRequest reference(CmsArray<CmsObjectReference> v) {
        this.reference = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference);
    }
}

package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetMSVCBValues-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0] IMPLICIT
 * SEQUENCE OF ObjectReference } — 8.10.2
 */
public class CmsGetMsvcbValuesRequest extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsObjectReference> reference; /* SEQUENCE OF ObjectReference */

    public CmsGetMsvcbValuesRequest() {
        super(Codec.GET_MSVCB_VALUES_REQUEST);
        this.reqId = new CmsReqId();
        this.reference = new CmsArray<>(CmsObjectReference.class);
    }

    public CmsGetMsvcbValuesRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetMsvcbValuesRequest reference(CmsArray<CmsObjectReference> v) {
        this.reference = v;
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, reference);
    }
}

package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetLogStatusValues-RequestPDU ::= SEQUENCE { reqId Int16U, logReference [0]
 * IMPLICIT SEQUENCE OF ObjectReference } — 8.8.6
 */
public class CmsGetLogStatusValuesRequest extends CmsType {

    public CmsReqId reqId;
    public CmsArray<CmsObjectReference> logReference; /* SEQUENCE OF ObjectReference */

    public CmsGetLogStatusValuesRequest() {
        super(Codec.GET_LOG_STATUS_VALUES_REQUEST);
        this.reqId = new CmsReqId();
        this.logReference = new CmsArray<>(CmsObjectReference.class);
    }

    public CmsGetLogStatusValuesRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetLogStatusValuesRequest logReference(CmsArray<CmsObjectReference> v) {
        this.logReference = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, logReference);
    }
}

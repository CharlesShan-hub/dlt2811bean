package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetSGCBValues-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     sgcbReference       [0] IMPLICIT SEQUENCE OF ObjectReference
 * }  —  8.6.6
 */
public class CmsGetSgcbValuesRequest extends CmsType {

    public CmsReqId                           reqId;
    public CmsArray<CmsObjectReference>       sgcbReference;  /* SEQUENCE OF ObjectReference */

    public CmsGetSgcbValuesRequest() { super(Codec.GET_SGCB_VALUES_REQUEST);
        this.reqId         = new CmsReqId();
        this.sgcbReference = new CmsArray<>();
    }
    
    public CmsGetSgcbValuesRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetSgcbValuesRequest sgcbReference(CmsArray<CmsObjectReference> v) { this.sgcbReference = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, sgcbReference);
    }
}
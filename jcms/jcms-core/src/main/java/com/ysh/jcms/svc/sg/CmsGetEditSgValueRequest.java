package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetEditSGValue-RequestPDU ::= SEQUENCE { reqId Int16U, data [0] IMPLICIT
 * SEQUENCE OF SGRefFcEntry } — 8.6.5
 */
public class CmsGetEditSgValueRequest extends CmsType {

    public CmsReqId reqId;
    public CmsArray<CmsSgRefFcEntry> data; /* SEQUENCE OF SGRefFcEntry */

    public CmsGetEditSgValueRequest() {
        super(Codec.GET_EDIT_SG_VALUE_REQUEST);
        this.reqId = new CmsReqId();
        this.data = new CmsArray<>(CmsSgRefFcEntry.class);
    }

    public CmsGetEditSgValueRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetEditSgValueRequest data(CmsArray<CmsSgRefFcEntry> v) {
        this.data = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, data);
    }
}

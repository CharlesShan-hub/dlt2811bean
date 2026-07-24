package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * ConfirmEditSGValues-RequestPDU ::= SEQUENCE { reqId Int16U, sgcbReference [0]
 * IMPLICIT ObjectReference } — 8.6.4
 */
public class CmsConfirmEditSgValuesRequest extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsObjectReference sgcbReference;

    public CmsConfirmEditSgValuesRequest() {
        super(Codec.CONFIRM_EDIT_SG_VALUES_REQUEST);
        this.reqId = new CmsReqId();
        this.sgcbReference = new CmsObjectReference();
    }

    public CmsConfirmEditSgValuesRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsConfirmEditSgValuesRequest sgcbReference(byte[] v) {
        this.sgcbReference.value(v);
        return this;
    }
    public CmsConfirmEditSgValuesRequest sgcbReference(String v) {
        this.sgcbReference.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, sgcbReference);
    }
}

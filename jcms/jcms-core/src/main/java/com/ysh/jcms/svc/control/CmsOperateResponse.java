package com.ysh.jcms.svc.control;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Operate-ResponsePDU ::= SEQUENCE { reqId Int16U, reference [0] IMPLICIT
 * ObjectReference } — 8.11.3
 */
public class CmsOperateResponse extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsObjectReference reference;

    public CmsOperateResponse() {
        super(Codec.OPERATE_RESPONSE);
        this.reqId = new CmsReqId();
        this.reference = new CmsObjectReference();
    }

    public CmsOperateResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsOperateResponse reference(byte[] v) {
        this.reference.value(v);
        return this;
    }
    public CmsOperateResponse reference(String v) {
        this.reference.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, reference);
    }
}

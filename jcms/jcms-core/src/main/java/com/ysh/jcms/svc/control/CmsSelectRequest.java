package com.ysh.jcms.svc.control;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Select-RequestPDU ::= SEQUENCE { reqId Int16U, reference [0] IMPLICIT
 * ObjectReference } — 8.11.1
 */
public class CmsSelectRequest extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsObjectReference reference;

    public CmsSelectRequest() {
        super(Codec.SELECT_REQUEST);
        this.reqId = new CmsReqId();
        this.reference = new CmsObjectReference();
    }

    public CmsSelectRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsSelectRequest reference(byte[] v) {
        this.reference.value(v);
        return this;
    }
    public CmsSelectRequest reference(String v) {
        this.reference.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, reference);
    }
}

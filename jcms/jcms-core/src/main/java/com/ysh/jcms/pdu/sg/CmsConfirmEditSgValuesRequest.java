package com.ysh.jcms.pdu.sg;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerConfirmEditSGValuesRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * ConfirmEditSGValues-RequestPDU ::= SEQUENCE { sgcbReference [0] IMPLICIT
 * ObjectReference } — 8.6.4
 */
public class CmsConfirmEditSgValuesRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference sgcbReference;

    public CmsConfirmEditSgValuesRequest() {
        super(new InnerConfirmEditSGValuesRequestPDU());
    }

    public CmsConfirmEditSgValuesRequest sgcbReference(String v) {
        this.sgcbReference.value(v);
        return this;
    }
    public CmsConfirmEditSgValuesRequest sgcbReference(byte[] v) {
        return sgcbReference(new String(v, StandardCharsets.UTF_8));
    }
}

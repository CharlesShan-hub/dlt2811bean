package com.ysh.jcms.core.pdu.sg;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerConfirmEditSGValuesRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * ConfirmEditSGValues-RequestPDU ::= SEQUENCE {
 *     sgcbReference       [0] IMPLICIT ObjectReference
 * } — 8.6.4
 * }
 * </pre>
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

package com.ysh.jcms.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerSelectResponsePDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * Select-ResponsePDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference
 * } — 8.11.1
 * }
 * </pre>
 */
public class CmsSelectResponse extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;

    public CmsSelectResponse() {
        super(new InnerSelectResponsePDU());
    }

    public CmsSelectResponse reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsSelectResponse reference(String v) {
        this.reference.value(v);
        return this;
    }
}

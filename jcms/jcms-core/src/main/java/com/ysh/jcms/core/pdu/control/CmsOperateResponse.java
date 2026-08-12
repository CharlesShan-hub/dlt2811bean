package com.ysh.jcms.core.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerOperateResponsePDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * Operate-ResponsePDU ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference
 * } — 8.11.3
 * }
 * </pre>
 */
public class CmsOperateResponse extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;

    public CmsOperateResponse() {
        super(new InnerOperateResponsePDU());
    }

    public CmsOperateResponse reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsOperateResponse reference(String v) {
        this.reference.value(v);
        return this;
    }
}

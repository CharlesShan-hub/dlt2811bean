package com.ysh.jcms.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerSelectRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * Select-RequestPDU ::= SEQUENCE { reference [0] IMPLICIT ObjectReference } —
 * 8.11.1
 */
public class CmsSelectRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;

    public CmsSelectRequest() {
        super(new InnerSelectRequestPDU());
    }

    public CmsSelectRequest reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsSelectRequest reference(String v) {
        this.reference.value(v);
        return this;
    }
}

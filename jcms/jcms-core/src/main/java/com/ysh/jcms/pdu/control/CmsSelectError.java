package com.ysh.jcms.pdu.control;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerSelectErrorPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * Select-ErrorPDU ::= SEQUENCE { reference [0] IMPLICIT ObjectReference } —
 * 8.11.1
 */
public class CmsSelectError extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;

    public CmsSelectError() {
        super(new InnerSelectErrorPDU());
    }

    public CmsSelectError reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsSelectError reference(String v) {
        this.reference.value(v);
        return this;
    }

    public CmsSelectError value(CmsSelectError v) {
        return reference(v.reference.value());
    }
}

package com.ysh.jcms.pdu.control;

import com.ysh.jcms.data.InnerOperateResponsePDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * Operate-ResponsePDU ::= SEQUENCE { reference [0] IMPLICIT ObjectReference } — 8.11.3
 */
public class CmsOperateResponse extends CmsSequence {

    @CmsField public CmsObjectReference reference;

    public CmsOperateResponse() { super(new InnerOperateResponsePDU()); }

    public CmsOperateResponse reference(byte[] v) { this.reference.value(new String(v)); return this; }
    public CmsOperateResponse reference(String v) { this.reference.value(v); return this; }

    public CmsOperateResponse value(CmsOperateResponse v) {
        return reference(v.reference.value());
    }
}

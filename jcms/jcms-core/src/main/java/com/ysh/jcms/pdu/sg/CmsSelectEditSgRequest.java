package com.ysh.jcms.pdu.sg;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerSelectEditSGRequestPDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsInt8U;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * SelectEditSG-RequestPDU ::= SEQUENCE { sgcbReference [0] IMPLICIT
 * ObjectReference, settingGroupNumber [1] IMPLICIT Int8U } — 8.6.2
 */
public class CmsSelectEditSgRequest extends CmsSequence {

    @CmsField
    public CmsObjectReference sgcbReference;

    @CmsField
    public CmsInt8U settingGroupNumber;

    public CmsSelectEditSgRequest() {
        super(new InnerSelectEditSGRequestPDU());
    }

    public CmsSelectEditSgRequest sgcbReference(String v) {
        this.sgcbReference.value(v);
        return this;
    }
    public CmsSelectEditSgRequest sgcbReference(byte[] v) {
        return sgcbReference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsSelectEditSgRequest settingGroupNumber(int v) {
        this.settingGroupNumber.value(v);
        return this;
    }
}

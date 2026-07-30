package com.ysh.jcms.data.sequence.directory;

import com.ysh.jcms.data.InnerAnonymousGetAllCBValuesResponsePDUCbValue;
import com.ysh.jcms.data.choice.CmsCbValueChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsSubReference;
/*
 * CBValueEntry ::= SEQUENCE { reference [0] IMPLICIT SubReference, value [1]
 * IMPLICIT CBValue } — 8.3.6
 *
 * Used by GetAllCBValues response (SEQUENCE OF CBValueEntry).
 * Backed by {@link InnerAnonymousGetAllCBValuesResponsePDUCbValue}.
 */
public class CmsCbValueEntry extends CmsSequence {

    @CmsField public CmsSubReference reference;
    @CmsField public CmsCbValueChoice value;

    public CmsCbValueEntry() {
        super(new InnerAnonymousGetAllCBValuesResponsePDUCbValue());
    }

    public CmsCbValueEntry reference(byte[] v) { this.reference.value(new String(v)); return this; }
    public CmsCbValueEntry reference(String v) { this.reference.value(v); return this; }
    public CmsCbValueEntry value(CmsCbValueChoice v) { this.value.value(v); return this; }
}

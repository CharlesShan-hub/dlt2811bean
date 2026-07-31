package com.ysh.jcms.data.sequence.directory;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerAnonymousGetAllDataValuesResponsePDUData;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.scalar.CmsSubReference;

/**
 * DataValueEntry ::= SEQUENCE { reference [0] IMPLICIT SubReference, value [1]
 * IMPLICIT Data } — 8.3.4
 *
 * Used by GetAllDataValues response (SEQUENCE OF DataValueEntry).
 */
public class CmsDataValueEntry extends CmsSequence {

    @CmsField public CmsSubReference reference;
    @CmsField public CmsData value;

    public CmsDataValueEntry() {
        super(new InnerAnonymousGetAllDataValuesResponsePDUData());
    }

    public CmsDataValueEntry reference(byte[] v) { this.reference.value(new String(v)); return this; }
    public CmsDataValueEntry reference(String v) { this.reference.value(v); return this; }
    public CmsDataValueEntry value(CmsData v) { this.value.value(v); return this; }
}

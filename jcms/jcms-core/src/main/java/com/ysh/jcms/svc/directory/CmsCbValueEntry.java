package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.InnerEmpty;
import com.ysh.jcms.data.common.CmsSubReference;

/**
 * CBValueEntry ::= SEQUENCE { reference [0] IMPLICIT SubReference, value [1]
 * IMPLICIT CBValue } — 8.3.6
 *
 * Used by GetAllCBValues response (SEQUENCE OF CBValueEntry).
 * Inner binding is handled by the parent PDU's manual sync.
 */
public class CmsCbValueEntry extends CmsSequence {

    @CmsField public CmsSubReference reference;
    @CmsField public CmsCbValueChoice value;

    public CmsCbValueEntry() {
        super(new InnerEmpty());
        this.reference = new CmsSubReference();
        this.value = new CmsCbValueChoice();
    }

    public CmsCbValueEntry reference(byte[] v) { this.reference.value(new String(v)); return this; }
    public CmsCbValueEntry reference(String v) { this.reference.value(v); return this; }
    public CmsCbValueEntry value(CmsCbValueChoice v) { this.value = v; return this; }
}

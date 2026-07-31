package com.ysh.jcms.data.sequence.sg;

import com.ysh.jcms.data.InnerAnonymousSetEditSGValueRequestPDUData;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * SGRefValueEntry ::= SEQUENCE { reference [0] IMPLICIT ObjectReference, value
 * [2] IMPLICIT Data } — used by SetEditSGValue-RequestPDU.
 */
public class CmsSgRefValueEntry extends CmsSequence {

    @CmsField public CmsObjectReference reference;
    @CmsField public CmsData value;

    public CmsSgRefValueEntry() {
        super(new InnerAnonymousSetEditSGValueRequestPDUData());
    }

    public CmsSgRefValueEntry reference(String v) { this.reference.value(v); return this; }
    public CmsSgRefValueEntry reference(byte[] v) { return reference(new String(v)); }
    public CmsSgRefValueEntry value(CmsData v) { this.value.value(v); return this; }

    /** Copy all field values from another CmsSgRefValueEntry (fluent). */
    public CmsSgRefValueEntry value(CmsSgRefValueEntry v) {
        return reference(v.reference.value()).value(v.value);
    }
}

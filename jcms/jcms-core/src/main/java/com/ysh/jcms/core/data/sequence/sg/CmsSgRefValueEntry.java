package com.ysh.jcms.core.data.sequence.sg;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerAnonymousSetEditSGValueRequestPDUData;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * SGRefValueEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     value       [2] IMPLICIT Data
 * }
 * }
 * </pre>
 *
 * <p>
 * used by SetEditSGValue-RequestPDU.
 */
public class CmsSgRefValueEntry extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;
    @CmsField
    public CmsData value;

    public CmsSgRefValueEntry() {
        super(new InnerAnonymousSetEditSGValueRequestPDUData());
    }

    public CmsSgRefValueEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsSgRefValueEntry reference(byte[] v) {
        return reference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsSgRefValueEntry value(CmsData v) {
        this.value.value(v);
        return this;
    }

    /** Copy all field values from another CmsSgRefValueEntry (fluent). */
    public CmsSgRefValueEntry value(CmsSgRefValueEntry v) {
        return reference(v.reference.value()).value(v.value);
    }
}

package com.ysh.jcms.core.data.sequence.goose;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerEmpty;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * GoRefFcEntry ::= SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     fc              [1] IMPLICIT FunctionalConstraint
 * } — 8.9.2
 * }
 * </pre>
 *
 * <p>
 * Used by GetGoReference response, GetGOOSEElementNumber request.
 */
public class CmsGoRefFcEntry extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;

    @CmsField
    public CmsFC fc;

    public CmsGoRefFcEntry() {
        super(new InnerEmpty());
    }

    public CmsGoRefFcEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsGoRefFcEntry reference(byte[] v) {
        return reference(new String(v, StandardCharsets.UTF_8));
    }
    public CmsGoRefFcEntry fc(int v) {
        this.fc.value(v);
        return this;
    }

    public CmsGoRefFcEntry value(CmsGoRefFcEntry v) {
        this.reference.value(v.reference.value());
        this.fc.value(v.fc.value());
        return this;
    }
}

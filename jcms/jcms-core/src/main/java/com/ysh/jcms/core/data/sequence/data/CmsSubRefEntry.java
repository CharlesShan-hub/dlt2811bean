package com.ysh.jcms.core.data.sequence.data;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.scalar.CmsSubReference;

/**
 * <pre>
 * {@code
 * SEQUENCE {
 *     reference     [0] IMPLICIT SubReference,
 *     fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 * } — SEQUENCE OF element — inline within GetDataDirectory-ResponsePDU
 * }
 * </pre>
 *
 * <p>
 * Used by GetDataDirectory Response (SEQUENCE OF SubRefEntry).
 */
public class CmsSubRefEntry extends CmsSequence {

    @CmsField
    public CmsSubReference reference;

    @CmsField(optional = true)
    public CmsFC fc;

    public CmsSubRefEntry() {
        super(new com.ysh.jcms.data.InnerEmpty());
    }

    public CmsSubRefEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsSubRefEntry reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsSubRefEntry fc(int v) {
        setPresent("fc", true);
        this.fc.value(v);
        return this;
    }

    public CmsSubRefEntry value(CmsSubRefEntry v) {
        this.reference.value(v.reference.value());
        if (v.isPresent("fc")) {
            setPresent("fc", true);
            this.fc.value(v.fc.value());
        } else {
            setPresent("fc", false);
        }
        return this;
    }
}

package com.ysh.jcms.core.data.sequence.data;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 * } — SEQUENCE OF element — inline within GetDataValues-RequestPDU / GetDataDefinition-RequestPDU
 * }
 * </pre>
 *
 * <p>
 * Used by GetDataValues Request, GetDataDefinition Request.
 */
public class CmsDataRefEntry extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;

    @CmsField(optional = true)
    public CmsFC fc;

    public CmsDataRefEntry() {
        super(new com.ysh.jcms.data.InnerEmpty());
    }

    public CmsDataRefEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsDataRefEntry reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsDataRefEntry fc(int v) {
        setPresent("fc", true);
        this.fc.value(v);
        return this;
    }
    public CmsDataRefEntry fc(String v) {
        if (v != null && !v.isEmpty() && !"XX".equalsIgnoreCase(v)) {
            this.fc.value(v);
            setPresent("fc", true);
        } else {
            setPresent("fc", false);
        }
        return this;
    }
    

    public CmsDataRefEntry value(CmsDataRefEntry v) {
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

package com.ysh.jcms.data.sequence.data;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * (inline type within GetDataValues-RequestPDU / GetDataDefinition-RequestPDU
 * ::= SEQUENCE {)<br>
 * {@code
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 * }
 *
 * <p>Used by GetDataValues Request, GetDataDefinition Request.
 */
public class CmsDataRefEntry extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;

    @CmsField(optional = true)
    public CmsFC fc; /* OPTIONAL */

    public CmsDataRefEntry() {
        super(new com.ysh.jcms.data.InnerEmpty());
        this.reference = new CmsObjectReference();
        this.fc = new CmsFC();
    }

    public CmsDataRefEntry reference(String v) { this.reference.value(v); return this; }
    public CmsDataRefEntry reference(byte[] v) { this.reference.value(new String(v, StandardCharsets.UTF_8)); return this; }
    public CmsDataRefEntry fc(int v) {
        setPresent("fc", true);
        this.fc.value(v);
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

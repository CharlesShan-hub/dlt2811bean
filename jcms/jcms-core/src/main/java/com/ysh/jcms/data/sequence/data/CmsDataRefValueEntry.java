package com.ysh.jcms.data.sequence.data;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * (inline type within SetDataValues-RequestPDU ::= SEQUENCE {)<br>
 * {@code
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     value         [2] IMPLICIT Data
 * }
 *
 * <p>Used by SetDataValues Request.
 */
public class CmsDataRefValueEntry extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;

    @CmsField(optional = true)
    public CmsFC fc;

    @CmsField
    public CmsData value;

    public CmsDataRefValueEntry() {
        super(new com.ysh.jcms.data.InnerEmpty());
        this.reference = new CmsObjectReference();
        this.fc = new CmsFC();
        this.value = new CmsData();
    }

    public CmsDataRefValueEntry reference(String v) { this.reference.value(v); return this; }
    public CmsDataRefValueEntry reference(byte[] v) { this.reference.value(new String(v, StandardCharsets.UTF_8)); return this; }
    public CmsDataRefValueEntry fc(int v) {
        setPresent("fc", true);
        this.fc.value(v);
        return this;
    }
    public CmsDataRefValueEntry value(CmsData v) {
        this.value.value(v);
        return this;
    }

    public CmsDataRefValueEntry value(CmsDataRefValueEntry v) {
        this.reference.value(v.reference.value());
        if (v.isPresent("fc")) {
            setPresent("fc", true);
            this.fc.value(v.fc.value());
        } else {
            setPresent("fc", false);
        }
        this.value.value(v.value);
        return this;
    }
}

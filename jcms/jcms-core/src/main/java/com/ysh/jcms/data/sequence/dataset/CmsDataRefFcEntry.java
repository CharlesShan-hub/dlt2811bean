package com.ysh.jcms.data.sequence.dataset;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint
 * } — SEQUENCE OF element — inline within CreateDataSet-RequestPDU / GetDataSetDirectory-ResponsePDU
 * }
 * </pre>
 *
 * <p>
 * Used by CreateDataSet Request memberData and GetDataSetDirectory Response
 * memberData.
 */
public class CmsDataRefFcEntry extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;

    @CmsField
    public CmsFC fc;

    public CmsDataRefFcEntry() {
        super(new com.ysh.jcms.data.InnerEmpty());
        this.reference = new CmsObjectReference();
        this.fc = new CmsFC();
    }

    public CmsDataRefFcEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsDataRefFcEntry reference(byte[] v) {
        this.reference.value(new String(v, StandardCharsets.UTF_8));
        return this;
    }
    public CmsDataRefFcEntry fc(int v) {
        this.fc.value(v);
        return this;
    }

    public CmsDataRefFcEntry value(CmsDataRefFcEntry v) {
        this.reference.value(v.reference.value());
        this.fc.value(v.fc.value());
        return this;
    }
}

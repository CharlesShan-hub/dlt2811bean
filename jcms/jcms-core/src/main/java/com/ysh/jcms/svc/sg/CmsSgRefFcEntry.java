package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFC;
import java.util.Arrays;
import java.util.List;

/**
 * SGRefFcEntry ::= SEQUENCE { reference [0] IMPLICIT ObjectReference, fc [1]
 * IMPLICIT FunctionalConstraint }
 *
 * Used by GetEditSGValue Request.
 */
public class CmsSgRefFcEntry extends CmsType {

    public CmsObjectReference reference;
    public CmsFC fc;

    public CmsSgRefFcEntry() {
        this.reference = new CmsObjectReference();
        this.fc = new CmsFC();
    }

    public CmsSgRefFcEntry reference(byte[] v) {
        this.reference.value(v);
        return this;
    }
    public CmsSgRefFcEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsSgRefFcEntry fc(int v) {
        this.fc.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, fc);
    }
}

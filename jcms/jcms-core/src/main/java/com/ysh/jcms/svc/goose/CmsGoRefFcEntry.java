package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFC;
import java.util.Arrays;
import java.util.List;

/**
 * GoRefFcEntry ::= SEQUENCE { reference [0] IMPLICIT ObjectReference, fc [1]
 * IMPLICIT FunctionalConstraint }
 *
 * Used by GetGoReference response, GetGOOSEElementNumber request.
 */
public class CmsGoRefFcEntry extends CmsType {

    public CmsObjectReference reference;
    public CmsFC fc;

    public CmsGoRefFcEntry() {
        this.reference = new CmsObjectReference();
        this.fc = new CmsFC();
    }

    public CmsGoRefFcEntry reference(byte[] v) {
        this.reference.value(v);
        return this;
    }
    public CmsGoRefFcEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsGoRefFcEntry fc(int v) {
        this.fc.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, fc);
    }
}

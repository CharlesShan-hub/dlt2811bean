package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * DataRefEntry ::= SEQUENCE { reference [0] IMPLICIT ObjectReference, fc [1]
 * IMPLICIT FunctionalConstraint OPTIONAL }
 *
 * Used by GetDataValues Request, GetDataDefinition Request.
 */
public class CmsDataRefEntry extends CmsTypeOld {

    public CmsObjectReference reference;
    public CmsBoolean fcPresent;
    public CmsFC fc; /* OPTIONAL */

    public CmsDataRefEntry() {
        this.reference = new CmsObjectReference();
        this.fcPresent = new CmsBoolean();
        this.fc = new CmsFC();
    }

    public CmsDataRefEntry reference(byte[] v) {
        this.reference.value(v);
        return this;
    }
    public CmsDataRefEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsDataRefEntry fcPresent(boolean v) {
        this.fcPresent.value(v);
        return this;
    }
    public CmsDataRefEntry fc(int v) {
        this.fcPresent.value(true);
        this.fc.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reference, fcPresent, fc);
    }
}

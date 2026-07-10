package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFC;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * DataRefValueEntry ::= SEQUENCE { reference [0] IMPLICIT ObjectReference, fc
 * [1] IMPLICIT FunctionalConstraint OPTIONAL, value [2] IMPLICIT Data }
 *
 * Used by SetDataValues Request.
 */
public class CmsDataRefValueEntry extends CmsType {

    public CmsObjectReference reference;
    public CmsBoolean fcPresent;
    public CmsFC fc; /* OPTIONAL */
    public CmsData value;

    public CmsDataRefValueEntry() {
        this.reference = new CmsObjectReference();
        this.fcPresent = new CmsBoolean();
        this.fc = new CmsFC();
        this.value = new CmsData();
    }

    public CmsDataRefValueEntry reference(byte[] v) {
        this.reference.value(v);
        return this;
    }
    public CmsDataRefValueEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsDataRefValueEntry fcPresent(boolean v) {
        this.fcPresent.value(v);
        return this;
    }
    public CmsDataRefValueEntry fc(int v) {
        this.fcPresent.value(true);
        this.fc.value(v);
        return this;
    }

    public CmsDataRefValueEntry value(CmsData v) {
        this.value = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, fcPresent, fc, value);
    }
}

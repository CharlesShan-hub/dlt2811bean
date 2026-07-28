package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import java.util.Arrays;
import java.util.List;

/**
 * SGRefValueEntry ::= SEQUENCE { reference [0] IMPLICIT ObjectReference, value
 * [2] IMPLICIT Data }
 *
 * Used by SetEditSGValue Request.
 */
public class CmsSgRefValueEntry extends CmsTypeOld {

    public CmsObjectReference reference;
    public CmsData value;

    public CmsSgRefValueEntry() {
        this.reference = new CmsObjectReference();
        this.value = new CmsData();
    }

    public CmsSgRefValueEntry reference(byte[] v) {
        this.reference.value(v);
        return this;
    }
    public CmsSgRefValueEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsSgRefValueEntry value(CmsData v) {
        this.value = v;
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reference, value);
    }
}

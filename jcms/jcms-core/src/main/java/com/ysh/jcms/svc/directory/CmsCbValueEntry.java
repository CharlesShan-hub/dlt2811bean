package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsSubReference;
import java.util.Arrays;
import java.util.List;

/**
 * CBValueEntry ::= SEQUENCE { reference [0] IMPLICIT SubReference, value [1]
 * IMPLICIT CBValue } — 8.3.6
 *
 * Used by GetAllCBValues response (SEQUENCE OF CBValueEntry).
 */
public class CmsCbValueEntry extends CmsType {

    public CmsSubReference reference;
    public CmsCbValueChoice value;

    public CmsCbValueEntry() {
        this.reference = new CmsSubReference();
        this.value = new CmsCbValueChoice();
    }

    public CmsCbValueEntry reference(byte[] v) {
        this.reference.value(v);
        return this;
    }
    public CmsCbValueEntry reference(String v) {
        this.reference.value(v);
        return this;
    }
    public CmsCbValueEntry value(CmsCbValueChoice v) {
        this.value = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, value);
    }
}

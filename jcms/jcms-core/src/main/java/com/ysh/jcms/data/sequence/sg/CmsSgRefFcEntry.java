package com.ysh.jcms.data.sequence.sg;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerAnonymousGetEditSGValueRequestPDUData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * SGRefFcEntry ::= SEQUENCE { reference [0] IMPLICIT ObjectReference, fc [1]
 * IMPLICIT FunctionalConstraint } — used by GetEditSGValue-RequestPDU.
 */
public class CmsSgRefFcEntry extends CmsSequence {

    @CmsField public CmsObjectReference reference;
    @CmsField public CmsFC fc;

    public CmsSgRefFcEntry() {
        super(new InnerAnonymousGetEditSGValueRequestPDUData());
    }

    public CmsSgRefFcEntry reference(String v) { this.reference.value(v); return this; }
    public CmsSgRefFcEntry reference(byte[] v) { return reference(new String(v, StandardCharsets.UTF_8)); }
    public CmsSgRefFcEntry fc(int v) { this.fc.value(v); return this; }

    /** Copy all field values from another CmsSgRefFcEntry (fluent). */
    public CmsSgRefFcEntry value(CmsSgRefFcEntry v) {
        return reference(v.reference.value()).fc(v.fc.value());
    }
}

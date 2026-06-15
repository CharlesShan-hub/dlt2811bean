package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsSubReference;
import com.ysh.jcms.data.fc.CmsFunctionalConstraint;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * SubRefEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT SubReference,
 *     fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 * }
 *
 * Used by GetDataDirectory Response (SEQUENCE OF SubRefEntry).
 */
public class CmsSubRefEntry extends CmsType {

    public CmsSubReference        reference;
    public CmsBoolean             fcPresent;
    public CmsFunctionalConstraint fc;            /* OPTIONAL */

    public CmsSubRefEntry() {
        this.reference = new CmsSubReference();
        this.fcPresent = new CmsBoolean();
        this.fc        = new CmsFunctionalConstraint();
    }
    
    public CmsSubRefEntry reference(byte[] v) { this.reference.value(v); return this; }
    public CmsSubRefEntry reference(String v) { this.reference.value(v); return this; }
    public CmsSubRefEntry fcPresent(boolean v) { this.fcPresent.value(v); return this; }
    public CmsSubRefEntry fc(byte[] v) { this.fcPresent.value(v != null && v.length > 0); if (v != null) this.fc.value(v); return this; }
    public CmsSubRefEntry fc(String v) { this.fcPresent.value(v != null); if (v != null) this.fc.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, fcPresent, fc);
    }
}
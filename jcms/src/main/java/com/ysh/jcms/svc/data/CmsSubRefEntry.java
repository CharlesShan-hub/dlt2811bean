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

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, fcPresent, fc);
    }
}

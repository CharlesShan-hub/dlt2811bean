package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFunctionalConstraint;
import java.util.Arrays;
import java.util.List;

/**
 * SGRefFcEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint
 * }
 *
 * Used by GetEditSGValue Request.
 */
public class CmsSgRefFcEntry extends CmsType {

    public CmsObjectReference     reference;
    public CmsFunctionalConstraint fc;

    public CmsSgRefFcEntry() {
        this.reference = new CmsObjectReference();
        this.fc        = new CmsFunctionalConstraint();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, fc);
    }
}

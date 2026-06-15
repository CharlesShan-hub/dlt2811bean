package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFunctionalConstraint;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * DataRefEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
 * }
 *
 * Used by GetDataValues Request, GetDataDefinition Request.
 */
public class CmsDataRefEntry extends CmsType {

    public CmsObjectReference     reference;
    public CmsBoolean             fcPresent;
    public CmsFunctionalConstraint fc;            /* OPTIONAL */

    public CmsDataRefEntry() {
        this.reference = new CmsObjectReference();
        this.fcPresent = new CmsBoolean();
        this.fc        = new CmsFunctionalConstraint();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, fcPresent, fc);
    }
}

package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.fc.CmsFunctionalConstraint;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * DataRefValueEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT ObjectReference,
 *     fc            [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *     value         [2] IMPLICIT Data
 * }
 *
 * Used by SetDataValues Request.
 */
public class CmsDataRefValueEntry extends CmsType {

    public CmsObjectReference     reference;
    public CmsBoolean             fcPresent;
    public CmsFunctionalConstraint fc;            /* OPTIONAL */
    public CmsData                value;

    public CmsDataRefValueEntry() {
        this.reference = new CmsObjectReference();
        this.fcPresent = new CmsBoolean();
        this.fc        = new CmsFunctionalConstraint();
        this.value     = new CmsData();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, fcPresent, fc, value);
    }
}

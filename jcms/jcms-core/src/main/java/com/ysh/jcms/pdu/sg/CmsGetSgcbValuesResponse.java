package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerGetSGCBValuesResponsePDU;
import com.ysh.jcms.data.choice.CmsSgcbValueChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetSGCBValues-ResponsePDU ::= SEQUENCE {
 *     sgscb          [0] IMPLICIT SEQUENCE OF CHOICE {
 *         error       [0] IMPLICIT ServiceError,
 *         value       [1] IMPLICIT SGCB
 *     },
 *     moreFollows    [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.6.6
 * }
 * </pre>
 */
public class CmsGetSgcbValuesResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsSgcbValueChoice.class)
    public List<CmsSgcbValueChoice> sgscb; /* SEQUENCE OF SGCBValueChoice */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetSgcbValuesResponse() {
        super(new InnerGetSGCBValuesResponsePDU());
        this.sgscb = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetSgcbValuesResponse sgscb(List<CmsSgcbValueChoice> v) {
        this.sgscb = v;
        return this;
    }
    public CmsGetSgcbValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}

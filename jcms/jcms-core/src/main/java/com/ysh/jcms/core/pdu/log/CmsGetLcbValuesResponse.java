package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.data.InnerGetLCBValuesResponsePDU;
import com.ysh.jcms.core.data.choice.CmsLcbValueChoice;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetLCBValues-ResponsePDU ::= SEQUENCE {
 *     lcb             [0] IMPLICIT SEQUENCE OF CHOICE {
 *         error       [0] IMPLICIT ServiceError,
 *         value       [1] IMPLICIT LCB
 *     },
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.8.2
 * }
 * </pre>
 */
public class CmsGetLcbValuesResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsLcbValueChoice.class)
    public List<CmsLcbValueChoice> lcb; /* SEQUENCE OF LCBValue */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetLcbValuesResponse() {
        super(new InnerGetLCBValuesResponsePDU());
        this.lcb = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetLcbValuesResponse lcb(List<CmsLcbValueChoice> v) {
        this.lcb = v;
        return this;
    }
    public CmsGetLcbValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}

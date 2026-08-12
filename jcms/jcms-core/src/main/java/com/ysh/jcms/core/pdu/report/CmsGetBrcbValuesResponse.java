package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.data.InnerGetBRCBValuesResponsePDU;
import com.ysh.jcms.core.data.choice.CmsRcbValueChoice;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetBRCBValues-ResponsePDU ::= SEQUENCE {
 *     brcb            [0] IMPLICIT SEQUENCE OF CHOICE {
 *         error       [0] IMPLICIT ServiceError,
 *         value       [1] IMPLICIT BRCB
 *     },
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.7.2
 * }
 * </pre>
 */
public class CmsGetBrcbValuesResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsRcbValueChoice.class)
    public List<CmsRcbValueChoice> brcb; /* SEQUENCE OF RCBValueChoice */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetBrcbValuesResponse() {
        super(new InnerGetBRCBValuesResponsePDU());
        this.brcb = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetBrcbValuesResponse brcb(List<CmsRcbValueChoice> v) {
        this.brcb = v;
        return this;
    }
    public CmsGetBrcbValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}

package com.ysh.jcms.pdu.report;

import com.ysh.jcms.data.InnerGetURCBValuesResponsePDU;
import com.ysh.jcms.data.choice.CmsUrcbValueChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * GetURCBValues-ResponsePDU ::= SEQUENCE { urcb [0] IMPLICIT SEQUENCE OF CHOICE
 * { error [0] IMPLICIT ServiceError, value [1] IMPLICIT URCB }, moreFollows [1]
 * IMPLICIT Boolean DEFAULT 1 } — 8.7.4
 */
public class CmsGetUrcbValuesResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsUrcbValueChoice.class)
    public List<CmsUrcbValueChoice> urcb; /* SEQUENCE OF URCBValueChoice */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetUrcbValuesResponse() {
        super(new InnerGetURCBValuesResponsePDU());
        this.urcb = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetUrcbValuesResponse urcb(List<CmsUrcbValueChoice> v) {
        this.urcb = v;
        return this;
    }
    public CmsGetUrcbValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}

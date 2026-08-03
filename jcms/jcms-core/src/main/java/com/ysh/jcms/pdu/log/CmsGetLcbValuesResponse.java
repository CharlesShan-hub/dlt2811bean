package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerGetLCBValuesResponsePDU;
import com.ysh.jcms.data.choice.CmsLcbValueChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * GetLCBValues-ResponsePDU ::= SEQUENCE { lcb [0] IMPLICIT SEQUENCE OF
 * LCBValue, moreFollows [1] IMPLICIT Boolean DEFAULT 1 } — 8.8.2
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

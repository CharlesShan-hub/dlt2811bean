package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.InnerGetLogStatusValuesResponsePDU;
import com.ysh.jcms.data.choice.CmsLogStatusValueChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * GetLogStatusValues-ResponsePDU ::= SEQUENCE {
 *     log         [0] IMPLICIT SEQUENCE OF LogStatusValueChoice,
 *     moreFollows [1] IMPLICIT Boolean DEFAULT 1
 * } — 8.8.6
 */
public class CmsGetLogStatusValuesResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsLogStatusValueChoice.class)
    public List<CmsLogStatusValueChoice> log; /* SEQUENCE OF LogStatusValueChoice */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetLogStatusValuesResponse() {
        super(new InnerGetLogStatusValuesResponsePDU());
        this.log = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetLogStatusValuesResponse log(List<CmsLogStatusValueChoice> v) {
        this.log = v;
        return this;
    }
    public CmsGetLogStatusValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}

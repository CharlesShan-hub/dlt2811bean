package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerGetEditSGValueResponsePDU;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetEditSGValue-ResponsePDU ::= SEQUENCE {
 *     value           [0] IMPLICIT SEQUENCE OF Data,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.6.5
 * }
 * </pre>
 */
public class CmsGetEditSgValueResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsData.class)
    public List<CmsData> value; /* SEQUENCE OF Data */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetEditSgValueResponse() {
        super(new InnerGetEditSGValueResponsePDU());
        this.value = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetEditSgValueResponse value(List<CmsData> v) {
        this.value = v;
        return this;
    }
    public CmsGetEditSgValueResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }
}

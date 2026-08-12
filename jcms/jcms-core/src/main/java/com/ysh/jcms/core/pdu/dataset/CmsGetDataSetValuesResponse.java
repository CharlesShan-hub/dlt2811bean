package com.ysh.jcms.core.pdu.dataset;

import com.ysh.jcms.data.InnerGetDataSetValuesResponsePDU;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetDataSetValues-ResponsePDU ::= SEQUENCE {
 *     value               [0] IMPLICIT SEQUENCE OF Data,
 *     moreFollows         [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.5.1
 * }
 * </pre>
 */
public class CmsGetDataSetValuesResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsData.class)
    public List<CmsData> value; /* SEQUENCE OF Data */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetDataSetValuesResponse() {
        super(new InnerGetDataSetValuesResponsePDU());
        this.value = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetDataSetValuesResponse value(List<CmsData> v) {
        this.value = v;
        return this;
    }
    public CmsGetDataSetValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

}

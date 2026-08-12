package com.ysh.jcms.core.pdu.data;

import com.ysh.jcms.data.InnerGetDataValuesResponsePDU;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * GetDataValues-ResponsePDU ::= SEQUENCE {
 *     value            [0] IMPLICIT SEQUENCE OF Data,
 *     moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * } — 8.4.1
 * }
 * </pre>
 */
public class CmsGetDataValuesResponse extends CmsSequence {

    @CmsField(sequenceOf = true, elementType = CmsData.class)
    public List<CmsData> value; /* SEQUENCE OF Data */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetDataValuesResponse() {
        super(new InnerGetDataValuesResponsePDU());
        this.value = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetDataValuesResponse value(List<CmsData> v) {
        this.value = v;
        return this;
    }
    public CmsGetDataValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

}

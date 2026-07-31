package com.ysh.jcms.pdu.data;

import com.ysh.jcms.data.*;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.ArrayList;
import java.util.List;

/**
 * GetDataValues-ResponsePDU ::= SEQUENCE {
 *     value            [0] IMPLICIT SEQUENCE OF Data,
 *     moreFollows      [1] IMPLICIT Boolean DEFAULT 1
 * } — 8.4.1
 */
public class CmsGetDataValuesResponse extends CmsSequence {

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

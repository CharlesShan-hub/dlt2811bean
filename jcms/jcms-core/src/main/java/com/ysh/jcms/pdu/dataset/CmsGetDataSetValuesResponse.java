package com.ysh.jcms.pdu.dataset;

import com.ysh.jcms.data.*;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.ArrayList;
import java.util.List;

/**
 * GetDataSetValues-ResponsePDU ::= SEQUENCE {
 *     value               [0] IMPLICIT SEQUENCE OF Data,
 *     moreFollows         [1] IMPLICIT Boolean DEFAULT 1
 * } — 8.5.1
 */
public class CmsGetDataSetValuesResponse extends CmsSequence {

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

    @Override
    public void syncToInner() {
        InnerGetDataSetValuesResponsePDU inner = (InnerGetDataSetValuesResponsePDU) this.inner;
        inner.value.clear();
        for (CmsData elem : value) {
            elem.syncToInner();
            inner.value.add((InnerData) elem.inner);
        }
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        InnerGetDataSetValuesResponsePDU inner = (InnerGetDataSetValuesResponsePDU) this.inner;
        value = new ArrayList<>();
        for (InnerData innerElem : inner.value) {
            CmsData c = new CmsData();
            c.inner = innerElem;
            c.syncFromInner();
            value.add(c);
        }
    }
}

package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataValues-ResponsePDU ::= SEQUENCE { reqId Int16U, value [0] IMPLICIT
 * SEQUENCE OF Data, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE } — 8.4.1
 */
public class CmsGetDataValuesResponse extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsData> value; /* SEQUENCE OF Data */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetDataValuesResponse() {
        super(Codec.GET_DATA_VALUES_RESPONSE);
        this.reqId = new CmsReqId();
        this.value = new CmsArray<>(CmsData.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetDataValuesResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetDataValuesResponse value(CmsArray<CmsData> v) {
        this.value = v;
        return this;
    }
    public CmsGetDataValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    /** Convenience: get data values as List. */
    public List<CmsData> values() {
        List<CmsData> items = new ArrayList<>();
        for (int i = 0; i < value.count; i++) {
            items.add(value.items.get(i));
        }
        return items;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, value, moreFollows);
    }
}

package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataSetValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     value           [0] IMPLICIT SEQUENCE OF Data,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.5.1
 */
public class CmsGetDataSetValuesResponse extends CmsType {

    public CmsReqId              reqId;
    public CmsArray<CmsData>     value;        /* SEQUENCE OF Data */
    public CmsBoolean            moreFollows;  /* DEFAULT TRUE */

    public CmsGetDataSetValuesResponse() { super(Codec.GET_DATA_SET_VALUES_RESPONSE);
        this.reqId       = new CmsReqId();
        this.value       = new CmsArray<>(CmsData.class);
        this.moreFollows = new CmsBoolean();
    }
    
    public CmsGetDataSetValuesResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetDataSetValuesResponse value(CmsArray<CmsData> v) { this.value = v; return this; }
    public CmsGetDataSetValuesResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, value, moreFollows);
    }
}
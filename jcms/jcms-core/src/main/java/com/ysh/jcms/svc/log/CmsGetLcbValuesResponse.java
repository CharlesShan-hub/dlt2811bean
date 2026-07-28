package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetLCBValues-ResponsePDU ::= SEQUENCE { reqId Int16U, lcb [0] IMPLICIT
 * SEQUENCE OF LCBValueChoice, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE } —
 * 8.8.2
 */
public class CmsGetLcbValuesResponse extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsLcbValueChoice> lcb; /* SEQUENCE OF LCBValueChoice */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetLcbValuesResponse() {
        super(Codec.GET_LCB_VALUES_RESPONSE);
        this.reqId = new CmsReqId();
        this.lcb = new CmsArray<>(CmsLcbValueChoice.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetLcbValuesResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetLcbValuesResponse lcb(CmsArray<CmsLcbValueChoice> v) {
        this.lcb = v;
        return this;
    }
    public CmsGetLcbValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, lcb, moreFollows);
    }
}

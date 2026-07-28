package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetBRCBValues-ResponsePDU ::= SEQUENCE { reqId Int16U, brcb [0] IMPLICIT
 * SEQUENCE OF RCBValueChoice, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE } —
 * 8.7.2
 */
public class CmsGetBrcbValuesResponse extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsRcbValueChoice> brcb; /* SEQUENCE OF RCBValueChoice */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetBrcbValuesResponse() {
        super(Codec.GET_BRCB_VALUES_RESPONSE);
        this.reqId = new CmsReqId();
        this.brcb = new CmsArray<>(CmsRcbValueChoice.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetBrcbValuesResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetBrcbValuesResponse brcb(CmsArray<CmsRcbValueChoice> v) {
        this.brcb = v;
        return this;
    }
    public CmsGetBrcbValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, brcb, moreFollows);
    }
}

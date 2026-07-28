package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetMSVCBValues-ResponsePDU ::= SEQUENCE { reqId Int16U, msvcb [0] IMPLICIT
 * SEQUENCE OF MSVCBValueChoice, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE }
 * — 8.10.2
 */
public class CmsGetMsvcbValuesResponse extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsArray<CmsMsvcbValueChoice> msvcb; /* SEQUENCE OF MSVCBValueChoice */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetMsvcbValuesResponse() {
        super(Codec.GET_MSVCB_VALUES_RESPONSE);
        this.reqId = new CmsReqId();
        this.msvcb = new CmsArray<>(CmsMsvcbValueChoice.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetMsvcbValuesResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetMsvcbValuesResponse msvcb(CmsArray<CmsMsvcbValueChoice> v) {
        this.msvcb = v;
        return this;
    }
    public CmsGetMsvcbValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, msvcb, moreFollows);
    }
}

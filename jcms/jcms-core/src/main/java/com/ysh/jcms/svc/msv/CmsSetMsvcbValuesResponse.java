package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetMSVCBValues-ResponsePDU ::= SEQUENCE { reqId Int16U } — 8.10.3
 */
public class CmsSetMsvcbValuesResponse extends CmsType {

    public CmsReqId reqId;

    public CmsSetMsvcbValuesResponse() {
        super(Codec.SET_MSVCB_VALUES_RESPONSE);
        this.reqId = new CmsReqId();
    }

    public CmsSetMsvcbValuesResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }
}

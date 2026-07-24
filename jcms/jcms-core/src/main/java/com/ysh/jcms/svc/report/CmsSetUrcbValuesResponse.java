package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetURCBValues-ResponsePDU ::= SEQUENCE { reqId Int16U } — 8.7.5
 */
public class CmsSetUrcbValuesResponse extends CmsTypeOld {

    public CmsReqId reqId;

    public CmsSetUrcbValuesResponse() {
        super(Codec.SET_URCB_VALUES_RESPONSE);
        this.reqId = new CmsReqId();
    }

    public CmsSetUrcbValuesResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId);
    }
}

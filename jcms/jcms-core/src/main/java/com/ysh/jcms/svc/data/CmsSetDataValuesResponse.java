package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetDataValues-ResponsePDU ::= SEQUENCE { reqId Int16U } — 8.4.2
 *
 * Response has no payload besides reqId.
 */
public class CmsSetDataValuesResponse extends CmsTypeOld {

    public CmsReqId reqId;

    public CmsSetDataValuesResponse() {
        super(Codec.SET_DATA_VALUES_RESPONSE);
        this.reqId = new CmsReqId();
    }

    public CmsSetDataValuesResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId);
    }
}

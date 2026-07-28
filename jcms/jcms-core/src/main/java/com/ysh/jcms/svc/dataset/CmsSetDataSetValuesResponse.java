package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetDataSetValues-ResponsePDU ::= SEQUENCE { reqId Int16U } — 8.5.2
 *
 * Response has no payload besides reqId.
 */
public class CmsSetDataSetValuesResponse extends CmsTypeOld {

    public CmsReqId reqId;

    public CmsSetDataSetValuesResponse() {
        super(Codec.SET_DATA_SET_VALUES_RESPONSE);
        this.reqId = new CmsReqId();
    }

    public CmsSetDataSetValuesResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId);
    }
}

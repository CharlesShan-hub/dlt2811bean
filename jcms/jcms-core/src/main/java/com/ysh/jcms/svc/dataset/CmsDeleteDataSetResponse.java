package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * DeleteDataSet-ResponsePDU ::= SEQUENCE { reqId Int16U } — 8.5.4
 *
 * Response has no payload besides reqId.
 */
public class CmsDeleteDataSetResponse extends CmsTypeOld {

    public CmsReqId reqId;

    public CmsDeleteDataSetResponse() {
        super(Codec.DELETE_DATA_SET_RESPONSE);
        this.reqId = new CmsReqId();
    }

    public CmsDeleteDataSetResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId);
    }
}

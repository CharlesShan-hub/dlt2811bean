package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * ConfirmEditSGValues-ResponsePDU ::= SEQUENCE { reqId Int16U } — 8.6.4
 */
public class CmsConfirmEditSgValuesResponse extends CmsType {

    public CmsReqId reqId;

    public CmsConfirmEditSgValuesResponse() {
        super(Codec.CONFIRM_EDIT_SG_VALUES_RESPONSE);
        this.reqId = new CmsReqId();
    }

    public CmsConfirmEditSgValuesResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }
}

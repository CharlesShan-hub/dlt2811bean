package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetEditSGValue-ResponsePDU ::= SEQUENCE { reqId Int16U } — 8.6.3
 */
public class CmsSetEditSgValueResponse extends CmsTypeOld {

    public CmsReqId reqId;

    public CmsSetEditSgValueResponse() {
        super(Codec.SET_EDIT_SG_VALUE_RESPONSE);
        this.reqId = new CmsReqId();
    }

    public CmsSetEditSgValueResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId);
    }
}

package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SelectEditSG-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.6.2
 */
public class CmsSelectEditSgResponse extends CmsType {

    public CmsReqId reqId;

    public CmsSelectEditSgResponse() { super(Codec.SELECT_EDIT_SG_RESPONSE);
        this.reqId = new CmsReqId();
    }
    
    public CmsSelectEditSgResponse reqId(int v) { this.reqId.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }
}
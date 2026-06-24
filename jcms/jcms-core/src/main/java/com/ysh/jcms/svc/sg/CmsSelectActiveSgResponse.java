package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SelectActiveSG-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.6.1
 */
public class CmsSelectActiveSgResponse extends CmsType {

    public CmsReqId reqId;

    public CmsSelectActiveSgResponse() { super(Codec.SELECT_ACTIVE_SG_RESPONSE);
        this.reqId = new CmsReqId();
    }
    
    public CmsSelectActiveSgResponse reqId(int v) { this.reqId.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }
}
package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetGoCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.9.5
 */
public class CmsSetGoCbValuesResponse extends CmsType {

    public CmsReqId reqId;

    public CmsSetGoCbValuesResponse() { super(Codec.SET_GO_CB_VALUES_RESPONSE);
        this.reqId = new CmsReqId();
    }
    
    public CmsSetGoCbValuesResponse reqId(int v) { this.reqId.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }
}
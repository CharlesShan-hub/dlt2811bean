package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetLCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.8.3
 */
public class CmsSetLcbValuesResponse extends CmsType {

    public CmsReqId reqId;

    public CmsSetLcbValuesResponse() { super(Codec.SET_LCB_VALUES_RESPONSE);
        this.reqId = new CmsReqId();
    }
    
    public CmsSetLcbValuesResponse reqId(int v) { this.reqId.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }
}
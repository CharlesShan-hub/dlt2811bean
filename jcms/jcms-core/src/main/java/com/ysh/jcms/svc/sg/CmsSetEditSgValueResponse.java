package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetEditSGValue-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.6.3
 */
public class CmsSetEditSgValueResponse extends CmsType {

    public CmsReqId reqId;

    public CmsSetEditSgValueResponse() {
        this.reqId = new CmsReqId();
    }
    
    // -- chain setters --
    public CmsSetEditSgValueResponse reqId(int v) { this.reqId.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetEditSgValueResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetEditSgValueResponse(nativePtr, data); read(); }
}
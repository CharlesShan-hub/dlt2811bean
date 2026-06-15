package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
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

    public CmsSelectActiveSgResponse() {
        this.reqId = new CmsReqId();
    }
    
    // -- chain setters --
    public CmsSelectActiveSgResponse reqId(int v) { this.reqId.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSelectActiveSgResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSelectActiveSgResponse(nativePtr, data); read(); }
}
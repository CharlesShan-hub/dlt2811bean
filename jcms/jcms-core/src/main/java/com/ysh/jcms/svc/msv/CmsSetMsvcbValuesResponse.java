package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetMSVCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.10.3
 */
public class CmsSetMsvcbValuesResponse extends CmsType {

    public CmsReqId reqId;

    public CmsSetMsvcbValuesResponse() {
        this.reqId = new CmsReqId();
    }
    
    // -- chain setters --
    public CmsSetMsvcbValuesResponse reqId(int v) { this.reqId.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetMsvcbValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetMsvcbValuesResponse(nativePtr, data); read(); }
}
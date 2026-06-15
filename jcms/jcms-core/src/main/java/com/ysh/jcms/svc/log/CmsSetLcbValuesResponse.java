package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
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

    public CmsSetLcbValuesResponse() {
        this.reqId = new CmsReqId();
    }
    
    // -- chain setters --
    public CmsSetLcbValuesResponse reqId(int v) { this.reqId.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetLcbValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetLcbValuesResponse(nativePtr, data); read(); }
}
package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetBRCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.7.3
 */
public class CmsSetBrcbValuesResponse extends CmsType {

    public CmsReqId reqId;

    public CmsSetBrcbValuesResponse() {
        this.reqId = new CmsReqId();
    }
    
    public CmsSetBrcbValuesResponse reqId(int v) { this.reqId.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetBrcbValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetBrcbValuesResponse(nativePtr, data); read(); }
}
package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetURCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.7.5
 */
public class CmsSetUrcbValuesResponse extends CmsType {

    public CmsReqId reqId;

    public CmsSetUrcbValuesResponse() {
        this.reqId = new CmsReqId();
    }
    
    public CmsSetUrcbValuesResponse reqId(int v) { this.reqId.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetUrcbValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetUrcbValuesResponse(nativePtr, data); read(); }
}
package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
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

    public CmsSetGoCbValuesResponse() {
        this.reqId = new CmsReqId();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetGoCbValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetGoCbValuesResponse(nativePtr, data); read(); }
}

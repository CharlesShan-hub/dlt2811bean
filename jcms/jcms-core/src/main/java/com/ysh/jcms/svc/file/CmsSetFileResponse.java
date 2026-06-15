package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetFile-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.12.2
 */
public class CmsSetFileResponse extends CmsType {

    public CmsReqId reqId;

    public CmsSetFileResponse() {
        this.reqId = new CmsReqId();
    }
    
    public CmsSetFileResponse reqId(int v) { this.reqId.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetFileResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetFileResponse(nativePtr, data); read(); }
}
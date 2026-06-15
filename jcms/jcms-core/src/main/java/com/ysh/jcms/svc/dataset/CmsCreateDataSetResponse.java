package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * CreateDataSet-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.5.3
 *
 * Response has no payload besides reqId.
 */
public class CmsCreateDataSetResponse extends CmsType {

    public CmsReqId reqId;

    public CmsCreateDataSetResponse() {
        this.reqId = new CmsReqId();
    }
    
    // -- chain setters --
    public CmsCreateDataSetResponse reqId(int v) { this.reqId.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeCreateDataSetResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeCreateDataSetResponse(nativePtr, data); read(); }
}
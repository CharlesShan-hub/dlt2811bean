package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * DeleteDataSet-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.5.4
 *
 * Response has no payload besides reqId.
 */
public class CmsDeleteDataSetResponse extends CmsType {

    public CmsReqId reqId;

    public CmsDeleteDataSetResponse() {
        this.reqId = new CmsReqId();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeDeleteDataSetResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeDeleteDataSetResponse(nativePtr, data); read(); }
}

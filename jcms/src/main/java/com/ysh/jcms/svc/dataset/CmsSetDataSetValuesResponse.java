package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SetDataSetValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.5.2
 *
 * Response has no payload besides reqId.
 */
public class CmsSetDataSetValuesResponse extends CmsType {

    public CmsReqId reqId;

    public CmsSetDataSetValuesResponse() {
        this.reqId = new CmsReqId();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSetDataSetValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSetDataSetValuesResponse(nativePtr, data); read(); }
}

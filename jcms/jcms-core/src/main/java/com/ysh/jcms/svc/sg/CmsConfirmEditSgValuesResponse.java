package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * ConfirmEditSGValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.6.4
 */
public class CmsConfirmEditSgValuesResponse extends CmsType {

    public CmsReqId reqId;

    public CmsConfirmEditSgValuesResponse() {
        this.reqId = new CmsReqId();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeConfirmEditSgValuesResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeConfirmEditSgValuesResponse(nativePtr, data); read(); }
}

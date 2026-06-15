package com.ysh.jcms.svc.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Operate-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference
 * }  —  8.11.3
 */
public class CmsOperateResponse extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  reference;

    public CmsOperateResponse() {
        this.reqId     = new CmsReqId();
        this.reference = new CmsObjectReference();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeOperateResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeOperateResponse(nativePtr, data); read(); }
}

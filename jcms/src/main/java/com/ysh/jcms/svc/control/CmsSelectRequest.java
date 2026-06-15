package com.ysh.jcms.svc.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Select-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference
 * }  —  8.11.1
 */
public class CmsSelectRequest extends CmsType {

    public CmsReqId            reqId;
    public CmsObjectReference  reference;

    public CmsSelectRequest() {
        this.reqId     = new CmsReqId();
        this.reference = new CmsObjectReference();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSelectRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSelectRequest(nativePtr, data); read(); }
}

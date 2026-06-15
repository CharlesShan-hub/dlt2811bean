package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcMethodDefinition-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT SEQUENCE OF VisibleString
 * }  —  8.13.5
 */
public class CmsGetRpcMethodDefinitionRequest extends CmsType {

    public CmsReqId                       reqId;
    public CmsArray<CmsUint8Array>        reference;  /* SEQUENCE OF VisibleString */

    public CmsGetRpcMethodDefinitionRequest() {
        this.reqId     = new CmsReqId();
        this.reference = new CmsArray<>();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetRpcMethodDefinitionRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetRpcMethodDefinitionRequest(nativePtr, data); read(); }
}

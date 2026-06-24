package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
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

    public CmsGetRpcMethodDefinitionRequest() { super(Codec.GET_RPC_METHOD_DEFINITION_REQUEST);
        this.reqId     = new CmsReqId();
        this.reference = new CmsArray<>(CmsUint8Array.class);
    }
    
    public CmsGetRpcMethodDefinitionRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetRpcMethodDefinitionRequest reference(CmsArray<CmsUint8Array> v) { this.reference = v; return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference);
    }
}
package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcMethodDefinition-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT SEQUENCE OF RpcMethodDefChoice,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }  —  8.13.5
 */
public class CmsGetRpcMethodDefinitionResponse extends CmsType {

    public CmsReqId                                reqId;
    public CmsArray<CmsRpcMethodDefChoice>         reference;    /* SEQUENCE OF RpcMethodDefChoice */
    public CmsBoolean                              moreFollows;  /* DEFAULT TRUE */

    public CmsGetRpcMethodDefinitionResponse() {
        this.reqId       = new CmsReqId();
        this.reference   = new CmsArray<>();
        this.moreFollows = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, moreFollows);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetRpcMethodDefinitionResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetRpcMethodDefinitionResponse(nativePtr, data); read(); }
}

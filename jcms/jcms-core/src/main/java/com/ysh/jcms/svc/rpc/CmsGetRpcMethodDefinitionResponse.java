package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
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

    public CmsGetRpcMethodDefinitionResponse() { super(Codec.GET_RPC_METHOD_DEFINITION_RESPONSE);
        this.reqId       = new CmsReqId();
        this.reference   = new CmsArray<>(CmsRpcMethodDefChoice.class);
        this.moreFollows = new CmsBoolean();
    }
    
    public CmsGetRpcMethodDefinitionResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetRpcMethodDefinitionResponse reference(CmsArray<CmsRpcMethodDefChoice> v) { this.reference = v; return this; }
    public CmsGetRpcMethodDefinitionResponse moreFollows(boolean v) { this.moreFollows.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, moreFollows);
    }
}
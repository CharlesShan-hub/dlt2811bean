package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcInterfaceDefinition-ResponsePDU ::= SEQUENCE { reqId Int16U, method [0]
 * IMPLICIT SEQUENCE OF RpcMethodEntry, moreFollows [1] IMPLICIT BOOLEAN DEFAULT
 * TRUE } — 8.13.4
 */
public class CmsGetRpcInterfaceDefinitionResponse extends CmsType {

    public CmsReqId reqId;
    public CmsArray<CmsRpcMethodEntry> method; /* SEQUENCE OF RpcMethodEntry */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetRpcInterfaceDefinitionResponse() {
        super(Codec.GET_RPC_INTERFACE_DEFINITION_RESPONSE);
        this.reqId = new CmsReqId();
        this.method = new CmsArray<>(CmsRpcMethodEntry.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetRpcInterfaceDefinitionResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetRpcInterfaceDefinitionResponse method(CmsArray<CmsRpcMethodEntry> v) {
        this.method = v;
        return this;
    }
    public CmsGetRpcInterfaceDefinitionResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, method, moreFollows);
    }
}

package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsArray;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcInterfaceDirectory-ResponsePDU ::= SEQUENCE { reqId Int16U, reference
 * [0] IMPLICIT SEQUENCE OF VisibleString, moreFollows [1] IMPLICIT BOOLEAN
 * DEFAULT TRUE } — 8.13.2
 */
public class CmsGetRpcInterfaceDirectoryResponse extends CmsType {

    public CmsReqId reqId;
    public CmsArray<CmsUint8Array> reference; /* SEQUENCE OF VisibleString */
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetRpcInterfaceDirectoryResponse() {
        super(Codec.GET_RPC_INTERFACE_DIRECTORY_RESPONSE);
        this.reqId = new CmsReqId();
        this.reference = new CmsArray<>(CmsUint8Array.class);
        this.moreFollows = new CmsBoolean();
    }

    public CmsGetRpcInterfaceDirectoryResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetRpcInterfaceDirectoryResponse reference(CmsArray<CmsUint8Array> v) {
        this.reference = v;
        return this;
    }
    public CmsGetRpcInterfaceDirectoryResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, reference, moreFollows);
    }
}

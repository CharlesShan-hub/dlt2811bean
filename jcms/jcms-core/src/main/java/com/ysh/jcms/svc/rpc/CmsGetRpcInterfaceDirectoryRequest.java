package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcInterfaceDirectory-RequestPDU ::= SEQUENCE { reqId Int16U,
 * referenceAfter [0] IMPLICIT VisibleString OPTIONAL } — 8.13.2
 */
public class CmsGetRpcInterfaceDirectoryRequest extends CmsType {

    public CmsReqId reqId;
    public CmsBoolean refAfterPresent;
    public CmsUint8Array refAfter; /* VisibleString OPTIONAL */

    public CmsGetRpcInterfaceDirectoryRequest() {
        super(Codec.GET_RPC_INTERFACE_DIRECTORY_REQUEST);
        this.reqId = new CmsReqId();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter = new CmsUint8Array();
    }

    public CmsGetRpcInterfaceDirectoryRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetRpcInterfaceDirectoryRequest refAfterPresent(boolean v) {
        this.refAfterPresent.value(v);
        return this;
    }
    public CmsGetRpcInterfaceDirectoryRequest refAfter(byte[] v) {
        this.refAfterPresent.value(v != null && v.length > 0);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }
    public CmsGetRpcInterfaceDirectoryRequest refAfter(String v) {
        this.refAfterPresent.value(v != null);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, refAfterPresent, refAfter);
    }
}

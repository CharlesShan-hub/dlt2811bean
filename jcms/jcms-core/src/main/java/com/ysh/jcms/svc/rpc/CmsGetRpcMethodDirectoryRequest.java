package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcMethodDirectory-RequestPDU ::= SEQUENCE { reqId Int16U, interface [0]
 * IMPLICIT VisibleString OPTIONAL, referenceAfter [1] IMPLICIT VisibleString
 * OPTIONAL } — 8.13.3
 */
public class CmsGetRpcMethodDirectoryRequest extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsBoolean interfacePresent;
    public CmsUint8Array interfaceName; /* VisibleString OPTIONAL */
    public CmsBoolean refAfterPresent;
    public CmsUint8Array refAfter; /* VisibleString OPTIONAL */

    public CmsGetRpcMethodDirectoryRequest() {
        super(Codec.GET_RPC_METHOD_DIRECTORY_REQUEST);
        this.reqId = new CmsReqId();
        this.interfacePresent = new CmsBoolean();
        this.interfaceName = new CmsUint8Array();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter = new CmsUint8Array();
    }

    public CmsGetRpcMethodDirectoryRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetRpcMethodDirectoryRequest interfacePresent(boolean v) {
        this.interfacePresent.value(v);
        return this;
    }
    public CmsGetRpcMethodDirectoryRequest interfaceName(byte[] v) {
        this.interfaceName.value(v);
        return this;
    }
    public CmsGetRpcMethodDirectoryRequest interfaceName(String v) {
        this.interfaceName.value(v);
        return this;
    }
    public CmsGetRpcMethodDirectoryRequest refAfterPresent(boolean v) {
        this.refAfterPresent.value(v);
        return this;
    }
    public CmsGetRpcMethodDirectoryRequest refAfter(byte[] v) {
        this.refAfterPresent.value(v != null && v.length > 0);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }
    public CmsGetRpcMethodDirectoryRequest refAfter(String v) {
        this.refAfterPresent.value(v != null);
        if (v != null)
            this.refAfter.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, interfacePresent, interfaceName, refAfterPresent, refAfter);
    }
}

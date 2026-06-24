package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcInterfaceDefinition-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     interface       [0] IMPLICIT VisibleString,
 *     referenceAfter  [1] IMPLICIT VisibleString OPTIONAL
 * }  —  8.13.4
 */
public class CmsGetRpcInterfaceDefinitionRequest extends CmsType {

    public CmsReqId        reqId;
    public CmsUint8Array   interfaceName;    /* VisibleString */
    public CmsBoolean      refAfterPresent;
    public CmsUint8Array   refAfter;         /* VisibleString OPTIONAL */

    public CmsGetRpcInterfaceDefinitionRequest() { super(Codec.GET_RPC_INTERFACE_DEFINITION_REQUEST);
        this.reqId            = new CmsReqId();
        this.interfaceName    = new CmsUint8Array();
        this.refAfterPresent  = new CmsBoolean();
        this.refAfter         = new CmsUint8Array();
    }
    
    public CmsGetRpcInterfaceDefinitionRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetRpcInterfaceDefinitionRequest interfaceName(byte[] v) { this.interfaceName.value(v); return this; }
    public CmsGetRpcInterfaceDefinitionRequest interfaceName(String v) { this.interfaceName.value(v); return this; }
    public CmsGetRpcInterfaceDefinitionRequest refAfterPresent(boolean v) { this.refAfterPresent.value(v); return this; }
    public CmsGetRpcInterfaceDefinitionRequest refAfter(byte[] v) { this.refAfterPresent.value(v != null && v.length > 0); if (v != null) this.refAfter.value(v); return this; }
    public CmsGetRpcInterfaceDefinitionRequest refAfter(String v) { this.refAfterPresent.value(v != null); if (v != null) this.refAfter.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, interfaceName, refAfterPresent, refAfter);
    }
}
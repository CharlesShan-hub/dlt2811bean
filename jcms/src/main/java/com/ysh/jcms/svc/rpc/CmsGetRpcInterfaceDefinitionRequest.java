package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
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

    public CmsGetRpcInterfaceDefinitionRequest() {
        this.reqId            = new CmsReqId();
        this.interfaceName    = new CmsUint8Array();
        this.refAfterPresent  = new CmsBoolean();
        this.refAfter         = new CmsUint8Array();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, interfaceName, refAfterPresent, refAfter);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetRpcInterfaceDefinitionRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetRpcInterfaceDefinitionRequest(nativePtr, data); read(); }
}

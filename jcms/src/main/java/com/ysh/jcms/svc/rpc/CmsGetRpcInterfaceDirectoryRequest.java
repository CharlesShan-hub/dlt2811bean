package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcInterfaceDirectory-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     referenceAfter  [0] IMPLICIT VisibleString OPTIONAL
 * }  —  8.13.2
 */
public class CmsGetRpcInterfaceDirectoryRequest extends CmsType {

    public CmsReqId        reqId;
    public CmsBoolean      refAfterPresent;
    public CmsUint8Array   refAfter;       /* VisibleString OPTIONAL */

    public CmsGetRpcInterfaceDirectoryRequest() {
        this.reqId           = new CmsReqId();
        this.refAfterPresent = new CmsBoolean();
        this.refAfter        = new CmsUint8Array();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, refAfterPresent, refAfter);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetRpcInterfaceDirectoryRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetRpcInterfaceDirectoryRequest(nativePtr, data); read(); }
}

package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * RpcCall-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     method          [0] IMPLICIT VisibleString,
 *     req             [1] IMPLICIT RpcCallReqChoice
 * }  —  8.13.6
 */
public class CmsRpcCallRequest extends CmsType {

    public CmsReqId             reqId;
    public CmsUint8Array        method;       /* VisibleString */
    public CmsRpcCallReqChoice  req;

    public CmsRpcCallRequest() {
        this.reqId  = new CmsReqId();
        this.method = new CmsUint8Array();
        this.req    = new CmsRpcCallReqChoice();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, method, req);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeRpcCallRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeRpcCallRequest(nativePtr, data); read(); }
}

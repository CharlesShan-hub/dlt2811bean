package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * RpcCall-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     rspData         [0] IMPLICIT Data,
 *     nextCallID      [1] IMPLICIT OCTET STRING OPTIONAL
 * }  —  8.13.6
 */
public class CmsRpcCallResponse extends CmsType {

    public CmsReqId        reqId;
    public CmsData         rspData;
    public CmsBoolean      nextCallIdPresent;
    public CmsUint8Array   nextCallId;       /* OCTET STRING OPTIONAL */

    public CmsRpcCallResponse() {
        this.reqId            = new CmsReqId();
        this.rspData          = new CmsData();
        this.nextCallIdPresent = new CmsBoolean();
        this.nextCallId       = new CmsUint8Array();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, rspData, nextCallIdPresent, nextCallId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeRpcCallResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeRpcCallResponse(nativePtr, data); read(); }
}

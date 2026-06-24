package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
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

    public CmsRpcCallResponse() { super(Codec.RPC_CALL_RESPONSE);
        this.reqId            = new CmsReqId();
        this.rspData          = new CmsData();
        this.nextCallIdPresent = new CmsBoolean();
        this.nextCallId       = new CmsUint8Array();
    }
    
    public CmsRpcCallResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsRpcCallResponse rspData(CmsData v) { this.rspData = v; return this; }
    public CmsRpcCallResponse nextCallIdPresent(boolean v) { this.nextCallIdPresent.value(v); return this; }
    public CmsRpcCallResponse nextCallId(byte[] v) { this.nextCallIdPresent.value(v != null && v.length > 0); if (v != null) this.nextCallId.value(v); return this; }
    public CmsRpcCallResponse nextCallId(String v) { this.nextCallIdPresent.value(v != null); if (v != null) this.nextCallId.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, rspData, nextCallIdPresent, nextCallId);
    }
}
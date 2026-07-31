package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerRpcCallRequestPDU;
import com.ysh.jcms.data.choice.CmsRpcCallReqChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * RpcCall-RequestPDU ::= SEQUENCE {
 *     method  [0] IMPLICIT VisibleString,
 *     req     [1] IMPLICIT RpcCallReqChoice
 * } — 8.13.6
 */
public class CmsRpcCallRequest extends CmsSequence {

    @CmsField
    public CmsString method;

    @CmsField
    public CmsRpcCallReqChoice req;

    public CmsRpcCallRequest() { super(new InnerRpcCallRequestPDU()); }

    public CmsRpcCallRequest method(String v) { this.method.value(v); return this; }
    public CmsRpcCallRequest method(byte[] v) { return method(new String(v)); }
    public CmsRpcCallRequest req(CmsRpcCallReqChoice v) { this.req.value(v); return this; }
}

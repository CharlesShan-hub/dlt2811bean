package com.ysh.jcms.core.pdu.rpc;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerRpcCallRequestPDU;
import com.ysh.jcms.core.data.choice.CmsRpcCallReqChoice;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsString;

/**
 * <pre>
 * {@code
 * RpcCall-RequestPDU ::= SEQUENCE {
 *     method          [0] IMPLICIT VisibleString,
 *     req             [1] IMPLICIT CHOICE {
 *         reqData     [0] IMPLICIT Data,
 *         callID      [1] IMPLICIT OCTET STRING
 *     }
 * } — 8.13.6
 * }
 * </pre>
 */
public class CmsRpcCallRequest extends CmsSequence {

    @CmsField
    public CmsString method;

    @CmsField
    public CmsRpcCallReqChoice req;

    public CmsRpcCallRequest() {
        super(new InnerRpcCallRequestPDU());
    }

    public CmsRpcCallRequest method(String v) {
        this.method.value(v);
        return this;
    }
    public CmsRpcCallRequest method(byte[] v) {
        return method(new String(v, StandardCharsets.UTF_8));
    }
    public CmsRpcCallRequest req(CmsRpcCallReqChoice v) {
        this.req.value(v);
        return this;
    }
}

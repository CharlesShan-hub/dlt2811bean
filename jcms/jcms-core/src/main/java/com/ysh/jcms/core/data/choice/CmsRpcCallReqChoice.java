package com.ysh.jcms.core.data.choice;

import com.ysh.jcms.data.DefaultInnerOctetString;
import com.ysh.jcms.data.InnerRpcCallRequestPDUReq;
import com.ysh.jcms.data.V;
import com.ysh.jcms.data.core.CmsChoice;

/**
 * <pre>
 * {@code
 * RpcCallReqChoice (inline within RpcCall-RequestPDU req) ::= CHOICE {
 *     reqData [0] IMPLICIT Data,
 *     callID  [1] IMPLICIT OCTET STRING
 * } — 8.13.6
 * }
 * </pre>
 */
public class CmsRpcCallReqChoice extends CmsChoice {

    public static final int REQ_DATA = 0;
    public static final int CALL_ID = 1;

    @Choice(index = 0, name = "reqData", sync = Sync.WRAPPER)
    public CmsData altReqData;
    @Choice(index = 1, name = "callID", sync = Sync.INNER)
    public DefaultInnerOctetString altCallId;

    public CmsRpcCallReqChoice() {
        super(new InnerRpcCallRequestPDUReq());
    }

    public CmsRpcCallReqChoice choice(int v) {
        super.choice(v);
        return this;
    }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsRpcCallReqChoice altReqData(CmsData v) {
        choice(REQ_DATA);
        this.altReqData.value(v);
        return this;
    }
    public CmsRpcCallReqChoice altCallId(byte[] v) {
        choice(CALL_ID);
        V.setVal(this.altCallId._v, v);
        return this;
    }

    /**
     * Copy choice selection and value from another CmsRpcCallReqChoice (fluent).
     */
    public CmsRpcCallReqChoice value(CmsRpcCallReqChoice v) {
        switch (v.choice()) {
            case REQ_DATA :
                return altReqData(v.altReqData);
            case CALL_ID :
                choice(CALL_ID);
                V.setVal(this.altCallId._v, V.getVal(v.altCallId._v));
                return this;
            default :
                throw new IllegalArgumentException("Unknown RpcCallReqChoice choice: " + v.choice());
        }
    }
}

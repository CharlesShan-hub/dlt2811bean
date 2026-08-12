package com.ysh.jcms.core.pdu.rpc;

import com.ysh.jcms.data.InnerRpcCallResponsePDU;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsOctetString;

/**
 * <pre>
 * {@code
 * RpcCall-ResponsePDU ::= SEQUENCE {
 *     rspData         [0] IMPLICIT Data,
 *     nextCallID      [1] IMPLICIT OCTET STRING OPTIONAL
 * } — 8.13.6
 * }
 * </pre>
 */
public class CmsRpcCallResponse extends CmsSequence {

    @CmsField
    public CmsData rspData;

    @CmsField(optional = true)
    public CmsOctetString nextCallID;

    public CmsRpcCallResponse() {
        super(new InnerRpcCallResponsePDU());
    }

    public CmsRpcCallResponse rspData(CmsData v) {
        this.rspData.value(v);
        return this;
    }
    public CmsRpcCallResponse nextCallID(byte[] v) {
        if (v != null) {
            this.nextCallID.value(v);
            setPresent("nextCallID", true);
        } else {
            setPresent("nextCallID", false);
        }
        return this;
    }
}

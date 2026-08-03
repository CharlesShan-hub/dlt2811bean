package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.InnerRpcCallResponsePDU;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsOctetString;

/**
 * RpcCall-ResponsePDU ::= SEQUENCE { rspData [0] IMPLICIT Data, nextCallID [1]
 * IMPLICIT OCTET STRING OPTIONAL } — 8.13.6
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

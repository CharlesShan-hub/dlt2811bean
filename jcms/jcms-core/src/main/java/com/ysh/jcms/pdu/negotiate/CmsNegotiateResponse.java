package com.ysh.jcms.pdu.negotiate;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerAssociateNegotiateResponsePDU;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * AssociateNegotiate-ResponsePDU ::= SEQUENCE { apduSize [0] IMPLICIT Int16U,
 * asduSize [1] IMPLICIT Int32U, protocolVersion [2] IMPLICIT Int32U,
 * modelVersion [3] IMPLICIT VisibleString } — 8.15
 */
public class CmsNegotiateResponse extends CmsSequence {

    @CmsField
    public CmsInt16U apduSize;

    @CmsField
    public CmsInt32U asduSize;

    @CmsField
    public CmsInt32U protocolVersion;

    @CmsField
    public CmsString modelVersion;

    public CmsNegotiateResponse() {
        super(new InnerAssociateNegotiateResponsePDU());
    }

    public CmsNegotiateResponse apduSize(int v) {
        this.apduSize.value(v);
        return this;
    }
    public CmsNegotiateResponse asduSize(long v) {
        this.asduSize.value(v);
        return this;
    }
    public CmsNegotiateResponse protocolVersion(long v) {
        this.protocolVersion.value(v);
        return this;
    }
    public CmsNegotiateResponse modelVersion(String v) {
        this.modelVersion.value(v);
        return this;
    }
    public CmsNegotiateResponse modelVersion(byte[] v) {
        return modelVersion(new String(v, StandardCharsets.UTF_8));
    }
}

package com.ysh.jcms.core.pdu.negotiate;

import com.ysh.jcms.data.InnerAssociateNegotiateRequestPDU;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsInt16U;
import com.ysh.jcms.core.data.scalar.CmsInt32U;

/**
 * <pre>
 * {@code
 * AssociateNegotiate-RequestPDU ::= SEQUENCE {
 *     apduSize        [0] IMPLICIT INT16U,
 *     asduSize        [1] IMPLICIT INT32U,
 *     protocolVersion [2] IMPLICIT INT32U
 * } — 8.15
 * }
 * </pre>
 */
public class CmsNegotiateRequest extends CmsSequence {

    @CmsField
    public CmsInt16U apduSize;

    @CmsField
    public CmsInt32U asduSize;

    @CmsField
    public CmsInt32U protocolVersion;

    public CmsNegotiateRequest() {
        super(new InnerAssociateNegotiateRequestPDU());
    }

    public CmsNegotiateRequest apduSize(int v) {
        this.apduSize.value(v);
        return this;
    }
    public CmsNegotiateRequest asduSize(long v) {
        this.asduSize.value(v);
        return this;
    }
    public CmsNegotiateRequest protocolVersion(long v) {
        this.protocolVersion.value(v);
        return this;
    }
}

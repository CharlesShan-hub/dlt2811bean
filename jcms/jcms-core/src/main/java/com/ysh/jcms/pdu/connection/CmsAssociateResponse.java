package com.ysh.jcms.pdu.connection;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerAssociateResponsePDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.connection.CmsAuthenticationParameter;
import com.ysh.jcms.data.scalar.CmsAssociationId;

/**
 * Associate-ResponsePDU ::= SEQUENCE {
 *     associationId                  [0] IMPLICIT OCTET STRING (SIZE (0..64)),
 *     serviceError                   [1] IMPLICIT ServiceError,
 *     authenticationParameter        [2] IMPLICIT SEQUENCE { ... } OPTIONAL
 * } — 8.2.1
 */
public class CmsAssociateResponse extends CmsSequence {

    @CmsField public CmsAssociationId associationId;
    @CmsField public CmsServiceError serviceError;
    @CmsField(optional = true) public CmsAuthenticationParameter authenticationParameter;

    public CmsAssociateResponse() {
        super(new InnerAssociateResponsePDU());
    }

    public CmsAssociateResponse associationId(byte[] v) { this.associationId.value(v); return this; }
    public CmsAssociateResponse serviceError(int v) { this.serviceError.value(v); return this; }
    public CmsAssociateResponse authenticationParameter(CmsAuthenticationParameter v) {
        this.authenticationParameter = v;
        bindWrapper("authenticationParameter", v);
        setPresent("authenticationParameter", true);
        return this;
    }
}

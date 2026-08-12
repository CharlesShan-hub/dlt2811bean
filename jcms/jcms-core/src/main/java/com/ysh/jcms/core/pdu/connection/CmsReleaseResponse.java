package com.ysh.jcms.core.pdu.connection;

import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.InnerReleaseResponsePDU;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.scalar.CmsAssociationId;

/**
 * <pre>
 * {@code
 * Release-ResponsePDU ::= SEQUENCE {
 *     associationId [0] IMPLICIT OCTET STRING (SIZE (0..64)),
 *     serviceError  [1] IMPLICIT ServiceError
 * } — 8.2.2
 * }
 * </pre>
 */
public class CmsReleaseResponse extends CmsSequence {

    @CmsField
    public CmsAssociationId associationId;
    @CmsField
    public CmsServiceError serviceError;

    public CmsReleaseResponse() {
        super(new InnerReleaseResponsePDU());
    }

    public CmsReleaseResponse associationId(byte[] v) {
        this.associationId.value(v);
        return this;
    }
    public CmsReleaseResponse serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }
}

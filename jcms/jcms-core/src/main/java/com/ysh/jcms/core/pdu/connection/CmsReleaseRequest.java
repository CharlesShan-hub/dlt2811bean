package com.ysh.jcms.core.pdu.connection;

import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.data.InnerReleaseRequestPDU;
import com.ysh.jcms.core.data.scalar.CmsAssociationId;

/**
 * <pre>
 * {@code
 * Release-RequestPDU ::= SEQUENCE {
 *     associationId [0] IMPLICIT OCTET STRING (SIZE (0..64))
 * } — 8.2.2
 * }
 * </pre>
 */
public class CmsReleaseRequest extends CmsSequence {

    @CmsField
    public CmsAssociationId associationId;

    public CmsReleaseRequest() {
        super(new InnerReleaseRequestPDU());
    }

    public CmsReleaseRequest associationId(byte[] v) {
        this.associationId.value(v);
        return this;
    }
}

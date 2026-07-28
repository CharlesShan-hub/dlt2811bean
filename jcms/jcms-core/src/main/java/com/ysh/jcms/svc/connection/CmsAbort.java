package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.InnerAbortRequestPDU;
import com.ysh.jcms.svc.other.CmsAssociationId;

/**
 * Abort-RequestPDU ::= SEQUENCE {
 *     associationId    [0] IMPLICIT OCTET STRING (SIZE (0..64)),
 *     reason           [1] IMPLICIT INTEGER { ... } (0..5)
 * } — 8.2.3
 */
public class CmsAbort extends CmsSequence {

    @CmsField public CmsAssociationId associationId;
    @CmsField public CmsAbortReason reason;

    public CmsAbort() {
        super(new InnerAbortRequestPDU());
    }

    public CmsAbort associationId(byte[] v) { this.associationId.value(v); return this; }
    public CmsAbort reason(int v) { this.reason.value(v); return this; }
}

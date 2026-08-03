package com.ysh.jcms.pdu.connection;

import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerAbortRequestPDU;
import com.ysh.jcms.data.enumerate.CmsAbortReason;
import com.ysh.jcms.data.scalar.CmsAssociationId;

/**
 * <pre>
 * {@code
 * Abort-RequestPDU ::= SEQUENCE {
 *     associationId [0] IMPLICIT OCTET STRING (SIZE (0..64)),
 *     reason        [1] IMPLICIT INTEGER {
 *         other                  (0),
 *         unrecognized-service   (1),
 *         invalid-reqID          (2),
 *         invalid-argument       (3),
 *         invalid-result         (4),
 *         max-serv-outstanding-exceeded (5)
 *     } (0..5)
 * } — 8.2.3
 * }
 * </pre>
 */
public class CmsAbort extends CmsSequence {

    @CmsField
    public CmsAssociationId associationId;
    @CmsField
    public CmsAbortReason reason;

    public CmsAbort() {
        super(new InnerAbortRequestPDU());
    }

    public CmsAbort associationId(byte[] v) {
        this.associationId.value(v);
        return this;
    }
    public CmsAbort reason(int v) {
        this.reason.value(v);
        return this;
    }
}

package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerAbortRequestPDU;
import com.ysh.jcms.svc.other.CmsAssociationId;

/**
 * Abort-RequestPDU ::= SEQUENCE {
 *     associationId    [0] IMPLICIT OCTET STRING (SIZE (0..64)),
 *     reason           [1] IMPLICIT INTEGER {
 *         other                  (0),
 *         unrecognized-service   (1),
 *         invalid-reqID          (2),
 *         invalid-argument       (3),
 *         invalid-result         (4),
 *         max-serv-outstanding-exceeded (5)
 *     } (0..5)
 * } — 8.2.3
 *
 * NOTE: Abort is a one-way message with no Response or Error PDU.
 * reqId is handled at the protocol level, not part of the ASN.1 definition.
 */
public class CmsAbort extends CmsType {

    public CmsAssociationId assocId;
    /** Reason code. Use constants from {@link CmsAbortReason}. */
    public int reason;

    public CmsAbort() {
        super(new InnerAbortRequestPDU());
        this.assocId = new CmsAssociationId();
    }

    public CmsAbort assocId(byte[] v) {
        this.assocId.value(v);
        return this;
    }
    public CmsAbort assocId(String v) {
        this.assocId.value(v);
        return this;
    }
    public CmsAbort reason(int v) {
        this.reason = v;
        return this;
    }

    @Override
    public void syncToInner() {
        InnerAbortRequestPDU inner = (InnerAbortRequestPDU) this.inner;
        inner.associationId = assocId.value();
        inner.reason = reason;
    }

    @Override
    public void syncFromInner() {
        InnerAbortRequestPDU inner = (InnerAbortRequestPDU) this.inner;
        this.assocId.value(inner.associationId);
        this.reason = inner.reason;
    }
}

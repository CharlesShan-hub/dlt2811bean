package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsAssociationId;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Abort-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     associationId   [0] IMPLICIT OCTET STRING (SIZE(0..64)),
 *     reason          [1] IMPLICIT INTEGER {
 *         other                        (0),
 *         unrecognized-service         (1),
 *         invalid-reqID                (2),
 *         invalid-argument             (3),
 *         invalid-result               (4),
 *         max-serv-outstanding-exceeded (5)
 *     } (0..5)
 * }  —  8.2.3
 *
 * NOTE: Abort is a one-way message with no Response or Error PDU.
 */
public class CmsAbort extends CmsType {

    public CmsReqId         reqId;
    public CmsAssociationId assocId;
    public CmsAbortReason   reason;

    public CmsAbort() {
        this.reqId   = new CmsReqId();
        this.assocId = new CmsAssociationId();
        this.reason  = new CmsAbortReason();
    }
    
    public CmsAbort reqId(int v) { this.reqId.value(v); return this; }
    public CmsAbort assocId(byte[] v) { this.assocId.value(v); return this; }
    public CmsAbort assocId(String v) { this.assocId.value(v); return this; }
    public CmsAbort reason(int v) { this.reason.value(v); return this; }
    
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, assocId, reason);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeAbort(nativePtr); }

    @Override public void decode(byte[] data) { write(); NativeBridge.decodeAbort(nativePtr, data); read(); }
}

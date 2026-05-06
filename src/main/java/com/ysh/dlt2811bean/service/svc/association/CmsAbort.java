package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AbortReason;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 02 — Abort (abort association).
 *
 * Corresponds to Table 21 in GB/T 45906.3-2025: Abort service parameters.
 *
 * Service code: 0x02 (2)
 * Service interface: Abort
 * Category: Association service
 *
 * The Abort service is used to abort an association. It can be initiated
 * by either the client (REQUEST) or the server (REQUEST_PLUS, indication).
 *
 * This class supports two message types:
 * <ul>
 *   <li>REQUEST - Client-initiated abort (with ReqID)</li>
 *   <li>REQUEST_PLUS - Server-initiated abort indication (ReqID=0)</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request / Indication ASDU:
 * ┌─────────────────────────────────────────────────┐
 * │ ReqID (2B)                                      │
 * │ reason          AbortReason                     │
 * └─────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * Abort-RequestPDU:: = SEQUENCE {
 *   associationId    [0] IMPLICIT OCTET STRING (SIZE (0..64)),
 *   reason           [1] IMPLICIT INTEGER {
 *     other                     (0),
 *     unrecognized-service      (1),
 *     invalid-reqID             (2),
 *     invalid-argument          (3),
 *     invalid-result            (4),
 *     max-serv-outstanding-exceed (5)
 *ed   } (0..5)
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsAbort extends CmsAsdu<CmsAbort> {

    // ==================== Fields based on Table 21 ====================

    @CmsField(only = {"REQUEST", "INDICATION"})
    public AbortReason reason = new AbortReason();

    // ==================== Constructor ====================

    public CmsAbort() {
    }

    public CmsAbort(MessageType messageType) {
        super(messageType);
    }

    public CmsAbort(boolean isResp, boolean isErr) {
        this(getIndicationMessageType(isResp, isErr));
    }

    // ==================== Convenience Setters ====================

    public CmsAbort reason(int reasonCode) {
        this.reason.set(reasonCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.ABORT;
    }
}
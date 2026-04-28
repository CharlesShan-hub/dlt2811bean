package com.ysh.dlt2811bean.service.svc.association;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AbortReason;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;

/**
 * CMS Service Code 02 — Abort (abort association).
 *
 * <p>Corresponds to Table 21 in GB/T 45906.3-2025: Abort service parameters.
 *
 * <p>Service code: 0x02 (2)
 * Service interface: Abort
 * Category: Association service
 *
 * <p>The Abort service is used to abort an association. It can be initiated
 * by either the client (REQUEST) or the server (REQUEST_PLUS, indication).
 *
 * <p>This class supports two message types:
 * <ul>
 *   <li>REQUEST - Client-initiated abort (with ReqID)</li>
 *   <li>REQUEST_PLUS - Server-initiated abort indication (ReqID=0)</li>
 * </ul>
 *
 * <p>ASDU field layout (PER encoded, in order):
 * <pre>
 * Request / Indication ASDU:
 * ┌─────────────────────────────────────────────────┐
 * │ ReqID (2B)                                      │
 * │ reason          AbortReason                     │
 * └─────────────────────────────────────────────────┘
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsAbort extends CmsAsdu<CmsAbort> {

    public CmsAbort(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("reason");
        }else{
            throw new IllegalArgumentException("Abort does not support " + messageType);
        }

    }

    public CmsAbort(boolean isResp, boolean isErr) {
        super(fromFlags(isResp, isErr));
    }

    private static MessageType fromFlags(boolean resp, boolean err) {
        if (!resp && !err) return MessageType.REQUEST;
        throw new IllegalArgumentException("Abort does not support err=true");
    }

    // ==================== Fields based on Table 21 ====================

    public AbortReason reason = new AbortReason();

    // ==================== Convenience Setters ====================

    public CmsAbort reason(int reasonCode) {
        this.reason.set(reasonCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceCode getServiceCode() {
        return ServiceCode.ABORT;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsAbort copy() {
        CmsAbort copy = new CmsAbort(messageType());
        copy.reqId.set(reqId.get());
        copy.reason = this.reason.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsAbort read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsAbort) new CmsAbort(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsAbort abort) {
        abort.encode(pos);
    }
}
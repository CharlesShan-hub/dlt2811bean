package com.ysh.dlt2811bean.service.svc.setting;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x3B — ConfirmEditSGValues (confirm edit setting group values).
 *
 * Corresponds to Table 43 in GB/T 45906.3-2025: ConfirmEditSGValues service parameters.
 *
 * Service code: 0x57 (87)
 * Service interface: ConfirmEditSGValues
 * Category: Setting group service
 *
 * The ConfirmEditSGValues service is used to confirm the edited setting group values
 * to take effect.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Confirm edit setting group values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response (no additional data)</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2 bytes)                                             │
 * │ ASDUHeader (variable)                                       │
 * ├─────────────────────────────────────────────────────────────┤
 * │ sgcbReference          [0] IMPLICIT ObjectReference         │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2 bytes)                                             │
 * │ ASDUHeader (variable)                                       │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2 bytes)                                             │
 * │ ASDUHeader (variable)                                       │
 * ├─────────────────────────────────────────────────────────────┤
 * │ serviceError           ServiceError                         │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * ConfirmEditSGValues-RequestPDU::= SEQUENCE {
 *   sgcbReference          [0] IMPLICIT ObjectReference
 * }
 *
 * ConfirmEditSGValues-ResponsePDU::= NULL
 *
 * ConfirmEditSGValues-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsConfirmEditSGValues extends CmsAsdu<CmsConfirmEditSGValues> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsConfirmEditSGValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("ConfirmEditSGValues does not support " + messageType);
        }
    }

    public CmsConfirmEditSGValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.CONFIRM_EDIT_SG_VALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsConfirmEditSGValues copy() {
        CmsConfirmEditSGValues copy = new CmsConfirmEditSGValues(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsConfirmEditSGValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsConfirmEditSGValues) new CmsConfirmEditSGValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsConfirmEditSGValues confirmEditSGValues) {
        confirmEditSGValues.encode(pos);
    }

}

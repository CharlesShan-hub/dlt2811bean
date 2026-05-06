package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
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

    // ==================== Fields based on Table 43 ====================

    // --- Request parameters ---
    public CmsObjectReference sgcbReference = new CmsObjectReference();

    // --- Response- parameters ---
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsConfirmEditSGValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("sgcbReference");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            // no additional fields
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("ConfirmEditSGValues does not support " + messageType);
        }
    }

    public CmsConfirmEditSGValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsConfirmEditSGValues sgcbReference(String ref) {
        this.sgcbReference.set(ref);
        return this;
    }

    public CmsConfirmEditSGValues serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.CONFIRM_EDIT_SG_VALUES;
    }
}

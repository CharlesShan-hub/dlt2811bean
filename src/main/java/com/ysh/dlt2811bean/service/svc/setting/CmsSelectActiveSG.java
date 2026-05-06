package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt8U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x38 — SelectActiveSG (select active setting group).
 *
 * Corresponds to Table 40 in GB/T 45906.3-2025: SelectActiveSG service parameters.
 *
 * Service code: 0x54 (84)
 * Service interface: SelectActiveSG
 * Category: Setting group service
 *
 * The SelectActiveSG service is used to select the setting group to be activated.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Select active setting group request</li>
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
 * │ settingGroupNumber     [1] IMPLICIT INT8U                   │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2 bytes)                                             │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2 bytes)                                             │
 * │ serviceError           ServiceError                         │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * SelectActiveSG-RequestPDU::= SEQUENCE {
 *   sgcbReference     [0] IMPLICIT ObjectReference,
 *   settingGroupNumber [1] IMPLICIT INT8U
 * }
 *
 * SelectActiveSG-ResponsePDU::= NULL
 * SelectActiveSG-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSelectActiveSG extends CmsAsdu<CmsSelectActiveSG> {

    // ==================== Fields based on Table 40 ====================

    // --- Request parameters ---
    public CmsObjectReference sgcbReference = new CmsObjectReference();
    public CmsInt8U settingGroupNumber = new CmsInt8U(1);

    // --- Response- parameters ---
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsSelectActiveSG(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("sgcbReference");
            registerField("settingGroupNumber");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            // no additional fields
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("SelectActiveSG does not support " + messageType);
        }
    }

    public CmsSelectActiveSG(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsSelectActiveSG sgcbReference(String ref) {
        this.sgcbReference.set(ref);
        return this;
    }

    public CmsSelectActiveSG settingGroupNumber(int num) {
        this.settingGroupNumber.set(num);
        return this;
    }

    public CmsSelectActiveSG serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SELECT_ACTIVE_SG;
    }
}

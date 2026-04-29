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

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsSelectActiveSG(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("SelectActiveSG does not support " + messageType);
        }
    }

    public CmsSelectActiveSG(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SELECT_ACTIVE_SG;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsSelectActiveSG copy() {
        CmsSelectActiveSG copy = new CmsSelectActiveSG(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsSelectActiveSG read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsSelectActiveSG) new CmsSelectActiveSG(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsSelectActiveSG selectActiveSG) {
        selectActiveSG.encode(pos);
    }

}

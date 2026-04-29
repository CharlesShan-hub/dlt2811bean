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
 * CMS Service Code 0x39 — SelectEditSG (select edit setting group).
 *
 * Corresponds to Table 41 in GB/T 45906.3-2025: SelectEditSG service parameters.
 *
 * Service code: 0x55 (85)
 * Service interface: SelectEditSG
 * Category: Setting group service
 *
 * The SelectEditSG service is used to select the setting group to be edited.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Select edit setting group request</li>
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
 * SelectEditSG-RequestPDU::= SEQUENCE {
 *   sgcbReference         [0] IMPLICIT ObjectReference,
 *   settingGroupNumber    [1] IMPLICIT INT8U
 * }
 *
 * SelectEditSG-ResponsePDU::= NULL
 *
 * SelectEditSG-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSelectEditSG extends CmsAsdu<CmsSelectEditSG> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsSelectEditSG(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("SelectEditSG does not support " + messageType);
        }
    }

    public CmsSelectEditSG(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SELECT_EDIT_SG;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsSelectEditSG copy() {
        CmsSelectEditSG copy = new CmsSelectEditSG(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsSelectEditSG read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsSelectEditSG) new CmsSelectEditSG(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsSelectEditSG selectEditSG) {
        selectEditSG.encode(pos);
    }

}

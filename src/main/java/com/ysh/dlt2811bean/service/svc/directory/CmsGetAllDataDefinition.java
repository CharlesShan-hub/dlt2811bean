package com.ysh.dlt2811bean.service.svc.directory;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x54 — GetAllDataDefinition (read all data definition).
 *
 * Corresponds to Table 29 in GB/T 45906.3-2025: GetAllDataDefinition service parameters.
 *
 * Service code: 0x9B (155)
 * Service interface: GetAllDataDefinition
 * Category: Data access service
 *
 * The GetAllDataDefinition service is used to retrieve the definitions of all data objects
 * under a specified logical device or logical node. The fc parameter is used to
 * filter specific functional constraint attributes.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get all data definition request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with data definitions</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference                  ObjectName / ObjectReference     │
 * │ fc                         FunctionalConstraint (OPTIONAL)  │
 * │ referenceAfter             ObjectReference (OPTIONAL)       │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ data                       SEQUENCE OF SEQUENCE {           │
 * │   reference                SubReference                     │
 * │   cdcType                 VisibleString (OPTIONAL)          │
 * │   definition              DataDefinition                    │
 * │ }                                                           │
 * │ moreFollows                BOOLEAN DEFAULT TRUE             │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ serviceError               ServiceError                     │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetAllDataDefinition-RequestPDU::= SEQUENCE {
 *   reference          [0] IMPLICIT CHOICE {
 *     ldName            [0] IMPLICIT ObjectName,
 *     lnReference       [1] IMPLICIT ObjectReference
 *   },
 *   fc                 [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *   referenceAfter     [2] IMPLICIT ObjectReference OPTIONAL
 * }
 *
 * GetAllDataDefinition-ResponsePDU::= SEQUENCE {
 *   data               [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference         [0] IMPLICIT SubReference,
 *     cdcType           [1] IMPLICIT VisibleString OPTIONAL,
 *     definition        [2] IMPLICIT DataDefinition
 *   },
 *   moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetAllDataDefinition-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetAllDataDefinition extends CmsAsdu<CmsGetAllDataDefinition> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetAllDataDefinition(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetAllDataDefinition does not support " + messageType);
        }
    }

    public CmsGetAllDataDefinition(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_ALL_DATA_DEFINITION;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetAllDataDefinition copy() {
        CmsGetAllDataDefinition copy = new CmsGetAllDataDefinition(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetAllDataDefinition read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetAllDataDefinition) new CmsGetAllDataDefinition(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetAllDataDefinition getAllDataDefinition) {
        getAllDataDefinition.encode(pos);
    }

}

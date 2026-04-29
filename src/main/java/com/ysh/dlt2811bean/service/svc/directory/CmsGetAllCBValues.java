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
 * CMS Service Code 0x55 — GetAllCBValues (read all control block values).
 *
 * Corresponds to Table 30 in GB/T 45906.3-2025: GetAllCBValues service parameters.
 *
 * Service code: 0x9C (156)
 * Service interface: GetAllCBValues
 * Category: Data access service
 *
 * The GetAllCBValues service is used to retrieve the values of all control blocks
 * under a specified logical device or logical node. The acsiClass parameter
 * specifies the type of control blocks to retrieve (e.g., buffered report,
 * unbuffered report, setting group, etc.).
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get all control block values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with control block values</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference                  ObjectName / ObjectReference     │
 * │ acsiClass                  ACSIClass                        │
 * │ referenceAfter             ObjectReference (OPTIONAL)       │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ cbValue[0..n]              SEQUENCE OF SEQUENCE {           │
 * │     reference              SubReference                     │
 * │     value                  CHOICE {                         │
 * │         brcb               BRCB                             │
 * │         urcb               URCB                             │
 * │         lcb                LCB                              │
 * │         sgb                SGCB                             │
 * │         gocb               GoCB                             │
 * │         msvcb              MSVCB                            │
 * │     }                                                       │
 * │ }                                                           │
 * │ moreFollows                BOOLEAN (DEFAULT TRUE)           │
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
 * GetAllCBValues-RequestPDU::= SEQUENCE {
 *   reference          [0] IMPLICIT CHOICE {
 *     ldName           [0] IMPLICIT ObjectName,
 *     lnReference      [1] IMPLICIT ObjectReference
 *   },
 *   acsiClass          [1] IMPLICIT ACSIClass,
 *   referenceAfter     [2] IMPLICIT ObjectReference OPTIONAL
 * }
 *
 * GetAllCBValues-ResponsePDU::= SEQUENCE {
 *   cbValue            [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference        [0] IMPLICIT SubReference
 *     value            [1] IMPLICIT CHOICE {
 *       brcb           [0] IMPLICIT BRCB,
 *       urcb           [1] IMPLICIT URCB,
 *       lcb            [2] IMPLICIT LCB,
 *       sgb            [3] IMPLICIT SGCB,
 *       gocb           [4] IMPLICIT GoCB,
 *       msvcb          [5] IMPLICIT MSVCB
 *     }
 *   },
 *   moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetAllCBValues-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetAllCBValues extends CmsAsdu<CmsGetAllCBValues> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetAllCBValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetAllCBValues does not support " + messageType);
        }
    }

    public CmsGetAllCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_ALL_CB_VALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetAllCBValues copy() {
        CmsGetAllCBValues copy = new CmsGetAllCBValues(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetAllCBValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetAllCBValues) new CmsGetAllCBValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetAllCBValues getAllCBValues) {
        getAllCBValues.encode(pos);
    }

}

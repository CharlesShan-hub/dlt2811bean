package com.ysh.dlt2811bean.service.svc.goose;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x67 — SetGoCBValues (set GOOSE control block values).
 *
 * Corresponds to Table 61 in GB/T 45906.3-2025: SetGoCBValues service parameters.
 *
 * Service code: 0x67 (103)
 * Service interface: SetGoCBValues
 * Category: GOOSE control block service
 *
 * The SetGoCBValues service is used to modify the configuration parameters
 * of one or more GOOSE Control Blocks (GoCB). The service allows changing
 * attributes such as the enable state, GOOSE ID, and dataset reference.
 *
 * This class supports two message types:
 * <ul>
 *   <li>REQUEST - Set GOOSE control block values request</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ gocb[1..n] (SEQUENCE OF SEQUENCE)                            │
 * │   ├─ reference            ObjectReference                    │
 * │   ├─ goEna      [0..1]    BOOLEAN                            │
 * │   ├─ goID       [0..1]    VisibleString129                   │
 * │   └─ datSet     [0..1]    ObjectReference                    │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │                                                              │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ result[1..n] (SEQUENCE OF SEQUENCE)                          │
 * │   ├─ error                ServiceError                       │
 * │   ├─ goEna                ServiceError OPTIONAL              │
 * │   ├─ goID                 ServiceError OPTIONAL              │
 * │   └─ datSet               ServiceError OPTIONAL              │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * Note: The positive response (Response+) for SetGoCBValues is NULL (no data).
 * Error information is returned via the Response- message containing the
 * result sequence.
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * SetGoCBValues-RequestPDU:: = SEQUENCE {
 *   gocb        [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference  [0] IMPLICIT ObjectReference,
 *     goEna      [1] IMPLICIT BOOLEAN OPTIONAL,
 *     goID       [2] IMPLICIT VisibleString129 OPTIONAL,
 *     datSet     [3] IMPLICIT ObjectReference OPTIONAL
 *   }
 * }
 *
 * SetGoCBValues-ResponsePDU:: = NULL
 *
 * SetGoCBValues-ErrorPDU:: = SEQUENCE {
 *   result      [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     error      [0] IMPLICIT ServiceError OPTIONAL,
 *     goEna      [1] IMPLICIT ServiceError OPTIONAL,
 *     goID       [2] IMPLICIT ServiceError OPTIONAL,
 *     datSet     [3] IMPLICIT ServiceError OPTIONAL
 *   }
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSetGoCBValues extends CmsAsdu<CmsSetGoCBValues> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsSetGoCBValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("SetGoCBValues does not support " + messageType);
        }
    }

    public CmsSetGoCBValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_GOCBVALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsSetGoCBValues copy() {
        CmsSetGoCBValues copy = new CmsSetGoCBValues(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsSetGoCBValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsSetGoCBValues) new CmsSetGoCBValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsSetGoCBValues setGoCBValues) {
        setGoCBValues.encode(pos);
    }

}

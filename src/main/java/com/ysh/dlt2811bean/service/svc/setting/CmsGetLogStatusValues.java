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
 * CMS Service Code 0x63 — GetLogStatusValues (get log status values).
 *
 * Corresponds to Table 56 in DL/T 2811—2024 / GB/T 45906.3-2025: GetLogStatusValues service parameters.
 *
 * Service code: 0x63 (99)
 * Service interface: GetLogStatusValues
 * Category: Logging service
 *
 * The GetLogStatusValues service is used to retrieve the status values of
 * log control blocks. The response contains a list of log entries, where
 * each entry can either indicate an error or provide specific status values
 * including old and new entry timestamps and IDs.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get log status values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with log status data and continuation flag</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ logReference[0..n]          SEQUENCE OF ObjectReference     │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ log[0..n]                   SEQUENCE OF CHOICE {            │
 * │   [0] error                 ServiceError                    │
 * │   [1] value                 SEQUENCE {                      │
 * │     oldEntrTm               EntryTime                       │
 * │     newEntrTm               EntryTime                       │
 * │     oldEntr                 EntryID                         │
 * │     newEntr                 EntryID                         │
 * │   }                                                         │
 * │ }                                                           │
 * │ moreFollows                 BOOLEAN DEFAULT TRUE            │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ serviceError                ServiceError                    │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetLogStatusValues-RequestPDU:: = SEQUENCE {
 *   logReference    [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 *
 * GetLogStatusValues-ResponsePDU:: = SEQUENCE {
 *   log             [0] IMPLICIT SEQUENCE OF CHOICE {
 *     error          [0] IMPLICIT ServiceError,
 *     value          [1] IMPLICIT SEQUENCE {
 *       oldEntrTm     [0] IMPLICIT EntryTime,
 *       newEntrTm     [1] IMPLICIT EntryTime,
 *       oldEntr       [2] IMPLICIT EntryID,
 *       newEntr       [3] IMPLICIT EntryID
 *     }
 *   },
 *   moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetLogStatusValues-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetLogStatusValues extends CmsAsdu<CmsGetLogStatusValues> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetLogStatusValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetLogStatusValues does not support " + messageType);
        }
    }

    public CmsGetLogStatusValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_LOG_STATUS_VALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetLogStatusValues copy() {
        CmsGetLogStatusValues copy = new CmsGetLogStatusValues(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetLogStatusValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetLogStatusValues) new CmsGetLogStatusValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetLogStatusValues getLogStatusValues) {
        getLogStatusValues.encode(pos);
    }

}

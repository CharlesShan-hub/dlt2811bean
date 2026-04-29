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
 * CMS Service Code 0x62 — QueryLogAfter (query log after specified entry).
 *
 * Corresponds to Table 55 in GB/T 45906.3-2025: QueryLogAfter service parameters.
 *
 * Service code: 0x62 (98)
 * Service interface: QueryLogAfter
 * Category: Logging service
 *
 * The QueryLogAfter service is used to query log entries that come after a
 * specified log entry identifier, optionally within a given time range starting
 * from startTime. It retrieves a sequence of log entries following the specified
 * entry point.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Query log entries after specified entry request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with log entries and continuation flag</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ logReference                [0] IMPLICIT ObjectReference     │
 * │ startTime                   [1] IMPLICIT EntryTime OPTIONAL  │
 * │ entry                       [2] IMPLICIT EntryID             │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ logEntry                    [0] IMPLICIT SEQUENCE OF LogEntry│
 * │ moreFollows                 [1] IMPLICIT BOOLEAN DEFAULT TRUE│
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ serviceError                ServiceError                     │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * QueryLogAfter-RequestPDU:: = SEQUENCE {
 *   logReference                [0] IMPLICIT ObjectReference,
 *   startTime                   [1] IMPLICIT EntryTime OPTIONAL,
 *   entry                       [2] IMPLICIT EntryID
 * }
 *
 * QueryLogAfter-ResponsePDU:: = SEQUENCE {
 *   logEntry                    [0] IMPLICIT SEQUENCE OF LogEntry,
 *   moreFollows                 [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * QueryLogAfter-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsQueryLogAfter extends CmsAsdu<CmsQueryLogAfter> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsQueryLogAfter(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("QueryLogAfter does not support " + messageType);
        }
    }

    public CmsQueryLogAfter(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.QUERY_LOG_AFTER;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsQueryLogAfter copy() {
        CmsQueryLogAfter copy = new CmsQueryLogAfter(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsQueryLogAfter read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsQueryLogAfter) new CmsQueryLogAfter(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsQueryLogAfter queryLogAfter) {
        queryLogAfter.encode(pos);
    }

}

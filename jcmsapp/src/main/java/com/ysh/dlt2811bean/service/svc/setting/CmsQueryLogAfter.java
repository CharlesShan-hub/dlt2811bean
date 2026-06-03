package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsBinaryTime;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsEntryID;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsLogEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import static com.ysh.dlt2811bean.service.protocol.enums.MessageType.*;
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

    // ==================== Fields based on Table 55 ====================

    @CmsField(only = {REQUEST})
    public CmsObjectReference logReference = new CmsObjectReference();

    @CmsField(optional = true, only = {REQUEST})
    public CmsBinaryTime startTime = new CmsBinaryTime();

    @CmsField(only = {REQUEST})
    public CmsEntryID entry = new CmsEntryID();

    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsArray<CmsLogEntry> logEntry = new CmsArray<>(CmsLogEntry::new);
    
    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {RESPONSE_NEGATIVE})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsQueryLogAfter() {
        super(ServiceName.QUERY_LOG_AFTER);
    }

    public CmsQueryLogAfter(MessageType messageType) {
        super(ServiceName.QUERY_LOG_AFTER, messageType);
    }

    public CmsQueryLogAfter(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsQueryLogAfter logReference(String ref) {
        this.logReference.set(ref);
        return this;
    }

    public CmsQueryLogAfter startTime(long msOfDay, int daysSince1984) {
        this.startTime.msOfDay(msOfDay).daysSince1984(daysSince1984);
        return this;
    }

    public CmsQueryLogAfter entry(byte[] entryId) {
        this.entry.set(entryId);
        return this;
    }

    public CmsQueryLogAfter serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }
}

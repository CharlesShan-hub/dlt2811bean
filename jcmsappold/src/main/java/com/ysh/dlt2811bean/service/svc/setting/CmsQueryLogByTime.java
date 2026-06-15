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
 * CMS Service Code 0x61 — QueryLogByTime (query log by time).
 *
 * Corresponds to Table 54 in GB/T 45906.3-2025: QueryLogByTime service parameters.
 *
 * Service code: 0x61 (97)
 * Service interface: QueryLogByTime
 * Category: Logging service
 *
 * The QueryLogByTime service is used to query log entries based on a specified
 * time range.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Query log entries by time request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with log entries and continuation flag</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * Time Range Behavior (per standard):
 * <ul>
 *   <li>If startTime is not specified (optional), query starts from the first log entry.</li>
 *   <li>If stopTime is not specified (optional), query continues to the last log entry.</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ logReference                [0] IMPLICIT ObjectReference    │
 * │ startTime                   [1] IMPLICIT EntryTime OPTIONAL │
 * │ stopTime                    [2] IMPLICIT EntryTime OPTIONAL │
 * │ entryAfter                  [3] IMPLICIT EntryID OPTIONAL   │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ logEntry                   [0] IMPLICIT SEQUENCE OF LogEntry│
 * │ moreFollows                [1] IMPLICIT BOOLEAN DEFAULT TRUE│
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
 * QueryLogByTime-RequestPDU:: = SEQUENCE {
 *   logReference                [0] IMPLICIT ObjectReference,
 *   startTime                   [1] IMPLICIT EntryTime OPTIONAL,
 *   stopTime                    [2] IMPLICIT EntryTime OPTIONAL,
 *   entryAfter                  [3] IMPLICIT EntryID OPTIONAL
 * }
 *
 * QueryLogByTime-ResponsePDU:: = SEQUENCE {
 *   logEntry                    [0] IMPLICIT SEQUENCE OF LogEntry,
 *   moreFollows                 [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * QueryLogByTime-ErrorPDU:: = ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsQueryLogByTime extends CmsAsdu<CmsQueryLogByTime> {

    // ==================== Fields based on Table 54 ====================

    @CmsField(only = {REQUEST})
    public CmsObjectReference logReference = new CmsObjectReference();

    @CmsField(optional = true, only = {REQUEST})
    public CmsBinaryTime startTime = new CmsBinaryTime();

    @CmsField(optional = true, only = {REQUEST})
    public CmsBinaryTime stopTime = new CmsBinaryTime();

    @CmsField(optional = true, only = {REQUEST})
    public CmsEntryID entryAfter = new CmsEntryID();

    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsArray<CmsLogEntry> logEntry = new CmsArray<>(CmsLogEntry::new);
    
    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {RESPONSE_NEGATIVE})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsQueryLogByTime() {
        super(ServiceName.QUERY_LOG_BY_TIME);
    }

    public CmsQueryLogByTime(MessageType messageType) {
        super(ServiceName.QUERY_LOG_BY_TIME, messageType);
    }

    public CmsQueryLogByTime(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsQueryLogByTime logReference(String ref) {
        this.logReference.set(ref);
        return this;
    }

    public CmsQueryLogByTime startTime(long msOfDay, int daysSince1984) {
        this.startTime.msOfDay(msOfDay).daysSince1984(daysSince1984);
        return this;
    }

    public CmsQueryLogByTime stopTime(long msOfDay, int daysSince1984) {
        this.stopTime.msOfDay(msOfDay).daysSince1984(daysSince1984);
        return this;
    }

    public CmsQueryLogByTime entryAfter(byte[] entryId) {
        this.entryAfter.set(entryId);
        return this;
    }

    public CmsQueryLogByTime serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }
}

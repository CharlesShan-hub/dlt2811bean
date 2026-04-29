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
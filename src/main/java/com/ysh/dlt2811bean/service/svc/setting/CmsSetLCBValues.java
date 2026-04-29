/**
 * CMS Service Code 0x60 — SetLCBValues (set log control block values).
 *
 * Corresponds to Table 53 in GB/T 45906.3-2025: SetLCBValues service parameters.
 *
 * Service code: 0x60 (96)
 * Service interface: SetLCBValues
 * Category: Logging service
 *
 * The SetLCBValues service is used to modify one or more attributes within the
 * log control block (LCB).
 *
 * This class supports two message types:
 * <ul>
 *   <li>REQUEST - Set LCB values request</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with detailed results</li>
 * </ul>
 * Note: Positive response (Response+) contains no additional data.
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ lcb[0..n]              SEQUENCE OF SEQUENCE {                │
 * │   reference         [0] IMPLICIT ObjectReference             │
 * │   logEna            [1] IMPLICIT BOOLEAN OPTIONAL,           │
 * │   datSet            [2] IMPLICIT ObjectReference OPTIONAL,   │
 * │   trgOps            [3] IMPLICIT TriggerConditions OPTIONAL, │
 * │   intgPd            [4] IMPLICIT INT32U OPTIONAL,            │
 * │   logRef            [5] IMPLICIT ObjectReference OPTIONAL,   │
 * │   optFlds           [6] IMPLICIT LCBOptFlds OPTIONAL,        │
 * │   bufTm             [7] IMPLICIT INT32U OPTIONAL             │
 * │ }                                                            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Error Response ASDU (Response+):
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ result[0..n]               SEQUENCE OF SEQUENCE {            │
 * │   error                  [0] IMPLICIT ServiceError           │
 * │   logEna                 [1] IMPLICIT ServiceError OPTIONAL, │
 * │   datSet                 [2] IMPLICIT ServiceError OPTIONAL, │
 * │   trgOps                 [3] IMPLICIT ServiceError OPTIONAL, │
 * │   intgPd                 [4] IMPLICIT ServiceError OPTIONAL, │
 * │   logRef                 [5] IMPLICIT ServiceError OPTIONAL, │
 * │   optFlds                [6] IMPLICIT ServiceError OPTIONAL, │
 * │   bufTm                  [7] IMPLICIT ServiceError OPTIONAL  │
 * │ }                                                            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * SetLCBValues-RequestPDU:: = SEQUENCE {
 *   lcb    [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference [0] IMPLICIT ObjectReference,
 *     logEna    [1] IMPLICIT BOOLEAN OPTIONAL,
 *     datSet    [2] IMPLICIT ObjectReference OPTIONAL,
 *     trgOps    [3] IMPLICIT TriggerConditions OPTIONAL,
 *     intgPd    [4] IMPLICIT INT32U OPTIONAL,
 *     logRef    [5] IMPLICIT ObjectReference OPTIONAL,
 *     optFlds   [6] IMPLICIT LCBOptFlds OPTIONAL,
 *     bufTm     [7] IMPLICIT INT32U OPTIONAL
 *   }
 * }
 *
 * SetLCBValues-ResponsePDU:: = NULL
 *
 * SetLCBValues-ErrorPDU:: = SEQUENCE {
 *   result [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     error   [0] IMPLICIT ServiceError,
 *     logEna  [1] IMPLICIT ServiceError OPTIONAL,
 *     datSet  [2] IMPLICIT ServiceError OPTIONAL,
 *     trgOps  [3] IMPLICIT ServiceError OPTIONAL,
 *     intgPd  [4] IMPLICIT ServiceError OPTIONAL,
 *     logRef  [5] IMPLICIT ServiceError OPTIONAL,
 *     optFlds [6] IMPLICIT ServiceError OPTIONAL,
 *     bufTm   [7] IMPLICIT ServiceError OPTIONAL
 *   }
 * }
 * </pre>
 */
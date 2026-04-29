/**
 * CMS Service Code 0x5E — SetURCBValues (set unbuffered report control block values).
 *
 * Corresponds to Table 50 in GB/T 45906.3-2025: SetURCBValues service parameters.
 *
 * Service code: 0x5E (94)
 * Service interface: SetURCBValues
 * Category: Reporting service
 *
 * The SetURCBValues service is used to modify one or more attributes within the
 * unbuffered report control block (URCB).
 *
 * This class supports two message types:
 * <ul>
 *   <li>REQUEST - Set URCB values request</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with detailed results</li>
 * </ul>
 * Note: Positive response (Response+) contains no additional data.
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌───────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                    │
 * │ urcb[0..n]            SEQUENCE OF SEQUENCE {                  │
 * │   reference           [0] IMPLICIT ObjectReference            │
 * │   rptID               [1] IMPLICIT VisibleString129 OPTIONAL, │
 * │   rptEna              [2] IMPLICIT BOOLEAN OPTIONAL,          │
 * │   datSet              [3] IMPLICIT ObjectReference OPTIONAL,  │
 * │   optFlds             [5] IMPLICIT RCBOptFlds OPTIONAL,       │
 * │   bufTm               [6] IMPLICIT INT32U OPTIONAL,           │
 * │   trgOps              [8] IMPLICIT TriggerConditions OPTIONAL,│
 * │   intgPd              [9] IMPLICIT INT32U OPTIONAL,           │
 * │   gi                  [10] IMPLICIT BOOLEAN OPTIONAL,         │
 * │   resv                [13] IMPLICIT BOOLEAN OPTIONAL          │
 * │ }                                                             │
 * └───────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌───────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                    │
 * │ result[0..n]               SEQUENCE OF SEQUENCE {             │
 * │   error               [0] IMPLICIT ServiceError OPTIONAL,     │
 * │   rptID               [1] IMPLICIT ServiceError OPTIONAL,     │
 * │   rptEna              [2] IMPLICIT ServiceError OPTIONAL,     │
 * │   datSet              [3] IMPLICIT ServiceError OPTIONAL,     │
 * │   optFlds             [5] IMPLICIT ServiceError OPTIONAL,     │
 * │   bufTm               [6] IMPLICIT ServiceError OPTIONAL,     │
 * │   trgOps              [8] IMPLICIT ServiceError OPTIONAL,     │
 * │   intgPd              [9] IMPLICIT ServiceError OPTIONAL,     │
 * │   gi                  [10] IMPLICIT ServiceError OPTIONAL,    │
 * │   resv                [13] IMPLICIT ServiceError OPTIONAL     │
 * │ }                                                             │
 * └───────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * SetURCBValues-RequestPDU:: = SEQUENCE {
 *   urcb                          [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference                  ] IMPLI[0CIT ObjectReference,
 *     rptID                      [1] IMPLICIT VisibleString129 OPTIONAL,
 *     rptEna                     [2] IMPLICIT BOOLEAN OPTIONAL,
 *     datSet                     [3] IMPLICIT ObjectReference OPTIONAL,
 *     optFlds                    [5] IMPLICIT RCBOptFlds OPTIONAL,
 *     bufTm                      [6] IMPLICIT INT32U OPTIONAL,
 *     trgOps                     [8] IMPLICIT TriggerConditions OPTIONAL,
 *     intgPd                     [9] IMPLICIT INT32U OPTIONAL,
 *     gi                         [10] IMPLICIT BOOLEAN OPTIONAL,
 *     resv                       [13] IMPLICIT BOOLEAN OPTIONAL
 *   }
 * }
 *
 * SetURCBValues-ResponsePDU:: = NULL
 *
 * SetURCBValues-ErrorPDU:: = SEQUENCE {
 *   result                        [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     error                      [0] IMPLICIT ServiceError OPTIONAL,
 *     rptID                      [1] IMPLICIT ServiceError OPTIONAL,
 *     rptEna                     [2] IMPLICIT ServiceError OPTIONAL,
 *     datSet                     [3] IMPLICIT ServiceError OPTIONAL,
 *     optFlds                    [5] IMPLICIT ServiceError OPTIONAL,
 *     bufTm                      [6] IMPLICIT ServiceError OPTIONAL,
 *     trgOps                     [8] IMPLICIT ServiceError OPTIONAL,
 *     intgPd                     [9] IMPLICIT ServiceError OPTIONAL,
 *     gi                         [10] IMPLICIT ServiceError OPTIONAL,
 *     resv                       [13] IMPLICIT ServiceError OPTIONAL
 *   }
 * }
 * </pre>
 */
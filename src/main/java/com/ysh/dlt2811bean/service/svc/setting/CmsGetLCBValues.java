/**
 * CMS Service Code 0x5F — GetLCBValues (get log control block values).
 *
 * Corresponds to Table 52 in GB/T 45906.3-2025: GetLCBValues service parameters.
 *
 * Service code: 0x5F (95)
 * Service interface: GetLCBValues
 * Category: Logging service
 *
 * The GetLCBValues service is used to retrieve all attributes of the log
 * control block (LCB).
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get LCB values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with either error or LCB data</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference[0..n]             SEQUENCE OF ObjectReference     │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ lcb[0..n]                   SEQUENCE OF CHOICE {            │
 * │   error                     [0] IMPLICIT ServiceError       │
 * │   value                     [1] IMPLICIT LCB                │
 * │ }                                                           │
 * │ moreFollows                 BOOLEAN (DEFAULT TRUE)          │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ serviceError                ServiceError                    │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * LCB:: = SEQUENCE {
 *   logEna                       [1] IMPLICIT BOOLEAN,
 *   datSet                       [2] IMPLICIT ObjectReference,
 *   trgOps                       [3] IMPLICIT TriggerConditions,
 *   intgPd                       [4] IMPLICIT INT32U,
 *   logRef                       [5] IMPLICIT ObjectReference,
 *   optFlds                      [6] IMPLICIT LCBOptFlds OPTIONAL,
 *   bufTm                        [7] IMPLICIT INT32U OPTIONAL
 * }
 *
 * GetLCBValues-RequestPDU:: = SEQUENCE {
 *   reference                    [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 *
 * GetLCBValues-ResponsePDU:: = SEQUENCE {
 *   lcb                          [0] IMPLICIT SEQUENCE OF CHOICE {
 *     error                      [0] IMPLICIT ServiceError,
 *     value                      [1] IMPLICIT LCB
 *   },
 *   moreFollows                  [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetLCBValues-ErrorPDU:: = ServiceError
 * </pre>
 */
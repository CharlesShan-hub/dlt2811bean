/**
 * CMS Service Code 0x5D — GetURCBValues (get unbuffered report control block values).
 *
 * Corresponds to Table 49 in GB/T 45906.3-2025: GetURCBValues service parameters.
 *
 * Service code: 0x5D (93)
 * Service interface: GetURCBValues
 * Category: Reporting service
 *
 * The GetURCBValues service is used to retrieve all attributes of the unbuffered
 * report control block (URCB).
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get URCB values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with either error or URCB data</li>
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
 * │ ReqID (2B)                                                  │
 * │ urcb[0..n]                  SEQUENCE OF CHOICE {            │
 * │   error                     [0] IMPLICIT ServiceError       │
 * │   value                     [1] IMPLICIT URCB               │
 * │ }                                                           │
 * │ moreFollows                [1] IMPLICIT BOOLEAN DEFAULT TRUE│
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
 * URCB:: = SEQUENCE {
 *   rptID                        [1] IMPLICIT VisibleString129,
 *   rptEna                       [2] IMPLICIT BOOLEAN,
 *   datSet                       [3] IMPLICIT ObjectReference,
 *   confRev                      [4] IMPLICIT INT32U,
 *   optFlds                      [5] IMPLICIT RCBOptFlds,
 *   bufTm                        [6] IMPLICIT INT32U,
 *   sqNum                        [7] IMPLICIT INT8U,
 *   trgOps                       [8] IMPLICIT TriggerConditions,
 *   intgPd                       [9] IMPLICIT INT32U,
 *   gi                           [10] IMPLICIT BOOLEAN,
 *   resv                         [14] IMPLICIT BOOLEAN,
 *   owner                        [15] IMPLICIT OCTET STRING (SIZE (0..64)) OPTIONAL
 * }
 *
 * GetURCBValues-RequestPDU:: = SEQUENCE {
 *   reference                    [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 *
 * GetURCBValues-ResponsePDU:: = SEQUENCE {
 *   urcb                         [0] IMPLICIT SEQUENCE OF CHOICE {
 *     error                      [0] IMPLICIT ServiceError,
 *     value                      [1] IMPLICIT URCB
 *   },
 *   moreFollows                  [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetURCBValues-ErrorPDU:: = ServiceError
 * </pre>
 */
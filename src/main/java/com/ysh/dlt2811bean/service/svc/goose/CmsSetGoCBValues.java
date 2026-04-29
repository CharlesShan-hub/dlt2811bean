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
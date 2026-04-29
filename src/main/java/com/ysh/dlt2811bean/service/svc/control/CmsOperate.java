/**
 * CMS Service Code 0x47 — Operate (operate service).
 *
 * Corresponds to Table 67 in GB/T 45906.3-2025: Operate service parameters.
 *
 * Service code: 0x47 (71)
 * Service interface: Operate
 * Category: Control service
 *
 * The Operate service is used to execute a control command on a previously
 * selected object. This follows the Select or SelectWithValue service in
 * a two-step control process. It sends the final command to change the
 * state of the device (e.g., opening/closing a breaker).
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client operate request with control value</li>
 *   <li>RESPONSE_POSITIVE - Server positive response confirming execution</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference                   ObjectReference                  │
 * │ ctlVal                      Data (depends on model)          │
 * │ origin                      Originator                       │
 * │ ctlNum                      INT8U                            │
 * │ t                           TimeStamp                        │
 * │ test                        BOOLEAN                          │
 * │ check                       Check                            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference                   ObjectReference                  │
 * │ ctlVal                      Data (depends on model)          │
 * │ origin                      Originator                       │
 * │ ctlNum                      INT8U                            │
 * │ t                           TimeStamp                        │
 * │ test                        BOOLEAN                          │
 * │ check                       Check                            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference                   ObjectReference                  │
 * │ ctlVal                      Data (depends on model)          │
 * │ origin                      Originator                       │
 * │ ctlNum                      INT8U                            │
 * │ t                           TimeStamp                        │
 * │ test                        BOOLEAN                          │
 * │ check                       Check                            │
 * │ addCause                    AddCause                         │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * Operate-RequestPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   origin       [2] IMPLICIT Originator,
 *   ctlNum       [3] IMPLICIT INT8U,
 *   t            [4] IMPLICIT TimeStamp,
 *   test         [5] IMPLICIT BOOLEAN,
 *   check        [6] IMPLICIT Check
 * }
 *
 * Operate-ResponsePDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   origin       [2] IMPLICIT Originator,
 *   ctlNum       [3] IMPLICIT INT8U,
 *   t            [4] IMPLICIT TimeStamp,
 *   test         [5] IMPLICIT BOOLEAN,
 *   check        [6] IMPLICIT Check
 * }
 *
 * Operate-ErrorPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   origin       [2] IMPLICIT Originator,
 *   ctlNum       [3] IMPLICIT INT8U,
 *   t            [4] IMPLICIT TimeStamp,
 *   test         [5] IMPLICIT BOOLEAN,
 *   check        [6] IMPLICIT Check,
 *   addCause     [7] IMPLICIT AddCause
 * }
 * </pre>
 */
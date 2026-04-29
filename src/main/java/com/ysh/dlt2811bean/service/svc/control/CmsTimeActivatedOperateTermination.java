/**
 * CMS Service Code 0x4B — TimeActivatedOperateTermination (time activated operate termination service).
 *
 * Corresponds to Table 71 in GB/T 45906.3-2025: TimeActivatedOperateTermination service parameters.
 *
 * Service code: 0x4A (74)
 * Service interface: TimeActivatedOperateTermination
 * Category: Control service
 *
 * The TimeActivatedOperateTermination service is used to explicitly terminate or cancel
 * a previously scheduled time-activated control operation before its execution time
 * has been reached. This allows a client to withdraw a scheduled command that is
 * no longer required or valid.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to terminate a scheduled operation</li>
 *   <li>RESPONSE_POSITIVE - Server positive response confirming termination</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference                   ObjectReference                 │
 * │ ctlVal                      Data                            │
 * │ operTm                      TimeStamp                       │
 * │ origin                      Originator                      │
 * │ ctlNum                      INT8U                           │
 * │ t                           TimeStamp                       │
 * │ test                        BOOLEAN                         │
 * │ check                       Check                           │
 * │ addCause                    AddCause OPTIONAL               │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference                   ObjectReference                 │
 * │ ctlVal                      Data                            │
 * │ operTm                      TimeStamp                       │
 * │ origin                      Originator                      │
 * │ ctlNum                      INT8U                           │
 * │ t                           TimeStamp                       │
 * │ test                        BOOLEAN                         │
 * │ check                       Check                           │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference                   ObjectReference                 │
 * │ ctlVal                      Data                            │
 * │ operTm                      TimeStamp                       │
 * │ origin                      Originator                      │
 * │ ctlNum                      INT8U                           │
 * │ t                           TimeStamp                       │
 * │ test                        BOOLEAN                         │
 * │ check                       Check                           │
 * │ addCause                    AddCause                        │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * TimeActivatedOperateTermination-RequestPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN,
 *   check        [7] IMPLICIT Check,
 *   addCause     [8] IMPLICIT AddCause OPTIONAL
 * }
 *
 * TimeActivatedOperateTermination-ResponsePDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN,
 *   check        [7] IMPLICIT Check
 * }
 *
 * TimeActivatedOperateTermination-ErrorPDU:: = SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,
 *   operTm       [2] IMPLICIT TimeStamp,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN,
 *   check        [7] IMPLICIT Check,
 *   addCause     [8] IMPLICIT AddCause
 * }
 * </pre>
 */
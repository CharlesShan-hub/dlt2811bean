/**
 * CMS Service Code 0x4A — TimeActivatedOperate (time activated operate service).
 *
 * Corresponds to Table 70 in GB/T 45906.3-2025: TimeActivatedOperate service parameters.
 *
 * Service code: 0x4A (74)
 * Service interface: TimeActivatedOperate
 * Category: Control service
 *
 * The TimeActivatedOperate service is used to request a controllable object
 * to perform a control operation at a specific time in the future. This
 * service combines the selection and value definition with a scheduled
 * execution time, allowing for time-synchronized control actions.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to operate at a specific time</li>
 *   <li>RESPONSE_POSITIVE - Server positive response confirming the timed operation</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference                   ObjectReference                  │
 * │ ctlVal                      Data                             │
 * │ operTm                      TimeStamp                        │
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
 * │ ctlVal                      Data                             │
 * │ operTm                      TimeStamp                        │
 * │ origin                      Originator                       │
 * │ ctlNum                      INT8U                            │
 * │ t                           TimeStamp                        │
 * │ test                        BOOLEAN                          │
 * │ check                       Check                            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU
 * ┌───────────:──────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference                   ObjectReference                  │
 * │ ctlVal                      Data                             │
 * │ operTm                      TimeStamp                        │
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
 * TimeActivatedOperate-RequestPDU:: = SEQUENCE {
 *   reference        [0] IMPLICIT ObjectReference,
 *   ctlVal           [1] IMPLICIT Data,
 *   operTm           [2] IMPLICIT TimeStamp,
 *   origin           [3] IMPLICIT Originator,
 *   ctlNum           [4] IMPLICIT INT8U,
 *   t                [5] IMPLICIT TimeStamp,
 *   test             [6] IMPLICIT BOOLEAN,
 *   check            [7] IMPLICIT Check
 * }
 *
 * TimeActivatedOperate-ResponsePDU:: = SEQUENCE {
 *   reference        [0] IMPLICIT ObjectReference,
 *   ctlVal           [1] IMPLICIT Data,
 *   operTm           [2] IMPLICIT TimeStamp,
 *   origin           [3] IMPLICIT Originator,
 *   ctlNum           [4] IMPLICIT INT8U,
 *   t                [5] IMPLICIT TimeStamp,
 *   test             [6] IMPLICIT BOOLEAN,
 *   check            [7] IMPLICIT Check
 * }
 *
 * TimeActivatedOperate-ErrorPDU:: = SEQUENCE {
 *   reference        [0] IMPLICIT ObjectReference,
 *   ctlVal           [1] IMPLICIT Data,
 *   operTm           [2] IMPLICIT TimeStamp,
 *   origin           [3] IMPLICIT Originator,
 *   ctlNum           [4] IMPLICIT INT8U,
 *   t                [5] IMPLICIT TimeStamp,
 *   test             [6] IMPLICIT BOOLEAN,
 *   check            [7] IMPLICIT Check,
 *   addCause         [8] IMPLICIT AddCause
 * }
 * </pre>
 */
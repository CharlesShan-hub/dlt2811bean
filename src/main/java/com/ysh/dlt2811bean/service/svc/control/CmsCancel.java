/**
 * CMS Service Code 0x48 — Cancel (cancel service).
 *
 * Corresponds to Table 68 in GB/T 45906.3-2025: Cancel service parameters.
 *
 * Service code: 0x48 (72)
 * Service interface: Cancel
 * Category: Control service
 *
 * The Cancel service is used to abort a previously issued Select, SelectWithValue,
 * or Operate command before it has been completed or executed. This allows a client
 * to withdraw a control request in cases where it was issued in error or the
 * operational conditions have changed.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client cancel request for a pending operation</li>
 *   <li>RESPONSE_POSITIVE - Server positive response confirming cancellation</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASN.1 (BER/Raw) encoded field layout:
 * <pre>
 * Request ::= SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,                -- 由模型定义
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN
 * }
 *
 * Response-Positive ::= SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,                -- 由模型定义
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN
 * }
 *
 * Response-Negative ::= SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference,
 *   ctlVal       [1] IMPLICIT Data,                -- 由模型定义
 *   operTm       [2] IMPLICIT TimeStamp OPTIONAL,
 *   origin       [3] IMPLICIT Originator,
 *   ctlNum       [4] IMPLICIT INT8U,
 *   t            [5] IMPLICIT TimeStamp,
 *   test         [6] IMPLICIT BOOLEAN,
 *   addCause     [7] IMPLICIT AddCause             -- 注意：此处Tag应为[7]，对应图片定义
 * }
 * </pre>
 */
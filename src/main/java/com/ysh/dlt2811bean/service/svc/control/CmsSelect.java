/**
 * CMS Service Code 0x44 — Select (select service).
 *
 * Corresponds to Table 65 in GB/T 45906.3-2025: Select service parameters.
 *
 * Service code: 0x44 (68)
 * Service interface: Select
 * Category: Control service
 *
 * The Select service is used to select a controllable object (such as a switchgear
 * or setting group) prior to issuing an operate command. This two-step process
 * (Select followed by Operate) ensures that the intended object is ready to be
 * controlled and prevents unauthorized or accidental operations.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client select request with target reference</li>
 *   <li>RESPONSE_POSITIVE - Server positive response confirming selection</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                           │
 * │ reference            [0] IMPLICIT ObjectReference    │
 * └──────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                           │
 * │ reference            [0] IMPLICIT ObjectReference    │
 * └──────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                           │
 * │ reference            [0] IMPLICIT ObjectReference    │
 * └──────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * Select-RequestPDU::= SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference
 * }
 *
 * Select-ResponsePDU::= SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference
 * }
 *
 * Select-ErrorPDU::= SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference
 * }
 * </pre>
 */
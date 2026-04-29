/**
 * CMS Service Code 0x3B — ConfirmEditSGValues (confirm edit setting group values).
 *
 * Corresponds to Table 43 in GB/T 45906.3-2025: ConfirmEditSGValues service parameters.
 *
 * Service code: 0x57 (87)
 * Service interface: ConfirmEditSGValues
 * Category: Setting group service
 *
 * The ConfirmEditSGValues service is used to confirm the edited setting group values
 * to take effect.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Confirm edit setting group values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response (no additional data)</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2 bytes)                                             │
 * │ ASDUHeader (variable)                                       │
 * ├─────────────────────────────────────────────────────────────┤
 * │ sgcbReference          [0] IMPLICIT ObjectReference         │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2 bytes)                                             │
 * │ ASDUHeader (variable)                                       │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2 bytes)                                             │
 * │ ASDUHeader (variable)                                       │
 * ├─────────────────────────────────────────────────────────────┤
 * │ serviceError           ServiceError                         │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * ConfirmEditSGValues-RequestPDU::= SEQUENCE {
 *   sgcbReference          [0] IMPLICIT ObjectReference
 * }
 *
 * ConfirmEditSGValues-ResponsePDU::= NULL
 *
 * ConfirmEditSGValues-ErrorPDU::= ServiceError
 * </pre>
 */
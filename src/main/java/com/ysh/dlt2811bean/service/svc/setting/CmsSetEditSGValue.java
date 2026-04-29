/**
 * CMS Service Code 0x3A — SetEditSGValue (set edit setting group values).
 *
 * Corresponds to Table 42 in GB/T 45906.3-2025: SetEditSGValue service parameters.
 *
 * Service code: 0x56 (86)
 * Service interface: SetEditSGValue
 * Category: Setting group service
 *
 * The SetEditSGValue service is used to modify a group of setting values.
 * The functional constraint (fc) is automatically identified as SE (Set Edits).
 * If all edit setting group values are set successfully, Response+ is returned.
 * If part or all fail, Response- is returned. In Response-, the setting result
 * for each value is returned in sequence.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Set edit setting group values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response (no additional data)</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with detailed results</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2 bytes)                                             │
 * │ ASDUHeader (variable)                                       │
 * ├─────────────────────────────────────────────────────────────┤
 * │ data[0..n]              SEQUENCE OF SEQUENCE {              │
 * │     reference            [0] IMPLICIT ObjectReference       │
 * │     value                [2] IMPLICIT Data                  │
 * │ }                                                           │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2 bytes)                                             │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2 bytes)                                             │
 * │ result[0..n]            SEQUENCE OF ServiceError            │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * SetEditSGValue-RequestPDU::= SEQUENCE {
 *   data                [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference        [0] IMPLICIT ObjectReference,
 *     value            [2] IMPLICIT Data
 *   }
 * }
 *
 * SetEditSGValue-ResponsePDU::= NULL
 *
 * SetEditSGValue-ErrorPDU::= SEQUENCE {
 *   result             [0] IMPLICIT SEQUENCE OF ServiceError
 * }
 * </pre>
 */
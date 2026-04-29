/**
 * CMS Service Code 0x38 — SelectActiveSG (select active setting group).
 *
 * Corresponds to Table 40 in GB/T 45906.3-2025: SelectActiveSG service parameters.
 *
 * Service code: 0x54 (84)
 * Service interface: SelectActiveSG
 * Category: Setting group service
 *
 * The SelectActiveSG service is used to select the setting group to be activated.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Select active setting group request</li>
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
 * │ settingGroupNumber     [1] IMPLICIT INT8U                   │
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
 * │ serviceError           ServiceError                         │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * SelectActiveSG-RequestPDU::= SEQUENCE {
 *   sgcbReference     [0] IMPLICIT ObjectReference,
 *   settingGroupNumber [1] IMPLICIT INT8U
 * }
 *
 * SelectActiveSG-ResponsePDU::= NULL
 * SelectActiveSG-ErrorPDU::= ServiceError
 * </pre>
 */
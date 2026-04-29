/**
 * CMS Service Code 0x9A — AssociateNegotiate (association negotiation service).
 *
 * Corresponds to Table 82 in GB/T 45906.3-2025: AssociateNegotiate service parameters.
 *
 * Service code: 0x9A (154)
 * Service interface: AssociateNegotiate
 * Category: Association service
 *
 * The AssociateNegotiate service is used for negotiating service parameters
 * between the client and the server. This includes negotiating the maximum
 * frame size (APDU), the maximum ASDU size, the protocol version, and the
 * model version supported by the server.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to negotiate association parameters</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with negotiated parameters and model version</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with service error</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ apduSize                     INT16U                          │
 * │ asduSize                     INT32U                          │
 * │ protocolVersion              INT32U                          │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ apduSize                     INT16U                          │
 * │ asduSize                     INT32U                          │
 * │ protocolVersion              INT32U                          │
 * │ modelVersion                 VisibleString                   │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ serviceError                 ServiceError                    │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * AssociateNegotiate-RequestPDU:: = SEQUENCE {
 *   apduSize               [0] IMPLICIT INT16U,
 *   asduSize               [1] IMPLICIT INT32U,
 *   protocolVersion        [2] IMPLICIT INT32U
 * }
 *
 * AssociateNegotiate-ResponseU:: = SEQUENCE {PD
 *   apduSize               [0] IMPLICIT INT16U,
 *   asduSize               [1] IMPLICIT INT32U,
 *   protocolVersion        [2] IMPLICIT INT32U,
 *   modelVersion           [3] IMPLICIT VisibleString
 * }
 *
 * AssociateNegotiate-ErrorPDU:: = ServiceError
 * </pre>
 */
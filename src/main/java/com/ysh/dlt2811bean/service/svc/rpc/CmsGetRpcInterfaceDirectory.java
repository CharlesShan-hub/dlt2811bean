/**
 * CMS Service Code 0x6E — GetRpcInterfaceDirectory (get RPC interface directory service).
 *
 * Corresponds to Table 77 in GB/T 45906.3-2025: GetRpcInterfaceDirectory service parameters.
 *
 * Service code: 0x6E (110)
 * Service interface: GetRpcInterfaceDirectory
 * Category: Remote Procedure Call service
 *
 * The GetRpcInterfaceDirectory service is used by a client to retrieve a list of all
 * available RPC interfaces on the server. This service supports pagination through
 * the use of a reference point. The client can optionally specify a starting point
 * (referenceAfter), and the server returns a list of interface names and indicates
 * if more data is available.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to get the RPC interface directory</li>
 *   <li>RESPONSE_POSITIVE - Server response containing interface references and continuation flag</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ referenceAfter          [0] IMPLICIT VisibleString OPTIONAL  │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ reference              [0] IMPLICIT SEQUENCE OF VisibleString│
 * │ moreFollows            [1] IMPLICIT BOOLEAN DEFAULT TRUE     │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ serviceError                 ServiceError                    │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetRpcerfaceIntDirectory-RequestPDU:: = SEQUENCE {
 *   referenceAfter  [0] IMPLICIT VisibleString OPTIONAL
 * }
 * GetRpcInterfaceDirectory-ResponsePDU:: = SEQUENCE {
 *   reference       [0] IMPLICIT SEQUENCE OF VisibleString,
 *   moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * GetRpcInterfaceDirectory-ErrorPDU:: = ServiceError
 * </pre>
 */
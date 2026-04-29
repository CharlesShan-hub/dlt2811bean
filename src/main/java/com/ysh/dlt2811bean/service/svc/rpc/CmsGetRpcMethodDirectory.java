/**
 * CMS Service Code 0x6F — GetRpcMethodDirectory (get RPC method directory service).
 *
 * Corresponds to Table 78 in GB/T 45906.3-2025: GetRpcMethodDirectory service parameters.
 *
 * Service code: 0x6F (111)
 * Service interface: GetRpcMethodDirectory
 * Category: Remote Procedure Call service
 *
 * The GetRpcMethodDirectory service is used by a client to retrieve the names of all methods
 * associated with a specified RPC interface. The client provides the interface name and an
 * optional starting point (referenceAfter) for pagination. The server responds with a list
 * of method names and indicates if more data is available.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to get the RPC method directory</li>
 *   <li>RESPONSE_POSITIVE - Server response containing method references and continuation flag</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ interface               [0] IMPLICIT VisibleString OPTIONAL  │
 * │ referenceAfter          [1] IMPLICIT VisibleString OPTIONAL  │
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
 * │ serviceError                ServiceError                     │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetRpcMethodDirectory-RequestPDU:: = SEQUENCE {
 *   interface            [0] IMPLICIT VisibleString OPTIONAL,
 *   referenceAfter       [1] IMPLICIT VisibleString OPTIONAL
 * }
 * GetRpcMethodDirectory-ResponsePDU:: = SEQUENCE {
 *   reference            [0] IMPLICIT SEQUENCE OF VisibleString,
 *   moreFollows          [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * GetRpcMethodDirectory-ErrorPDU:: = ServiceError
 * </pre>
 */
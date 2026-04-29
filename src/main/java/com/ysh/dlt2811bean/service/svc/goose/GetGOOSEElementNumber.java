/**
 * 8.9.3 (GetGOOSEElementNumber)
 *
 * Corresponds to Table 59 in GB/T 45906.3-2025: GetGOOSEElementNumber service parameters.
 *
 * Service code: N/A (Part of GOOSE Management Services)
 * Service interface: GetGOOSEElementNumber
 * Category: General station event service
 *
 * The GetGOOSEElementNumber service is used to retrieve the sequence numbers
 * (offsets) of the data elements within a GOOSE dataset. This is complementary
 * to the GetGoReference service. While GetGoReference returns the names and
 * functional constraints, GetGOOSEElementNumber returns the positional indices
 * (memberOffset) of those elements within the dataset structure.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Client request for element offsets</li>
 *   <li>RESPONSE_POSITIVE - Server response containing element offsets</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ gocbReference              ObjectReference                   │
 * │ memberData                 SEQUENCE OF SEQUENCE {            │
 * │   reference                ObjectReference                   │
 * │   fc                       FunctionalConstraint              │
 * │ }                                                            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ gocbReference              ObjectReference                   │
 * │ confRev                    INT32U                            │
 * │ datSet                     ObjectReference                   │
 * │ memberOffset               SEQUENCE OF INT16U                │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ serviceError               ServiceError                      │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetGOOSEElementNumber-RequestPDU::= SEQUENCE {
 *   gocbReference    [0] IMPLICIT ObjectReference,
 *   memberData       [1] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference       [0] IMPLIC ObjectReference,
 *    IT fc              [1] IMPLICIT FunctionalConstraint
 *   }
 * }
 *
 * GetGOOSEElementNumber-ResponsePDU::= SEQUENCE {
 *   gocbReference    [0] IMPLICIT ObjectReference,
 *   confRev          [1] IMPLICIT INT32U,
 *   datSet           [2] IMPLICITjectReference, Ob
 *   memberOffset     [3] IMPLICIT SEQUENCE OF INT16U
 * }
 *
 * GetGOOSEElementNumber-ErrorPDU::= ServiceError
 * </pre>
 */
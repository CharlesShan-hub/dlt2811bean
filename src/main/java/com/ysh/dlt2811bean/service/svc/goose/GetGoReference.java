/**
 * 8.9.2 读 GOOSE 引用服务 (GetGoReference)
 *
 * Corresponds to Table 58 in GB/T 45906.3-2025: GetGoReference service parameters.
 *
 * Service code: N/A (Part of GOOSE Management Services)
 * Service interface: GetGoReference
 * Category: General station event service
 *
 * The GetGoReference service is used to retrieve the references and functional
 * constraints of the members within a GOOSE Control Block (GoCB) dataset.
 * This service is typically used by a client to discover the structure of a
 * GOOSE dataset published by a server.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Client request for GOOSE member references</li>
 *   <li>RESPONSE_POSITIVE - Server response containing dataset structure</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ gocbReference              ObjectReference                   │
 * │ memberOffset               INT16U (1..n)                     │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ gocbReference              ObjectReference                   │
 * │ confRev                    INT32U                            │
 * │ datSet                     ObjectReference                   │
 * │ memberData                 SEQUENCE OF SEQUENCE {            │
 * │   reference                ObjectReference                   │
 * │   fc                       FunctionalConstraint              │
 * │ }                                                            │
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
 * GetGoReference-RequestPDU::= SEQUENCE {
 *   gocbReference    [0] IMPLICIT ObjectReference,
 *   memberOffset      [1] IMPLICIT SEQUENCE OF INT16U
 * }
 *
 * GetGoReference-ResponsePDU::= SEQUENCE {
 *   gocbReference    [0] IMPLICIT ObjectReference,
 *   confRev          [1] IMPLICIT INT32U,
 *   datSet           [2] IMPLICIT ObjectReference,
 *   memberData       [3] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     fc              [1] IMPLICIT FunctionalConstraint
 *   }
 * }
 *
 * GetGoReference-ErrorPDU::= ServiceError
 * </pre>
 */
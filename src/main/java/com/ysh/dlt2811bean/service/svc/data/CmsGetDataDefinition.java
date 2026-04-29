/**
 * CMS Service Code 0x33 — GetDataDefinition (read data definition).
 *
 * Corresponds to Table 34 in GB/T 45906.3-2025: GetDataDefinition service parameters.
 *
 * Service code: 0x33 (51)
 * Service interface: GetDataDefinition
 * Category: Data access service
 *
 * The GetDataDefinition service is used to retrieve the structural definitions of a set of data objects
 * or data attributes. When the data is a data object, the cdcType should be set to the corresponding
 * CDC type; when the data is a data attribute, cdcType should be empty.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get data definition request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with data definitions</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ data[0..n]                 SEQUENCE OF SEQUENCE {           │
 * │   reference                ObjectReference                  │
 * │   fc                       FunctionalConstraint (OPTIONAL)  │
 * │ }                                                           │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ data[0..n]                 SEQUENCE OF SEQUENCE {           │
 * │   cdcType                  VisibleString (OPTIONAL)         │
 * │   definition               DataDefinition                   │
 * │ }                                                           │
 * │ moreFollows                BOOLEAN DEFAULT TRUE             │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ serviceError               ServiceError                     │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetDataDefinition-RequestPDU::= SEQUENCE {
 *   data               [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference         [0] IMPLICIT ObjectReference,
 *     fc                [1] IMPLICIT FunctionalConstraint OPTIONAL
 *   }
 * }
 *
 * GetDataDefinition-ResponsePDU::= SEQUENCE {
 *   data               [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     cdcType           [0] IMPLICIT VisibleString OPTIONAL,
 *     definition        [1] IMPLICIT DataDefinition
 *   },
 *   moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetDataDefinition-ErrorPDU::= ServiceError
 * </pre>
 */
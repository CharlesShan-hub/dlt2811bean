/**
 * CMS Service Code 0x56 — GetDataValues (read data values).
 *
 * Corresponds to Table 31 in GB/T 45906.3-2025: GetDataValues service parameters.
 *
 * Service code: 0x30 (48)
 * Service interface: GetDataValues
 * Category: Data access service
 *
 * The GetDataValues service is used to retrieve the values of a set of data objects
 * or data attributes. The fc parameter is used to specify functional constraints
 * for filtering specific categories of data attributes. If fc is XX or empty, no filtering is applied.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get data values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with data values</li>
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
 * │   fc                       FunctionalConstraint OPTIONAL    │
 * │ }                                                           │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ value[0..n]                SEQUENCE OF Data                 │
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
 * GetDataValues-RequestPDU::= SEQUENCE {
 *   data    [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference [0] IMPLICIT ObjectReference,
 *     fc        [1] IMPLICIT FunctionalConstraint OPTIONAL
 *   }
 * }
 *
 * GetDataValues-ResponsePDU::= SEQUENCE {
 *   value        [0] IMPLICIT SEQUENCE OF Data,
 *   moreFollows  [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetDataValues-ErrorPDU::= ServiceError
 * </pre>
 */
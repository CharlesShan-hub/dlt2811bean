/**
 * CMS Service Code 0x39 — GetDataSetDirectory (read data set directory).
 *
 * Corresponds to Table 39 in GB/T 45906.3-2025: GetDataSetDirectory service parameters.
 *
 * Service code: 0x39 (57)
 * Service interface: GetDataSetDirectory
 * Category: Data set service
 *
 * The GetDataSetDirectory service is used to batch retrieve the references of data set members.
 * When referenceAfter is not specified in the request, the directory should be read starting from
 * the first member. When referenceAfter is specified, the directory should be read starting after
 * the specified member.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get data set directory request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with member directory data</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ datasetReference            ObjectReference                 │
 * │ referenceAfter              ObjectReference (OPTIONAL)      │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ memberData[0..n]            SEQUENCE OF SEQUENCE {          │
 * │   reference                  ObjectReference                │
 * │   fc                         FunctionalConstraint           │
 * │ }                                                           │
 * │ moreFollows                  BOOLEAN DEFAULT TRUE           │
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
 * GetDataSetDirectory-RequestPDU::= SEQUENCE {
 *   datasetReference  [0] IMPLICIT ObjectReference,
 *   referenceAfter    [1] IMPLICIT ObjectReference OPTIONAL
 * }
 *
 * GetDataSetDirectory-ResponsePDU::= SEQUENCE {
 *   memberData        [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     fc              [1] IMPLICIT FunctionalConstraint
 *   },
 *   moreFollows       [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetDataSetDirectory-ErrorPDU::= ServiceError
 * </pre>
 */
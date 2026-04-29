/**
 * CMS Service Code 0x36 — CreateDataSet (create data set).
 *
 * Corresponds to Table 37 in GB/T 45906.3-2025: CreateDataSet service parameters.
 *
 * Service code: 0x36 (54)
 * Service interface: CreateDataSet
 * Category: Data set service
 *
 * The CreateDataSet service is used to dynamically create new data sets. The created data set
 * can be either persistent or non-persistent. Non-persistent data sets are automatically deleted
 * after the association is released. Persistent data sets are not automatically deleted even
 * if the server restarts.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Create data set request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response (no payload)</li>
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
 * │ memberData[0..n]            SEQUENCE OF SEQUENCE {          │
 * │   reference                  ObjectReference                │
 * │   fc                         FunctionalConstraint           │
 * │ }                                                           │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ (No additional data)                                        │
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
 * CreateDataSet-RequestPDU::= SEQUENCE {
 *   datasetReference  [0] IMPLICIT ObjectReference,
 *   referenceAfter    [1] IMPLICIT ObjectReference OPTIONAL,
 *   memberData        [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference       [0] IMPLICIT ObjectReference,
 *     fc              [1] IMPLICIT FunctionalConstraint
 *   }
 * }
 *
 * CreateDataSet-ResponsePDU::= SEQUENCE {
 *   -- NULL
 * }
 *
 * CreateDataSet-ErrorPDU::= ServiceError
 * </pre>
 */
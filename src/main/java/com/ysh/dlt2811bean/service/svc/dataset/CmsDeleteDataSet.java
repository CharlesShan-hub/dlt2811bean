/**
 * CMS Service Code 0x37 — DeleteDataSet (delete data set).
 *
 * Corresponds to Table 38 in GB/T 45906.3-2025: DeleteDataSet service parameters.
 *
 * Service code: 0x37 (55)
 * Service interface: DeleteDataSet
 * Category: Data set service
 *
 * The DeleteDataSet service is used to delete a specified data set.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Delete data set request</li>
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
 * DeleteDataSet-RequestPDU::= SEQUENCE {
 *   datasetReference  [0] IMPLICIT ObjectReference
 * }
 *
 * DeleteDataSet-ResponsePDU::= SEQUENCE {
 *   -- NULL
 * }
 *
 * DeleteDataSet-ErrorPDU::= ServiceError
 * </pre>
 */
/**
 * CMS Service Code 0x3B — SetDataSetValues (set data set values).
 *
 * Corresponds to Table 36 in GB/T 45906.3-2025: SetDataSetValues service parameters.
 *
 * Service code: 0x3B (59)
 * Service interface: SetDataSetValues
 * Category: Data set service
 *
 * The SetDataSetValues service is used to batch set the values of data set members.
 * Each data should be arranged in the index order within the data set. When referenceAfter
 * is not specified, values should be set sequentially starting from the first member of
 * the data set. When referenceAfter is specified, values should be set sequentially
 * starting after the referenceAfter member.
 *
 * Response behavior:
 * <ul>
 *   <li>If all data set values are set successfully, return Response+ (positive response).</li>
 *   <li>If partial or all settings fail, return Response- (negative response) with the result
 *       of each data set value setting.</li>
 * </ul>
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Set data set values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response (NULL)</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error results</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ datasetReference                 ObjectReference             │
 * │ referenceAfter [0..1]            ObjectReference (OPTIONAL)  │
 * │ memberValue [1..n]               SEQUENCE OF Data            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ (Positive): NULL (no data returned)
 *
 * Response- (Negative):
 * ┌─────────────────────────────────────────────────────────────┐
 * │ result [1..n]                    SEQUENCE OF ServiceError   │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 */
/**
 * CMS Service Code 0x3A — GetDataSetValues (read data set values).
 *
 * Corresponds to Table 35 in GB/T 45906.3-2025: GetDataSetValues service parameters.
 *
 * Service code: 0x3A (58)
 * Service interface: GetDataSetValues
 * Category: Data set service
 *
 * The GetDataSetValues service is used to batch retrieve the values of data set members.
 * When referenceAfter is not specified, values should be returned sequentially starting
 * from the first member of the data set. When referenceAfter is specified, values should
 * be returned sequentially starting after the referenceAfter member.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get data set values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with member values</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ datasetReference            ObjectReference                  │
 * │ referenceAfter              [0..1] ObjectReference OPTIONAL  │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response ASDU (positive):
 * ┌─────────────────────────────────────────────────────────────┐
 * │ value                       [0] SEQUENCE OF Data            │
 * │ moreFollows                 [1] BOOLEAN DEFAULT TRUE        │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * Service requirements:
 * <ul>
 *   <li>If referenceAfter is not specified, return values sequentially from the first member.</li>
 *   <li>If referenceAfter is specified, return values sequentially after that member.</li>
 *   <li>If one ASDU cannot return all data values, set moreFollows to TRUE.</li>
 *   <li>If data set does not exist or a member cannot be accessed, return error response.</li>
 * </ul>
 *
 */
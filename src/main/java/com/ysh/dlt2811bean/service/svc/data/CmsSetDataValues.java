/**
 * CMS Service Code 0x57 — SetDataValues (set data values).
 *
 * Corresponds to Table 32 in GB/T 45906.3-2025: SetDataValues service parameters.
 *
 * Service code: 0x30 (49)
 * Service interface: SetDataValues
 * Category: Data access service
 *
 * The SetDataValues service is used to batch set the values of a set of data.
 * Each data is uniquely indexed by Reference. When fc (functional constraint)
 * is included, it indicates the value of the FCD. When fc is not included,
 * it indicates the values of all data attributes. A positive response is returned
 * if all data values are set successfully; a negative response is returned if
 * some or all fail. In the negative response, the setting result for each data
 * value is returned in order.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Set data values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response (no payload)</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with result list</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ data[0..n]                                                  │
 * │   └─ SEQUENCE {                                             │
 * │       reference            ObjectReference                  │
 * │       fc                   FunctionalConstraint (OPTIONAL)  │
 * │       value                Data                             │
 * │   }                                                         │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response Positive ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ (NULL)                                                      │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response Negative ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ result[0..n]               ServiceError                     │
 * └─────────────────────────────────────────────────────────────┘
 */
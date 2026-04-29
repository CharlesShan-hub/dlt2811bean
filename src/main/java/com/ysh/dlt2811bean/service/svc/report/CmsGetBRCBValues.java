/**
 * CMS Service Code 0x5B — GetBRCBValues (get buffered report control block values).
 *
 * Corresponds to Table 47 in GB/T 45906.3-2025: GetBRCBValues service parameters.
 *
 * Service code: 0x5B (91)
 * Service interface: GetBRCBValues
 * Category: Reporting service
 *
 * The GetBRCBValues service is used to retrieve all attributes of the buffered
 * report control block (BRCB).
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get BRCB values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with either error or BRCB data</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ brcbReference[0..n]        SEQUENCE OF ObjectReference      │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ error/brcb[0..n]           SEQUENCE OF CHOICE {             │
 * │   error                     ServiceError                    │
 * │   brcb                      BRCB                            │
 * │ }                                                           │
 * │ moreFollows                 BOOLEAN (DEFAULT TRUE)          │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ serviceError                ServiceError                    │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetBRCBValues-RequestPDU::= SEQUENCE {
 *   brcbReference      [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 *
 * GetBRCBValues-ResponsePDU::= SEQUENCE {
 *   errorBrcb          [0] IMPLICIT SEQUENCE OF CHOICE {
 *     error            [0] IMPLICIT ServiceError,
 *     brcb             [1] IMPLICIT BRCB
 *   },
 *   moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetBRCBValues-ErrorPDU::= ServiceError
 * </pre>
 */
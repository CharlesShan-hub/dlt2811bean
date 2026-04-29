/**
 * CMS Service Code 0x69 — GetMSVCBValues (get multicast sampling value control block values).
 *
 * Corresponds to Table 63 in GB/T 45906.3-2025: GetMSVCBValues service parameters.
 *
 * Service code: 0x69 (105)
 * Service interface: GetMSVCBValues
 * Category: MSV control block service
 *
 * The GetMSVCBValues service is used to retrieve the configuration values of
 * one or more Multicast Sampling Value Control Blocks (MSVCB).
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get MSVCB values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with MSVCB data</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference[0..n]             SEQUENCE OF ObjectReference     │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ error/msvcb[0..n]           SEQUENCE OF CHOICE {            │
 * │   error                     ServiceError                    │
 * │   msvcb                     MSVCB                           │
 * │ }                                                           │
 * │ moreFollows                 BOOLEAN DEFAULT TRUE            │
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
 * GetMSVCBValues-RequestPDU::= SEQUENCE {
 *   reference      [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 *
 * GetMSVCBValues-ResponsePDU::= SEQUENCE {
 *   errorMsvcb     [0] IMPLICIT SEQUENCE OF CHOICE {
 *     error        [0] IMPLICIT ServiceError,
 *     msvcb        [1] IMPLICIT MSVCB
 *   },
 *   moreFollows    [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetMSVCBValues-ErrorPDU::= ServiceError
 * </pre>
 */
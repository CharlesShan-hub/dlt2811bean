/**
 * CMS Service Code 0x50 — GetFile (read file service).
 *
 * Corresponds to Table 72 in GB/T 45906.3-2025: GetFile service parameters.
 *
 * Service code: 0x80 (128)
 * Service interface: GetFile
 * Category: File service
 *
 * The GetFile service is used by a client to read a file from a server. The client
 * specifies the file name and the starting position within the file. The server
 * responds with a chunk of file data and indicates whether the end of the file
 * has been reached. The start position is 1-based.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to read a portion of a file</li>
 *   <li>RESPONSE_POSITIVE - Server response containing file data and EOF flag</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ fileName                     VisibleString255                │
 * │ startPosition                INT32U                          │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ fileData                     OCTET STRING (variable length)  │
 * │ endOfFile                    BOOLEAN (default FALSE)         │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ serviceError                 ServiceError                    │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetFile-RequestPDU:: = SEQUENCE {
 *   fileName       [0] IMPLICIT VisibleString255,
 *   startPosition  [1] IMPLICIT INT32U }
 *
 * GetFile-ResponsePDU:: = SEQUENCE {
 *   fileData       [0] IMPLICIT OCTET STRING,
 *   endOfFile      [1] IMPLICIT BOOLEAN DEFAULT FALSE
 * }
 *
 * GetFile-ErrorPDU:: = ServiceError
 * </pre>
 */
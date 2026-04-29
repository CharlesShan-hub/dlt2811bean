/**
 * CMS Service Code 0x82 — DeleteFile (delete file service).
 *
 * Corresponds to Table 74 in GB/T 45906.3-2025: DeleteFile service parameters.
 *
 * Service code: 0x82 (130)
 * Service interface: DeleteFile
 * Category: File service
 *
 * The DeleteFile service is used by a client to delete a specified file from a server.
 * The client provides the name of the file to be deleted. This service is typically
 * used for file management and cleanup operations.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to delete a file</li>
 *   <li>RESPONSE_POSITIVE - Server positive response (no data returned)</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ fileName                     VisibleString255                │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
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
 * DeleteFile-RequestPDU:: = SEQUENCE {
 *   fileName    [0] IMPLICIT VisibleString255
 * }
 *
 * DeleteFile-ResponsePDU:: = NULL
 *
 * DeleteFile-ErrorPDU:: = ServiceError
 * </pre>
 */
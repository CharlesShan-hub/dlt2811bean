/**
 * CMS Service Code 0x83 — GetFileAttributeValues (read file attribute values service).
 *
 * Corresponds to Table 75 in GB/T 45906.3-2025: GetFileAttributeValues service parameters.
 *
 * Service code: 0x83 (131)
 * Service interface: GetFileAttributeValues
 * Category: File service
 *
 * The GetFileAttributeValues service is used by a client to retrieve the attributes
 * of a specified file from a server. The client provides the file name, and the server
 * responds with a FileEntry structure containing the file's metadata attributes.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to read file attributes</li>
 *   <li>RESPONSE_POSITIVE - Server response containing file attributes (FileEntry)</li>
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
 * │ fileEntry                    FileEntry                       │
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
 * GetFileAttributeValues-RequestPDU:: = SEQUENCE {
 *   fileName [0] IMPLICIT VisibleString255
 * }
 * GetFileAttributeValues-ponsePDResU:: = FileEntry
 * GetFileAttributeValues-ErrorPDU:: = ServiceError
 * </pre>
 */
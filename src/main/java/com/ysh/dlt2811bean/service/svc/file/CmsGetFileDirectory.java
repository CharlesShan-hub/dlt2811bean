/**
 * CMS Service Code 0x84 — GetFileDirectory (list file directory service).
 *
 * Corresponds to Table 76 in GB/T 45906.3-2025: GetFileDirectory service parameters.
 *
 * Service code: 0x84 (132)
 * Service interface: GetFileDirectory
 * Category: File service
 *
 * The GetFileDirectory service is used by a client to list files and their attributes
 * within a specified directory on a server. The client can provide filtering criteria
 * including a path name, a time range, and a starting file name. The server responds
 * with a sequence of FileEntry structures and indicates if more data is available.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client request to list directory contents</li>
 *   <li>RESPONSE_POSITIVE - Server response containing file entries and continuation flag</li>
 *   <li>RESPONSE_NEGATIVE - Server error response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ pathName             [0] IMPLICIT VisibleString255 OPTIONAL  │
 * │ startTime            [1] IMPLICIT TimeStamp OPTIONAL         │
 * │ stopTime             [2] IMPLICIT TimeStamp OPTIONAL         │
 * │ fileAfter            [3] IMPLICIT VisibleString255 OPTIONAL  │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ fileEntry           [0] IMPLICIT SEQUENCE OF FileEntry       │
 * │ moreFollows         [1] IMPLICIT BOOLEAN DEFAULT TRUE        │
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
 * GetFileDirectory-RequestPDU:: = SEQUENCE {
 *   pathName                     [0] IMPLICIT VisibleString255 OPTIONAL,
 *   startTime                    [1] IMPLICIT TimeStamp OPTIONAL,
 *   stopTime                     [2] IMPLICIT TimeStamp OPTIONAL,
 *   fileAfter                    [3] IMPLICIT VisibleString255 OPTIONAL
 * }
 * GetFileDirectory-ResponsePDU:: = SEQUENCE {
 *   fileEntry                    [0] IMPLICIT SEQUENCE OF FileEntry,
 *   moreFollows                  [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * GetFileDirectory-ErrorPDU:: = ServiceError
 * </pre>
 */
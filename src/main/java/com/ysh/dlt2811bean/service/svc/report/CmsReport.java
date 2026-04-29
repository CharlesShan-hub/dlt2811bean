/**
 * CMS Service Code 0x5A — Report (report service).
 *
 * Corresponds to Table 46 in GB/T 45906.3-2025: Report service parameters.
 *
 * Service code: 0x5A (90)
 * Service interface: Report
 * Category: Reporting service
 *
 * The Report service is used by the server to actively send subscribed data to the client.
 *
 * This class supports only RESPONSE_POSITIVE message type as reports are sent by server actively:
 * <ul>
 *   <li>RESPONSE_POSITIVE - Server positive response containing report data</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ rptID                        VisibleString129               │
 * │ optFlds                      RCBOptFlds                     │
 * │ sqNum                        INT16U (OPTIONAL)              │
 * │ subSqNum                     INT16U (OPTIONAL)              │
 * │ moreSegmentsFollow           BOOLEAN (OPTIONAL)             │
 * │ datSet                       ObjectReference (OPTIONAL)     │
 * │ bufOvfl                      BOOLEAN (OPTIONAL)             │
 * │ confRev                      INT32U (OPTIONAL)              │
 * │ entry                       SEQUENCE {                      │
 * │   timeOfEntry                EntryTime (OPTIONAL)           │
 * │   entryID                    EntryID (OPTIONAL)             │
 * │   entryData[1..n]           SEQUENCE OF SEQUENCE {          │
 * │     reference                ObjectReference (OPTIONAL)     │
 * │     fc                       FunctionalConstraint (OPTIONAL)│
 * │     id                       INT16U                         │
 * │     value                    Data                           │
 * │     reason                   ReasonCode (OPTIONAL)          │
 * │   }                                                         │
 * │ }                                                           │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * Report-ResponsePDU::= SEQUENCE {
 *   rptID               [0] IMPLICIT VisibleString129,
 *   optFlds             [1] IMPLICIT RCBOptFlds,
 *   sqNum               [2] IMPLICIT INT16U OPTIONAL,
 *   subSqNum            [3] IMPLICIT INT16U OPTIONAL,
 *   moreSegmentsFollow  [4] IMPLICIT BOOLEAN OPTIONAL,
 *   datSet              [5] IMPLICIT ObjectReference OPTIONAL,
 *   bufOvfl             [6] IMPLICIT BOOLEAN OPTIONAL,
 *   confRev             [7] IMPLICIT INT32U OPTIONAL,
 *   entry               [8] IMPLICIT SEQUENCE {
 *     timeOfEntry       [0] IMPLICIT EntryTime OPTIONAL,
 *     entryID           [1] IMPLICIT EntryID OPTIONAL,
 *     entryData         [2] IMPLICIT SEQUENCE OF SEQUENCE {
 *       reference       [0] IMPLICIT ObjectReference OPTIONAL,
 *       fc              [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *       id              [2] IMPLICIT INT16U,
 *       value           [3] IMPLICIT Data,
 *       reason          [4] IMPLICIT ReasonCode OPTIONAL
 *     }
 *   }
 * }
 * </pre>
 */
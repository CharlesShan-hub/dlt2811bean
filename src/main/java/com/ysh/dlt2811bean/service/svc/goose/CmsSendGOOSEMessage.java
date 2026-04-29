/**
 * GOOSE Service — SendGOOSEMessage (send GOOSE message service).
 *
 * Corresponds to Table 57 in GB/T 45906.3-2025: SendGOOSEMessage service parameters.
 *
 * Service type: GOOSE (Generic Object Oriented Substation Event)
 * Service interface: SendGOOSEMessage
 * Category: General station event service
 *
 * The SendGOOSEMessage service is used for transmitting real-time status data
 * and fast tripping commands. Unlike traditional client/server services, this
 * service operates on a publish/subscribe model using direct Ethernet mapping
 * (GSE) for transmission. The service does not use the standard CMS
 * request/response mechanism and has no service code in APCH.
 *
 * This class supports only INDICATION message type as GOOSE messages are
 * sent by publishers to subscribers:
 * <ul>
 *   <li>INDICATION - GOOSE message sent by publisher (no response)</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * GOOSE ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ goID                         VisibleString129                │
 * │ datSet[0..1]                 ObjectReference (OPTIONAL)      │
 * │ goRef[0..1]                  ObjectReference (OPTIONAL)      │
 * │ t                            TimeStamp                       │
 * │ stNum                        INT32U                          │
 * │ sqNum                        INT32U                          │
 * │ simulation                   BOOLEAN                         │
 * │ confRev                      INT32U                          │
 * │ ndsCom                       BOOLEAN                         │
 * │ data[1..n]                   SEQUENCE OF Data                │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * SendGOOSEMessage-PDU::= SEQUENCE {
 *   goID             [0] IMPLICIT VisibleString129,
 *   datSet           [1] IMPLICIT ObjectReference OPTIONAL,
 *   goRef            [2] IMPLICIT ObjectReference OPTIONAL,
 *   t                [3] IMPLICIT TimeStamp,
 *   stNum            [4] IMPLICIT INT32U,
 *   sqNum            [5] IMPLICIT INT32U,
 *   simulation       [6] IMPLICIT BOOLEAN,
 *   confRev          [7] IMPLICIT INT32U,
 *   ndsCom           [8] IMPLICIT BOOLEAN,
 *   data             [9] IMPLICIT SEQUENCE OF Data
 * }
 * </pre>
 */
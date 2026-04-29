package com.ysh.dlt2811bean.service.svc.goose;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

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
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSendGOOSEMessage extends CmsAsdu<CmsSendGOOSEMessage> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsSendGOOSEMessage(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else {
            throw new IllegalArgumentException("SendGOOSEMessage does not support " + messageType);
        }
    }

    public CmsSendGOOSEMessage(boolean isResp, boolean isErr) {
        this(MessageType.REQUEST);
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.Send_GOOSE_Message;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsSendGOOSEMessage copy() {
        CmsSendGOOSEMessage copy = new CmsSendGOOSEMessage(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsSendGOOSEMessage read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsSendGOOSEMessage) new CmsSendGOOSEMessage(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsSendGOOSEMessage sendGOOSEMessage) {
        sendGOOSEMessage.encode(pos);
    }

}

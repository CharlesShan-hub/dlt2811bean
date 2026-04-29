package com.ysh.dlt2811bean.service.svc.sv;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * MSV Service — SendMSVMessage (send multicast sampling value message).
 *
 * Corresponds to Table 62 in GB/T 45906.3-2025: SendMSVMessage service parameters.
 *
 * Service type: MSV (Multicast Sampling Value)
 * Service interface: SendMSVMessage
 * Category: Multicast sampling value service
 *
 * The SendMSVMessage service is used for transmitting multicast sampled value
 * messages containing measurement data. Similar to GOOSE, this service operates
 * on a publish/subscribe model using direct Ethernet mapping for transmission
 * and does not use the standard CMS request/response mechanism.
 *
 * This class supports only INDICATION message type as MSV messages are
 * sent by publishers to subscribers:
 * <ul>
 *   <li>INDICATION - MSV message sent by publisher (no response)</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * MSV ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ msvID                         VisibleString129               │
 * │ datSet[0..1]                  ObjectReference (OPTIONAL)     │
 * │ smpCnt                        INT16U                         │
 * │ confRev                       INT32U                         │
 * │ refrTm[0..1]                  TimeStamp (OPTIONAL)           │
 * │ smpSynch                      INT8U                          │
 * │ smpRate[0..1]                 INT16U (OPTIONAL)              │
 * │ simulation                    BOOLEAN                        │
 * │ sample[1..n]                  SEQUENCE OF Data               │
 * │ smpMod[0..1]                  SmpMod (OPTIONAL)              │
 * └──────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * SendMSVMessage-PDU::= SEQUENCE {
 *   msvID           [0] IMPLICIT VisibleString129,
 *   datSet          [1] IMPLICIT ObjectReference OPTIONAL,
 *   smpCnt          [2] IMPLICIT INT16U,
 *   confRev         [3] IMPLICIT INT32U,
 *   refrTm          [4] IMPLICIT TimeStamp OPTIONAL,
 *   smpSynch        [5] IMPLICIT INT8U,
 *   smpRate         [6] IMPLICIT INT16U OPTIONAL,
 *   simulation      [7] IMPLICIT BOOLEAN,
 *   sample          [8] IMPLICIT SEQUENCE OF Data,
 *   smpMod          [9] IMPLICIT SmpMod OPTIONAL
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSendMSVMessage extends CmsAsdu<CmsSendMSVMessage> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsSendMSVMessage(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else {
            throw new IllegalArgumentException("SendMSVMessage does not support " + messageType);
        }
    }

    public CmsSendMSVMessage(boolean isResp, boolean isErr) {
        this(MessageType.REQUEST);
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.Send_MSVMessage;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsSendMSVMessage copy() {
        CmsSendMSVMessage copy = new CmsSendMSVMessage(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsSendMSVMessage read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsSendMSVMessage) new CmsSendMSVMessage(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsSendMSVMessage sendMSVMessage) {
        sendMSVMessage.encode(pos);
    }

}

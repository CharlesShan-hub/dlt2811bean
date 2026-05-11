package com.ysh.dlt2811bean.service.svc.sv;

import com.ysh.dlt2811bean.datatypes.compound.CmsUtcTime;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsSmpMod;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt8U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import static com.ysh.dlt2811bean.service.protocol.enums.MessageType.*;
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

    // ==================== Fields based on Table 62 ====================

    public CmsVisibleString msvID = new CmsVisibleString().max(129);
    public CmsObjectReference datSet = new CmsObjectReference();
    public CmsInt16U smpCnt = new CmsInt16U();
    public CmsInt32U confRev = new CmsInt32U();
    public CmsUtcTime refrTm = new CmsUtcTime();
    public CmsInt8U smpSynch = new CmsInt8U();
    public CmsInt16U smpRate = new CmsInt16U();
    public CmsBoolean simulation = new CmsBoolean();
    public CmsData<?> sample = new CmsData<>();
    public CmsSmpMod smpMod = new CmsSmpMod();

    // ========================= Constructor ============================

    public CmsSendMSVMessage() {
        this(MessageType.INDICATION); // default
    }

    public CmsSendMSVMessage(MessageType messageType) {
        super(ServiceName.SEND_MSV_MESSAGE, messageType);
        // message type is not need for SendMSVMessage
        registerField("msvID");
        registerOptionalField("datSet");
        registerField("smpCnt");
        registerField("confRev");
        registerOptionalField("refrTm");
        registerField("smpSynch");
        registerOptionalField("smpRate");
        registerField("simulation");
        registerField("sample");
        registerOptionalField("smpMod");
    }

    // ====================== Convenience Setters =======================

    public CmsSendMSVMessage msvID(String id) { 
        this.msvID.set(id); 
        return this; 
    }

    public CmsSendMSVMessage datSet(String ds) { 
        this.datSet.set(ds); 
        return this; 
    }

    public CmsSendMSVMessage smpCnt(int cnt) { 
        this.smpCnt.set(cnt); 
        return this; 
    }

    public CmsSendMSVMessage confRev(long rev) { 
        this.confRev.set(rev); 
        return this; 
    }

    public CmsSendMSVMessage smpSynch(int synch) { 
        this.smpSynch.set(synch); 
        return this; 
    }

    public CmsSendMSVMessage smpRate(int rate) { 
        this.smpRate.set(rate); 
        return this; 
    }

    public CmsSendMSVMessage simulation(boolean sim) { 
        this.simulation.set(sim); 
        return this; 
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsSendMSVMessage sample(CmsType<?> val) {
        this.sample = new CmsData(val);
        return this;
    }
}

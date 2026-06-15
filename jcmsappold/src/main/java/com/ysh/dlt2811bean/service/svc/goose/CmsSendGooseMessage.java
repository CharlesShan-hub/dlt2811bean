package com.ysh.dlt2811bean.service.svc.goose;

import com.ysh.dlt2811bean.datatypes.compound.CmsUtcTime;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import static com.ysh.dlt2811bean.service.protocol.enums.MessageType.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * GOOSE Service — CmsSendGooseMessage (send GOOSE message service).
 *
 * Corresponds to Table 57 in GB/T 45906.3-2025: CmsSendGooseMessage service parameters.
 *
 * Service type: GOOSE (Generic Object Oriented Substation Event)
 * Service interface: SendGooseMessage
 * Category: General station event service
 *
 * The SendGooseMessage service is used for transmitting real-time status data
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
public class CmsSendGooseMessage extends CmsAsdu<CmsSendGooseMessage> {

    // ==================== Fields based on Table 57 ====================

    @CmsField(only = {INDICATION})
    public CmsVisibleString goID = new CmsVisibleString().max(129);

    @CmsField(optional = true, only = {INDICATION})
    public CmsObjectReference datSet = new CmsObjectReference();

    @CmsField(optional = true, only = {INDICATION})
    public CmsObjectReference goRef = new CmsObjectReference();

    @CmsField(only = {INDICATION})
    public CmsUtcTime t = new CmsUtcTime();

    @CmsField(only = {INDICATION})
    public CmsInt32U stNum = new CmsInt32U();

    @CmsField(only = {INDICATION})
    public CmsInt32U sqNum = new CmsInt32U();

    @CmsField(only = {INDICATION})
    public CmsBoolean simulation = new CmsBoolean();

    @CmsField(only = {INDICATION})
    public CmsInt32U confRev = new CmsInt32U();

    @CmsField(only = {INDICATION})
    public CmsBoolean ndsCom = new CmsBoolean();

    @CmsField(only = {INDICATION})
    public CmsData<?> data = new CmsData<>();

    // ========================= Constructor ============================

    public CmsSendGooseMessage() {
        super(ServiceName.SEND_GOOSE_MESSAGE, MessageType.INDICATION);
    }

    public CmsSendGooseMessage(MessageType messageType) {
        super(ServiceName.SEND_GOOSE_MESSAGE, messageType);
    }

    // ====================== Convenience Setters =======================

    public CmsSendGooseMessage goID(String id) { 
        this.goID.set(id);
        return this; 
    }

    public CmsSendGooseMessage datSet(String ds) { 
        this.datSet.set(ds);
        return this; 
    }

    public CmsSendGooseMessage goRef(String ref) { 
        this.goRef.set(ref);
        return this; 
    }

    public CmsSendGooseMessage stNum(long n) { 
        this.stNum.set(n);
        return this; 
    }

    public CmsSendGooseMessage sqNum(long n) { 
        this.sqNum.set(n);
        return this; 
    }

    public CmsSendGooseMessage simulation(boolean sim) { 
        this.simulation.set(sim);
        return this; 
    }

    public CmsSendGooseMessage confRev(long rev) { 
        this.confRev.set(rev);
        return this; 
    }

    public CmsSendGooseMessage ndsCom(boolean nds) { 
        this.ndsCom.set(nds);
        return this; 
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsSendGooseMessage data(CmsType<?> val) { 
        this.data = new CmsData(val);
        return this; 
    }
}

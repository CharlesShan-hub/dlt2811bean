package com.ysh.dlt2811bean.service.svc.control;

import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x44 — Select (select service).
 *
 * Corresponds to Table 65 in GB/T 45906.3-2025: Select service parameters.
 *
 * Service code: 0x44 (68)
 * Service interface: Select
 * Category: Control service
 *
 * The Select service is used to select a controllable object (such as a switchgear
 * or setting group) prior to issuing an operate command. This two-step process
 * (Select followed by Operate) ensures that the intended object is ready to be
 * controlled and prevents unauthorized or accidental operations.
 *
 * This class supports all three message types:
 * <ul>
 *   <li>REQUEST - Client select request with target reference</li>
 *   <li>RESPONSE_POSITIVE - Server positive response confirming selection</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error details</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                           │
 * │ reference            [0] IMPLICIT ObjectReference    │
 * └──────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌──────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                           │
 * │ reference            [0] IMPLICIT ObjectReference    │
 * └──────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌──────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                           │
 * │ reference            [0] IMPLICIT ObjectReference    │
 * └──────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * Select-RequestPDU::= SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference
 * }
 *
 * Select-ResponsePDU::= SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference
 * }
 *
 * Select-ErrorPDU::= SEQUENCE {
 *   reference    [0] IMPLICIT ObjectReference
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSelect extends CmsAsdu<CmsSelect> {

    // ==================== Fields based on Table 65 ====================

    @CmsField(only = {"REQUEST", "REQUEST_POSITIVE", "REQUEST_NEGATIVE"})
    public CmsObjectReference reference = new CmsObjectReference();

    // ========================= Constructor ============================

    public CmsSelect(){
    }

    public CmsSelect(MessageType messageType) {
        super(messageType);
    }

    public CmsSelect(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsSelect reference(String ref) {
        this.reference.set(ref);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SELECT;
    }
}

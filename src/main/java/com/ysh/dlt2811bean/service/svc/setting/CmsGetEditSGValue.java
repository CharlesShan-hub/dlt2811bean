package com.ysh.dlt2811bean.service.svc.setting;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x3C — GetEditSGValue (get edit setting group values).
 *
 * Corresponds to Table 44 in GB/T 45906.3-2025: GetEditSGValue service parameters.
 *
 * Service code: 0x58 (88)
 * Service interface: GetEditSGValue
 * Category: Setting group service
 *
 * The GetEditSGValue service is used to retrieve the data of the edit setting group.
 * The functional constraint (fc) value is SG or SE.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get edit setting group values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with data and continuation flag</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ ASDUHeader (variable)                                       │
 * ├─────────────────────────────────────────────────────────────┤
 * │ data[0..n]                 SEQUENCE OF SEQUENCE {           │
 * │     reference              [0] IMPLICIT ObjectReference     │
 * │     fc                     [1] IMPLICIT FunctionalConstraint│
 * │ }                                                           │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ value[0..n]                SEQUENCE OF Data                 │
 * │ moreFollows                [1] IMPLICIT BOOLEAN (OPTIONAL)  │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response- ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ serviceError               ServiceError                     │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * ASN.1 Definition (from standard document):
 * <pre>
 * GetEditSGValue-RequestPDU::= SEQUENCE {
 *   data                [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference        [0] IMPLICIT ObjectReference,
 *     fc               [1] IMPLICIT FunctionalConstraint
 *   }
 * }
 *
 * GetEditSGValue-ResponsePDU::= SEQUENCE {
 *   value             [0] IMPLICIT SEQUENCE OF Data,
 *   moreFollows       [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetEditSGValue-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetEditSGValue extends CmsAsdu<CmsGetEditSGValue> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetEditSGValue(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetEditSGValue does not support " + messageType);
        }
    }

    public CmsGetEditSGValue(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_EDIT_SG_VALUE;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetEditSGValue copy() {
        CmsGetEditSGValue copy = new CmsGetEditSGValue(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetEditSGValue read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetEditSGValue) new CmsGetEditSGValue(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetEditSGValue getEditSGValue) {
        getEditSGValue.encode(pos);
    }

}

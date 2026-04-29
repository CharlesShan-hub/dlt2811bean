package com.ysh.dlt2811bean.service.svc.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x57 — SetDataValues (set data values).
 *
 * Corresponds to Table 32 in GB/T 45906.3-2025: SetDataValues service parameters.
 *
 * Service code: 0x30 (49)
 * Service interface: SetDataValues
 * Category: Data access service
 *
 * The SetDataValues service is used to batch set the values of a set of data.
 * Each data is uniquely indexed by Reference. When fc (functional constraint)
 * is included, it indicates the value of the FCD. When fc is not included,
 * it indicates the values of all data attributes. A positive response is returned
 * if all data values are set successfully; a negative response is returned if
 * some or all fail. In the negative response, the setting result for each data
 * value is returned in order.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Set data values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response (no payload)</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with result list</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ data[0..n]                                                  │
 * │   └─ SEQUENCE {                                             │
 * │       reference            ObjectReference                  │
 * │       fc                   FunctionalConstraint (OPTIONAL)  │
 * │       value                Data                             │
 * │   }                                                         │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response Positive ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ (NULL)                                                      │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response Negative ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ result[0..n]               ServiceError                     │
 * └─────────────────────────────────────────────────────────────┘
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSetDataValues extends CmsAsdu<CmsSetDataValues> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsSetDataValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("SetDataValues does not support " + messageType);
        }
    }

    public CmsSetDataValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_DATA_VALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsSetDataValues copy() {
        CmsSetDataValues copy = new CmsSetDataValues(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsSetDataValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsSetDataValues) new CmsSetDataValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsSetDataValues setDataValues) {
        setDataValues.encode(pos);
    }

}

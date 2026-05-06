package com.ysh.dlt2811bean.service.svc.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsSetDataValuesEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
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

    // ==================== Fields based on Table 32====================

    @CmsField(only = {"REQUEST"})
    public CmsArray<CmsSetDataValuesEntry> data = new CmsArray<>(CmsSetDataValuesEntry::new).capacity(100);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsArray<CmsServiceError> result = new CmsArray<>(CmsServiceError::new).capacity(100);

    // ========================= Constructor ============================

    public CmsSetDataValues() {
        super(ServiceName.SET_DATA_VALUES);
    }
    
    public CmsSetDataValues(MessageType messageType) {
        super(ServiceName.SET_DATA_VALUES, messageType);
    }

    public CmsSetDataValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsSetDataValues addData(String reference, String fc, com.ysh.dlt2811bean.datatypes.type.CmsType<?> value) {
        CmsSetDataValuesEntry entry = new CmsSetDataValuesEntry()
            .reference(reference)
            .fc(fc)
            .value(value);
        this.data.add(entry);
        return this;
    }
}

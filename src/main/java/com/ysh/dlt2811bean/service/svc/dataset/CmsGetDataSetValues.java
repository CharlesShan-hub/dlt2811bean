package com.ysh.dlt2811bean.service.svc.dataset;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x3A — GetDataSetValues (read data set values).
 *
 * Corresponds to Table 35 in GB/T 45906.3-2025: GetDataSetValues service parameters.
 *
 * Service code: 0x3A (58)
 * Service interface: GetDataSetValues
 * Category: Data set service
 *
 * The GetDataSetValues service is used to batch retrieve the values of data set members.
 * When referenceAfter is not specified, values should be returned sequentially starting
 * from the first member of the data set. When referenceAfter is specified, values should
 * be returned sequentially starting after the referenceAfter member.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get data set values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with member values</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ datasetReference            ObjectReference                  │
 * │ referenceAfter              [0..1] ObjectReference OPTIONAL  │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response ASDU (positive):
 * ┌─────────────────────────────────────────────────────────────┐
 * │ value                       [0] SEQUENCE OF Data            │
 * │ moreFollows                 [1] BOOLEAN DEFAULT TRUE        │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * Service requirements:
 * <ul>
 *   <li>If referenceAfter is not specified, return values sequentially from the first member.</li>
 *   <li>If referenceAfter is specified, return values sequentially after that member.</li>
 *   <li>If one ASDU cannot return all data values, set moreFollows to TRUE.</li>
 *   <li>If data set does not exist or a member cannot be accessed, return error response.</li>
 * </ul>
 *
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetDataSetValues extends CmsAsdu<CmsGetDataSetValues> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsGetDataSetValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("GetDataSetValues does not support " + messageType);
        }
    }

    public CmsGetDataSetValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_DATA_SET_VALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetDataSetValues copy() {
        CmsGetDataSetValues copy = new CmsGetDataSetValues(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetDataSetValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetDataSetValues) new CmsGetDataSetValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetDataSetValues getDataSetValues) {
        getDataSetValues.encode(pos);
    }

}

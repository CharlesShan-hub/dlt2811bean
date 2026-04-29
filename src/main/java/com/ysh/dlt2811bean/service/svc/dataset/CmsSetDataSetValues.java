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
 * CMS Service Code 0x3B — SetDataSetValues (set data set values).
 *
 * Corresponds to Table 36 in GB/T 45906.3-2025: SetDataSetValues service parameters.
 *
 * Service code: 0x3B (59)
 * Service interface: SetDataSetValues
 * Category: Data set service
 *
 * The SetDataSetValues service is used to batch set the values of data set members.
 * Each data should be arranged in the index order within the data set. When referenceAfter
 * is not specified, values should be set sequentially starting from the first member of
 * the data set. When referenceAfter is specified, values should be set sequentially
 * starting after the referenceAfter member.
 *
 * Response behavior:
 * <ul>
 *   <li>If all data set values are set successfully, return Response+ (positive response).</li>
 *   <li>If partial or all settings fail, return Response- (negative response) with the result
 *       of each data set value setting.</li>
 * </ul>
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Set data set values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response (NULL)</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response with error results</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌──────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                   │
 * │ datasetReference                 ObjectReference             │
 * │ referenceAfter [0..1]            ObjectReference (OPTIONAL)  │
 * │ memberValue [1..n]               SEQUENCE OF Data            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * Response+ (Positive): NULL (no data returned)
 *
 * Response- (Negative):
 * ┌─────────────────────────────────────────────────────────────┐
 * │ result [1..n]                    SEQUENCE OF ServiceError   │
 * └─────────────────────────────────────────────────────────────┘
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsSetDataSetValues extends CmsAsdu<CmsSetDataSetValues> {

    // ==================== Fields based on Table XX ====================

    // ========================= Constructor ============================

    public CmsSetDataSetValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
        } else {
            throw new IllegalArgumentException("SetDataSetValues does not support " + messageType);
        }
    }

    public CmsSetDataSetValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_DATA_SET_VALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsSetDataSetValues copy() {
        CmsSetDataSetValues copy = new CmsSetDataSetValues(messageType());
        // todo
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsSetDataSetValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsSetDataSetValues) new CmsSetDataSetValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsSetDataSetValues setDataSetValues) {
        setDataSetValues.encode(pos);
    }

}

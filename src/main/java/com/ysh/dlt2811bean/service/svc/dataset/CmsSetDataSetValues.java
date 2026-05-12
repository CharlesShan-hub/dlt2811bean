package com.ysh.dlt2811bean.service.svc.dataset;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import static com.ysh.dlt2811bean.service.protocol.enums.MessageType.*;
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

    // ==================== Fields based on Table 36 ====================

    @CmsField(only = {REQUEST})
    public CmsObjectReference datasetReference = new CmsObjectReference();

    @CmsField(optional = true, only = {REQUEST})
    public CmsObjectReference referenceAfter = new CmsObjectReference();
    
    @CmsField(only = {REQUEST})
    public CmsStructure memberValue = new CmsStructure();

    @CmsField(only = {RESPONSE_NEGATIVE})
    public CmsArray<CmsServiceError> result = new CmsArray<>(CmsServiceError::new);

    // ========================= Constructor ============================

    public CmsSetDataSetValues() {
        super(ServiceName.SET_DATA_SET_VALUES);
    }
    
    public CmsSetDataSetValues(MessageType messageType) {
        super(ServiceName.SET_DATA_SET_VALUES, messageType);
    }

    public CmsSetDataSetValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsSetDataSetValues datasetReference(String ref) {
        this.datasetReference.set(ref);
        return this;
    }

    public CmsSetDataSetValues referenceAfter(String ref) {
        this.referenceAfter.set(ref);
        return this;
    }

    public CmsSetDataSetValues addMemberValue(com.ysh.dlt2811bean.datatypes.type.CmsType<?> val) {
        this.memberValue.add(val);
        return this;
    }

    public CmsSetDataSetValues addResult(int errorCode) {
        this.result.add(new CmsServiceError(errorCode));
        return this;
    }
}

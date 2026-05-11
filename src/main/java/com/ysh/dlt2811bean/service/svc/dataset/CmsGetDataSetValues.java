package com.ysh.dlt2811bean.service.svc.dataset;

import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
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

    // ==================== Fields based on Table 35 ====================

    @CmsField(only = {REQUEST})
    public CmsObjectReference datasetReference = new CmsObjectReference();

    @CmsField(optional = true, only = {REQUEST})
    public CmsObjectReference referenceAfter = new CmsObjectReference();

    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsStructure value = new CmsStructure().capacity(100);

    @CmsField(only = {RESPONSE_POSITIVE})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {RESPONSE_NEGATIVE})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetDataSetValues() {
        super(ServiceName.GET_DATA_SET_VALUES);
    }

    public CmsGetDataSetValues(MessageType messageType) {
        super(ServiceName.GET_DATA_SET_VALUES, messageType);
    }

    public CmsGetDataSetValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetDataSetValues datasetReference(String ref) {
        this.datasetReference.set(ref);
        return this;
    }

    public CmsGetDataSetValues referenceAfter(String ref) {
        this.referenceAfter.set(ref);
        return this;
    }

    public CmsGetDataSetValues serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }
}

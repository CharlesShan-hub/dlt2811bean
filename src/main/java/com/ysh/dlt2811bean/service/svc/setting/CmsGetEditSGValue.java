package com.ysh.dlt2811bean.service.svc.setting;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.service.svc.dataset.datatypes.CmsCreateDataSetEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
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

    // ==================== Fields based on Table 44 ====================

    @CmsField(only = {"REQUEST"})
    public CmsArray<CmsCreateDataSetEntry> data = new CmsArray<>(CmsCreateDataSetEntry::new).capacity(100);

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsStructure value = new CmsStructure().capacity(100);
    
    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetEditSGValue() {
    }

    public CmsGetEditSGValue(MessageType messageType) {
        super(messageType);
    }

    public CmsGetEditSGValue(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetEditSGValue addData(String reference, String fc) {
        this.data.add(new CmsCreateDataSetEntry()
            .reference(reference)
            .fc(fc));
        return this;
    }

    public CmsGetEditSGValue serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_EDIT_SG_VALUE;
    }
}

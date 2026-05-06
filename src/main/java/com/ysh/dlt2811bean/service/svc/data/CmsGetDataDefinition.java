package com.ysh.dlt2811bean.service.svc.data;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataDefinitionEntry;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;

/**
 * CMS Service Code 0x33 — GetDataDefinition (read data definition).
 *
 * Corresponds to Table 34 in GB/T 45906.3-2025: GetDataDefinition service parameters.
 *
 * Service code: 0x33 (51)
 * Service interface: GetDataDefinition
 * Category: Data access service
 *
 * The GetDataDefinition service is used to retrieve the structural definitions of a set of data objects
 * or data attributes. When the data is a data object, the cdcType should be set to the corresponding
 * CDC type; when the data is a data attribute, cdcType should be empty.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get data definition request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with data definitions</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ data[0..n]                 SEQUENCE OF SEQUENCE {           │
 * │   reference                ObjectReference                  │
 * │   fc                       FunctionalConstraint (OPTIONAL)  │
 * │ }                                                           │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ data[0..n]                 SEQUENCE OF SEQUENCE {           │
 * │   cdcType                  VisibleString (OPTIONAL)         │
 * │   definition               DataDefinition                   │
 * │ }                                                           │
 * │ moreFollows                BOOLEAN DEFAULT TRUE             │
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
 * GetDataDefinition-RequestPDU::= SEQUENCE {
 *   data               [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference         [0] IMPLICIT ObjectReference,
 *     fc                [1] IMPLICIT FunctionalConstraint OPTIONAL
 *   }
 * }
 *
 * GetDataDefinition-ResponsePDU::= SEQUENCE {
 *   data               [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     cdcType           [0] IMPLICIT VisibleString OPTIONAL,
 *     definition        [1] IMPLICIT DataDefinition
 *   },
 *   moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetDataDefinition-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetDataDefinition extends CmsAsdu<CmsGetDataDefinition> {

    // ==================== Fields based on Table 34 ====================

    @CmsField(only = {"REQUEST"})
    public CmsArray<CmsGetDataValuesEntry> data = new CmsArray<>(CmsGetDataValuesEntry::new).capacity(100);

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsArray<CmsGetDataDefinitionEntry> definition = new CmsArray<>(CmsGetDataDefinitionEntry::new).capacity(100);
    
    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetDataDefinition() {
    }

    public CmsGetDataDefinition(MessageType messageType) {
        super(messageType);
    }

    public CmsGetDataDefinition(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    // ====================== Convenience Setters =======================

    public CmsGetDataDefinition addData(String reference, String fc) {
        CmsGetDataValuesEntry entry = new CmsGetDataValuesEntry()
            .reference(reference)
            .fc(fc);
        this.data.add(entry);
        return this;
    }

    public CmsGetDataDefinition addData(String reference) {
        CmsGetDataValuesEntry entry = new CmsGetDataValuesEntry()
            .reference(reference);
        this.data.add(entry);
        return this;
    }

    public CmsGetDataDefinition serviceError(int errorCode) {
        this.serviceError.set(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_DATA_DEFINITION;
    }
}

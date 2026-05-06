package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.type.CmsField;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataEntry;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsReference;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * CMS Service Code 0x53 — GetAllDataValues (read all data values).
 *
 * Corresponds to Table 27 in GB/T 45906.3-2025: GetAllDataValues service parameters.
 *
 * Service code: 0x53 (83)
 * Service interface: GetAllDataValues
 * Category: Data access service
 *
 * The GetAllDataValues service is used to retrieve the values of all data objects
 * under a specified logical device or logical node. The fc parameter is used to
 * filter specific functional constraint attributes.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get all data values request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with data values</li>
 *   <li>RESPONSE_NEGATIVE - Server negative response</li>
 * </ul>
 *
 * ASDU field layout (PER encoded, in order):
 * <pre>
 * Request ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ reference                  ObjectName / ObjectReference     │
 * │ fc                         FunctionalConstraint (OPTIONAL)  │
 * │ referenceAfter             ObjectReference (OPTIONAL)       │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Response+ ASDU:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ ReqID (2B)                                                  │
 * │ data[0..n]                 SEQUENCE OF SEQUENCE {           │
 * │   reference                SubReference                     │
 * │   value                   Data                              │
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
 * GetAllDataValues-RequestPDU::= SEQUENCE {
 *   reference          [0] IMPLICIT CHOICE {
 *     ldName            [0] IMPLICIT ObjectName,
 *     lnReference       [1] IMPLICIT ObjectReference
 *   },
 *   fc                 [1] IMPLIC FunctionalConstrITaint OPTIONAL,
 *   referenceAfter     [2] IMPLICIT ObjectReference OPTIONAL
 * }
 *
 * GetAllDataValues-ResponsePDU::= SEQUENCE {
 *   data               [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference         [0] IMPLICIT SubReference,
 *     value             [1] IMPLICIT Data
 *   },
 *   moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetAllDataValues-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetAllDataValues extends CmsAsdu<CmsGetAllDataValues> {

    // ==================== Fields based on Table 27 ====================

    @CmsField(only = {"REQUEST"})
    public CmsReference reference = new CmsReference();

    @CmsField(optional = true, only = {"REQUEST"})
    public CmsFC fc = new CmsFC();

    @CmsField(optional = true, only = {"REQUEST"})
    public CmsObjectReference referenceAfter = new CmsObjectReference();

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsArray<CmsDataEntry> data = new CmsArray<>(CmsDataEntry::new).capacity(100);

    @CmsField(only = {"RESPONSE_POSITIVE"})
    public CmsBoolean moreFollows = new CmsBoolean(true);

    @CmsField(only = {"RESPONSE_NEGATIVE"})
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    // ========================= Constructor ============================

    public CmsGetAllDataValues() {
        super(ServiceName.GET_ALL_DATA_VALUES);
    }
    
    public CmsGetAllDataValues(MessageType messageType) {
        super(ServiceName.GET_ALL_DATA_VALUES, messageType);
    }

    public CmsGetAllDataValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }


    // ==================== Convenience Setters ====================

    public CmsGetAllDataValues ldName(String name) {
        this.reference.ldName(name);
        return this;
    }

    public CmsGetAllDataValues lnReference(String ref) {
        this.reference.lnReference(ref);
        return this;
    }

    public CmsGetAllDataValues fc(String fc) {
        this.fc = new CmsFC(fc);
        return this;
    }

    public CmsGetAllDataValues referenceAfter(String ref) {
        this.referenceAfter = new CmsObjectReference(ref);
        return this;
    }

    public CmsGetAllDataValues serviceError(int errorCode) {
        this.serviceError = new CmsServiceError(errorCode);
        return this;
    }
}

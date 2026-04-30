package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * CMS Service Code 0x54 — GetAllDataDefinition (read all data definition).
 *
 * Corresponds to Table 29 in GB/T 45906.3-2025: GetAllDataDefinition service parameters.
 *
 * Service code: 0x9B (155)
 * Service interface: GetAllDataDefinition
 * Category: Data access service
 *
 * The GetAllDataDefinition service is used to retrieve the definitions of all data objects
 * under a specified logical device or logical node. The fc parameter is used to
 * filter specific functional constraint attributes.
 *
 * This class supports three message types:
 * <ul>
 *   <li>REQUEST - Get all data definition request</li>
 *   <li>RESPONSE_POSITIVE - Server positive response with data definitions</li>
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
 * │ data                       SEQUENCE OF SEQUENCE {           │
 * │   reference                SubReference                     │
 * │   cdcType                 VisibleString (OPTIONAL)          │
 * │   definition              DataDefinition                    │
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
 * GetAllDataDefinition-RequestPDU::= SEQUENCE {
 *   reference          [0] IMPLICIT CHOICE {
 *     ldName            [0] IMPLICIT ObjectName,
 *     lnReference       [1] IMPLICIT ObjectReference
 *   },
 *   fc                 [1] IMPLICIT FunctionalConstraint OPTIONAL,
 *   referenceAfter     [2] IMPLICIT ObjectReference OPTIONAL
 * }
 *
 * GetAllDataDefinition-ResponsePDU::= SEQUENCE {
 *   data               [0] IMPLICIT SEQUENCE OF SEQUENCE {
 *     reference         [0] IMPLICIT SubReference,
 *     cdcType           [1] IMPLICIT VisibleString OPTIONAL,
 *     definition        [2] IMPLICIT DataDefinition
 *   },
 *   moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 *
 * GetAllDataDefinition-ErrorPDU::= ServiceError
 * </pre>
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsGetAllDataDefinition extends CmsAsdu<CmsGetAllDataDefinition> {

    // ==================== Fields based on Table 29 ====================

    // --- Request parameters ---
    public CmsReference reference = new CmsReference();
    public CmsFC fc = new CmsFC();
    public CmsObjectReference referenceAfter = new CmsObjectReference();

    // --- Response+ parameters ---
    public CmsArray<CmsDataDefinitionEntry> data = new CmsArray<>(CmsDataDefinitionEntry::new).capacity(100);
    public CmsBoolean moreFollows = new CmsBoolean(true);

    // --- Response- parameters ---
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);
    
    // ========================= Constructor ============================
    
    public CmsGetAllDataDefinition(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("reference");
            registerOptionalField("fc");
            registerOptionalField("referenceAfter");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("data");
            registerField("moreFollows");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("GetAllDataDefinition does not support " + messageType);
        }
    }

    // ====================== Convenience Setters =======================

    public CmsGetAllDataDefinition(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }

    public CmsGetAllDataDefinition ldName(String name) {
        this.reference.ldName(name);
        return this;
    }

    public CmsGetAllDataDefinition lnReference(String ref) {
        this.reference.lnReference(ref);
        return this;
    }

    public CmsGetAllDataDefinition fc(String fc) {
        this.fc = new CmsFC(fc);
        return this;
    }

    public CmsGetAllDataDefinition referenceAfter(String ref) {
        this.referenceAfter = new CmsObjectReference(ref);
        return this;
    }

    public CmsGetAllDataDefinition serviceError(int errorCode) {
        this.serviceError = new CmsServiceError(errorCode);
        return this;
    }

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_ALL_DATA_DEFINITION;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetAllDataDefinition copy() {
        CmsGetAllDataDefinition copy = new CmsGetAllDataDefinition(messageType());
        copy.reqId.set(reqId.get());
        copy.reference = reference.copy();
        copy.fc = fc.copy();
        copy.referenceAfter = referenceAfter.copy();
        copy.data = data.copy();
        copy.moreFollows = moreFollows.copy();
        copy.serviceError = serviceError.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetAllDataDefinition read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetAllDataDefinition) new CmsGetAllDataDefinition(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetAllDataDefinition getAllDataDefinition) {
        getAllDataDefinition.encode(pos);
    }

}

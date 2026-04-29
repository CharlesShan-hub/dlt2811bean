package com.ysh.dlt2811bean.service.svc.directory;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectName;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsSubReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
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
 * │ moreFollows                BOOLEAN (OPTIONAL)               │
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

    // ==================== Inner type: DataEntry ====================

    /**
     * Represents a single entry in the data SEQUENCE.
     *
     * <p>ASN.1: SEQUENCE { reference [0] IMPLICIT SubReference, value [1] IMPLICIT Data }
     *
     * <p>In PER, IMPLICIT tags are not encoded; the inner types are encoded directly
     * in sequence order: first SubReference, then Data.
     */
    @Getter
    @Setter
    @Accessors(fluent = true)
    @SuppressWarnings("rawtypes")
    public static class CmsDataEntry extends AbstractCmsCompound<CmsDataEntry> {

        public CmsSubReference reference = new CmsSubReference();
        public CmsData value = new CmsData<>();

        public CmsDataEntry() {
            super("DataEntry");
            registerField("reference");
            registerField("value");
        }

        public CmsDataEntry reference(String ref) {
            this.reference = new CmsSubReference(ref);
            return this;
        }

        public CmsDataEntry value(CmsType<?> val) {
            this.value.set(val);
            return this;
        }

        @Override
        public CmsDataEntry copy() {
            CmsDataEntry copy = new CmsDataEntry();
            copy.reference = reference.copy();
            copy.value = value.copy();
            return copy;
        }
    }

    // ==================== Fields based on Table 27 ====================

    // --- Request parameters ---
    // reference CHOICE { ldName [0] IMPLICIT ObjectName, lnReference [1] IMPLICIT ObjectReference }
    public CmsObjectName ldName = new CmsObjectName();
    public CmsObjectReference lnReference = new CmsObjectReference();

    // fc [0..1] FunctionalConstraint (optional)
    public CmsFC fc = new CmsFC();

    // referenceAfter [0..1] ObjectReference (optional)
    public CmsObjectReference referenceAfter = new CmsObjectReference();

    // --- Response+ parameters ---
    // data [0..n] SEQUENCE OF SEQUENCE { reference SubReference, value Data }
    public CmsArray<CmsDataEntry> data = new CmsArray<>(CmsDataEntry::new).capacity(100);

    // moreFollows [0..1] BOOLEAN DEFAULT TRUE
    public CmsBoolean moreFollows = new CmsBoolean();

    // --- Response- parameters ---
    // serviceError ServiceError
    public CmsServiceError serviceError = new CmsServiceError(CmsServiceError.NO_ERROR);

    public CmsGetAllDataValues(MessageType messageType) {
        super(messageType);
        if (messageType == MessageType.REQUEST) {
            registerField("ldName");
            registerField("lnReference");
            registerField("fc");
            registerField("referenceAfter");
        } else if (messageType == MessageType.RESPONSE_POSITIVE) {
            registerField("data");
            registerField("moreFollows");
        } else if (messageType == MessageType.RESPONSE_NEGATIVE) {
            registerField("serviceError");
        } else {
            throw new IllegalArgumentException("GetAllDataValues does not support " + messageType);
        }
    }

    public CmsGetAllDataValues(boolean isResp, boolean isErr) {
        this(getRRMessageType(isResp, isErr));
    }


    // ==================== Convenience Setters ====================

    public CmsGetAllDataValues ldName(String name) {
        this.ldName = new CmsObjectName(name);
        return this;
    }

    public CmsGetAllDataValues lnReference(String ref) {
        this.lnReference = new CmsObjectReference(ref);
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

    // ==================== CmsAsdu Abstract Methods ====================

    @Override
    public ServiceCode getServiceCode() {
        return ServiceCode.GET_ALL_DATA_VALUES;
    }

    // ==================== CmsType Implementation ====================

    @Override
    public CmsGetAllDataValues copy() {
        CmsGetAllDataValues copy = new CmsGetAllDataValues(messageType());
        copy.reqId.set(reqId.get());
        copy.ldName = ldName.copy();
        copy.lnReference = lnReference.copy();
        copy.fc = fc.copy();
        copy.referenceAfter = referenceAfter.copy();
        copy.data = data.copy();
        copy.moreFollows = moreFollows.copy();
        copy.serviceError = serviceError.copy();
        return copy;
    }

    // ==================== Static Convenience Methods ====================

    @SuppressWarnings("unchecked")
    public static CmsGetAllDataValues read(PerInputStream pis, MessageType messageType) throws Exception {
        return (CmsGetAllDataValues) new CmsGetAllDataValues(messageType).decode(pis);
    }

    public static void write(PerOutputStream pos, CmsGetAllDataValues service) {
        service.encode(pos);
    }
}
